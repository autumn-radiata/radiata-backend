package radiata.service.order.core.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import radiata.common.domain.order.dto.request.OrderPaymentRequestDto;
import radiata.common.domain.order.dto.response.OrderResponseDto;
import radiata.service.order.core.domain.model.constant.OrderStatus;
import radiata.service.order.core.domain.model.entity.Order;
import radiata.service.order.core.domain.model.entity.OrderItem;
import radiata.service.order.core.implemetation.OrderReader;
import radiata.service.order.core.implemetation.OrderValidator;
import radiata.service.order.core.service.mapper.OrderMapper;

@Service
@RequiredArgsConstructor
public class OrderRequestService {

    private final OrderReader orderReader;
    private final OrderMapper orderMapper;
    private final OrderValidator orderValidator;
    private final OrderItemService orderItemService;


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
//        order.setPaymentIdAndType(paymentId, type);
        // 주문 상태 체크 & 변경(결제 완료)
        orderValidator.checkCanMoveToNextStatus(order, OrderStatus.PAYMENT_COMPLETED);
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
