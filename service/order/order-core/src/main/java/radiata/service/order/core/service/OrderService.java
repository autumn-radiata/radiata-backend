package radiata.service.order.core.service;

import feign.FeignException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import radiata.common.domain.order.dto.request.OrderCreateRequestDto;
import radiata.common.domain.order.dto.request.OrderItemCreateRequestDto;
import radiata.common.domain.order.dto.request.OrderPaymentRequestDto;
import radiata.common.domain.order.dto.response.OrderResponseDto;
import radiata.common.exception.BusinessException;
import radiata.common.message.ExceptionMessage;
import radiata.service.order.core.domain.model.constant.OrderStatus;
import radiata.service.order.core.domain.model.entity.Order;
import radiata.service.order.core.domain.model.entity.OrderItem;
import radiata.service.order.core.implemetation.OrderIdCreator;
import radiata.service.order.core.implemetation.OrderReader;
import radiata.service.order.core.implemetation.OrderSaver;
import radiata.service.order.core.implemetation.OrderValidator;
import radiata.service.order.core.service.client.CouponIssueClient;
import radiata.service.order.core.service.client.ProductClient;
import radiata.service.order.core.service.client.UserClient;
import radiata.service.order.core.service.mapper.OrderItemMapper;
import radiata.service.order.core.service.mapper.OrderMapper;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderIdCreator orderIdCreator;
    private final OrderSaver orderSaver;
    private final OrderReader orderReader;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final OrderValidator orderValidator;
    private final OrderItemService orderItemService;
    private final ProductClient productClient;
    private final CouponIssueClient couponIssueClient;
    private final UserClient userClient;


    // 주문 생성
    @Transactional
    public OrderResponseDto createOrder(OrderCreateRequestDto requestDto, String userId) {
        // 주문 ID 생성
        String orderId = orderIdCreator.create();
        // 초기 주문 생성
        Order order = orderSaver.save(orderMapper.toEntity(requestDto, orderId, userId));
        // 주문 상품 목록 - set
        Set<OrderItem> orderItems = new HashSet<>();
        // 총 주문 금액 - set
        int orderPrice = 0;
        // 보상 트랜잭션 관리 변수
        // TODO - 타입 클래스를 따로 만들어 줘야할 거 같음 - 사이즈, 갯수, 아이디
        List<String> deductedProducts = new ArrayList<>();  // 재고 차감 목록
        List<String> usedCoupons = new ArrayList<>();       // 사용된 쿠폰 목록

        for (OrderItemCreateRequestDto itemCreateDto : requestDto.itemList()) {
            try {
//                // 1️⃣ 재고 확인 및 차감
//                String productId = itemCreateDto.productId();
//                productClient.getProductInfo(productId);  // 상품 조회
////                productClient.deductStock(productId);     // 재고 차감
//                deductedProducts.add(productId);          // 재고 차감 목록 추가
//
//                // 2️⃣ 쿠폰 사용 여부 체크
//                String couponIssuedId = itemCreateDto.couponIssuedId();
//                if (couponIssuedId != null) {
//                    couponIssueClient.getCouponIssue(couponIssuedId, userId).getBody();  // 쿠폰 조회
//                    couponIssueClient.useCouponIssue(couponIssuedId, userId).getBody();  // 쿠폰 사용
//                    usedCoupons.add(couponIssuedId);                                     // 사용된 쿠폰 목록 추가
//                }

                // 주문 상품 ID 생성 및 주문 상품 목록에 추가
                String orderItemId = orderIdCreator.create();
                OrderItem orderItem = orderItemMapper.toEntity(itemCreateDto, orderItemId, order);
                orderItems.add(orderItem);
                // 주문 금액 추가
                orderPrice += (orderItem.getUnitPrice() * orderItem.getQuantity());

            } catch (FeignException e) {
                // 실패 시 SAGA 보상 트랜잭션 처리 (Kafka 사용)
                orderItemService.rollbackTransaction(deductedProducts, usedCoupons);
                throw new BusinessException(ExceptionMessage.ORDER_CREATION_FAILED);
            }
        }

        /* 적립금 사용 여부 체크
            1) 미사용 - Null
            👉 다음 단계

            2) 사용 - NotNull
            👉 적립금 차감 시도
                1) 성공 - 적립금 차감 -> 다음
                2) 실패 -
                    쿠폰 상태 USED -> ISSUED 로 요청(보상2)
                    재고 차감 -> 증감 요청(보상1)
         */

        // 주문에 상품목록 지정 - setOrderItems
        order.setOrderItems(orderItems);
        // 결제 금액 지정 - setOrderPrice
        order.setOrderPrice(orderPrice);
        // 주문 상품 목록 추가 & 반환
        return orderMapper.toDto(order).withItemList(orderItemService.toDtoSet(order.getOrderItems()));
    }

    // 주문 상세 조회
    @Transactional(readOnly = true)
    public OrderResponseDto getOrder(String orderId, String userId) {
        // 주문 조회
        Order order = orderReader.readOrder(orderId);
        // 사용자의 주문 내역인지 확인
        orderValidator.validateUserOwnsOrder(order.getUserId(), userId);
        // 주문 상품 목록 추가 & 반환
        return orderMapper.toDto(order).withItemList(orderItemService.toDtoSet(order.getOrderItems()));
    }

    // 결제 요청 -> 주문 상태 (결제 요청 중 -> 결제 대기 중 -> 결제 완료)
    @Transactional
    public OrderResponseDto sendPaymentRequest(String orderId, String userId, OrderPaymentRequestDto requestDto) {
        // 주문 조회
        Order order = orderReader.readOrder(orderId);
        // 유저의 주문인지 체크
        orderValidator.validateUserOwnsOrder(order.getUserId(), userId);
        // 주문 상태 체크 & 변경(결제 대기 중)
        orderValidator.checkCanMoveToNextStatus(order, OrderStatus.PAYMENT_PENDING);
        // 주문 금액 일치 확인
        orderValidator.IsEqualsOrderPrice(requestDto.amount(), order.getOrderPrice());

        /* TODO - 결제 승인 요청 (openFeign) orderId, tossPaymentId, amount - Merge 후
            1) 성공 - 다음
            2) 실패 시 (FeignException - 결제 실패)
            - 주문 취소 처리 - 보상 트랜잭션(롤백)
         */

        // paymentId 지정
//        order.setPaymentId(paymentId);
        // 주문 상태 체크 & 변경(결제 완료)
        orderValidator.checkCanMoveToNextStatus(order, OrderStatus.PAYMENT_COMPLETED);
        // 반환
        return orderMapper.toDto(order).withItemList(orderItemService.toDtoSet(order.getOrderItems()));
    }

    // 주문 상태 변경 (배송 대기 중)
    @Transactional
    public OrderResponseDto updateStatusPendingShipping(String orderId) {
        // 주문 조회
        Order order = orderReader.readOrder(orderId);
        // 주문 상태 체크 & 변경(배송 대기 중)
        orderValidator.checkCanMoveToNextStatus(order, OrderStatus.SHIPPING_PENDING);
        // 반환
        return orderMapper.toDto(order).withItemList(orderItemService.toDtoSet(order.getOrderItems()));
    }

    // 주문 상태 변경 (배송 중)
    @Transactional
    public OrderResponseDto updateStatusShipping(String orderId) {
        // 주문 조회
        Order order = orderReader.readOrder(orderId);
        // 주문 상태 체크 & 변경(배송 중)
        orderValidator.checkCanMoveToNextStatus(order, OrderStatus.SHIPPING_IN_PROGRESS);
        // 반환
        return orderMapper.toDto(order).withItemList(orderItemService.toDtoSet(order.getOrderItems()));
    }

    // 주문 상태 변경 (배송 완료)
    @Transactional
    public OrderResponseDto updateStatusCompletedShipping(String orderId) {
        // 주문 조회
        Order order = orderReader.readOrder(orderId);
        // 주문 상태 체크 & 변경(배송 완료)
        orderValidator.checkCanMoveToNextStatus(order, OrderStatus.SHIPPING_COMPLETED);
        // 반환
        return orderMapper.toDto(order).withItemList(orderItemService.toDtoSet(order.getOrderItems()));
    }


    // 주문 취소 요청
    @Transactional
    public OrderResponseDto cancelOrder(String orderId, String userId) {
        // 주문 조회
        Order order = orderReader.readOrder(orderId);
        // 사용자의 주문 내역인지 확인
        orderValidator.validateUserOwnsOrder(order.getUserId(), userId);
        // 주문 상태 체크 - 결제 단계만 주문 취소 가능
        orderValidator.checkStatusIsPaymentLevel(order.getStatus());
        // 주문 상태 업데이트 - 주문 취소 요청
        order.updateOrderStatus(OrderStatus.PAYMENT_CANCEL_REQUESTED);
        // TODO - PaymentClient로 결제 취소 요청. & 롤백 (주문 상품 목록 재고 ++ & 쿠폰 이슈 복구 & 주문 상태 이전으로? - 스케줄러)
        for (OrderItem orderItem : order.getOrderItems()) {
            /*
            try - 1) 재고 증감
             */

            /*
            try - 2) 쿠폰 상태 변경 - USED -> ISSUED
            */
        }
        // 주문 상태 체크
        orderValidator.checkStatusIsPaymentCancelRequested(order.getStatus());
        // 주문 상태 업데이트 - 주문 취소 완료
        order.updateOrderStatus(OrderStatus.PAYMENT_CANCEL_COMPLETED);
        // 반환
        return orderMapper.toDto(order).withItemList(orderItemService.toDtoSet(order.getOrderItems()));
    }
}
