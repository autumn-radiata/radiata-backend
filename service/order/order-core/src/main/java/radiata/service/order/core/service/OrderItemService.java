package radiata.service.order.core.service;

import feign.FeignException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import radiata.common.annotation.Implementation;
import radiata.common.domain.order.dto.request.OrderCreateRequestDto;
import radiata.common.domain.order.dto.request.OrderItemCreateRequestDto;
import radiata.common.domain.order.dto.response.OrderItemResponseDto;
import radiata.common.exception.BusinessException;
import radiata.common.message.ExceptionMessage;
import radiata.service.order.core.domain.model.entity.Order;
import radiata.service.order.core.domain.model.entity.OrderItem;
import radiata.service.order.core.implemetation.OrderIdCreator;
import radiata.service.order.core.service.client.CouponIssueClient;
import radiata.service.order.core.service.client.ProductClient;
import radiata.service.order.core.service.client.TimeSaleProductClient;
import radiata.service.order.core.service.client.UserClient;
import radiata.service.order.core.service.mapper.OrderItemMapper;

@Implementation
@Slf4j(topic = "OrderItemService")
@RequiredArgsConstructor
public class OrderItemService {

    private final OrderItemMapper orderItemMapper;
    private final OrderIdCreator orderIdCreator;
    private final TimeSaleProductClient timeSaleProductClient;
    private final ProductClient productClient;
    private final CouponIssueClient couponIssueClient;
    private final UserClient userClient;

    public Set<OrderItemResponseDto> toDtoSet(Set<OrderItem> orderItems) {

        return orderItems.stream()
            .map(orderItemMapper::toDto)
            .collect(Collectors.toSet());
    }


    // 보상 트랜잭션 - 타임 세일 상품 재고
    private void rollbackTimeSaleStock(List<String> deductedTimeSaleStocks) {
        try {
            // 타임세일 상품 재고 증감 요청
            deductedTimeSaleStocks.forEach(timeSaleProduct -> {
                // TODO - kafka 를 이용한 비동기 처리
                // A->B->C
            });
        } catch (Exception e) {
            log.info(" [Rollback Error]: TimeSale-Service ");
        }
    }

    // 보상 트랜잭션 - 상품 재고
    private void rollbackProductStock(List<String> deductedProductStocks) {
        try {
            // 상품 재고 증감 요청
            deductedProductStocks.forEach(product -> {
                // TODO - kafka 를 이용한 비동기 처리
            });
        } catch (Exception e) {
            log.info(" [Rollback Error]: Product-Service ");
        }
    }

    // 보상 트랜잭션 - 쿠폰 상태
    private void rollbackCoupons(List<String> usedCoupons) {
        try {
            // 쿠폰 상태 변경 요청(USED -> ISSUED)
            usedCoupons.forEach(usedCouponId -> {
                // TODO - kafka 를 이용한 비동기 처리
            });
        } catch (Exception e) {
            log.info(" [Rollback Error]: Coupon-Service ");
        }
    }

    // 보상 트랜잭션 - 적립금
    private void rollbackRewardPoint(String userId) {
        try {
            // 적립금 증감 요청
            // TODO - kafka 를 이용한 비동기 처리

        } catch (Exception e) {
            log.info(" [Rollback Error]: User(Point)-Service ");
        }
    }

    // 주문 등록 - 보상 트랜잭션
    private void createOrderRollbackTransaction(List<String> deductedTimeSales, List<String> deductedProducts,
        List<String> usedCoupons) {

        rollbackTimeSaleStock(deductedTimeSales);
        rollbackProductStock(deductedProducts);
        rollbackCoupons(usedCoupons);
    }

    // 주문 상품 처리 로직
    public void processOrderItems(OrderCreateRequestDto requestDto, Order order, String userId) {
        // 보상 트랜잭션 관리 변수
        // TODO - 타입 클래스를 따로 만들어 줘야할 거 같음 - 사이즈, 갯수, 아이디
        List<String> deductedTimeSales = new ArrayList<>(); // 타임세일 재고 차감 목록
        List<String> deductedProducts = new ArrayList<>();  // 재고 차감 목록
        List<String> usedCoupons = new ArrayList<>();       // 사용된 쿠폰 목록
        // 주문 상품 목록 - set
        Set<OrderItem> orderItems = new HashSet<>();
        // 총 주문 금액 - set
        int orderPrice = 0;

        for (OrderItemCreateRequestDto itemCreateDto : requestDto.itemList()) {
            try {
//                // ID, 갯수, 사이즈 담겨있는 Dto 생성 후 사용
//                int quantity = itemCreateDto.quantity();
//                Object size = itemCreateDto;
//
//                // 1️⃣ 타임세일 제품 확인
//                String timeSaleProductId = itemCreateDto.timeSaleProductId();
//                if (timeSaleProductId != null) {
//                    // 타임세일 상품에 재고 존재? -> 요청 + 롤백처리 필요.
//                    timeSaleProductClient.checkAndDeductTimeSaleProduct(timeSaleProductId);
//                    deductedTimeSales.add(timeSaleProductId);
//                }
//                // 2️⃣ 재고 확인 및 차감
//                String productId = itemCreateDto.productId();
//
//                productClient.checkAndDeductStock(productId);   // 재고 확인 및 차감
//                deductedProducts.add(productId);                // 재고 차감 목록 추가
//
//                // 3️⃣ 쿠폰 사용 여부 체크
//                String couponIssuedId = itemCreateDto.couponIssuedId();
//                if (couponIssuedId != null) {
//                    // 아래 둘 중 하나에서 쿠폰 할인율 or 할인 금액을 가져와야함.
//                    couponIssueClient.getCouponIssue(couponIssuedId, userId).getBody();  // 쿠폰 조회
//                    couponIssueClient.useCouponIssue(couponIssuedId, userId).getBody();  // 쿠폰 사용
//                    usedCoupons.add(couponIssuedId);                                     // 사용된 쿠폰 목록 추가
//                }

                // 주문 상품 ID 생성 및 주문 상품 목록에 추가
                String orderItemId = orderIdCreator.create();
                OrderItem orderItem = orderItemMapper.toEntity(itemCreateDto, orderItemId, order);
                orderItems.add(orderItem);
                // 주문 금액 추가 - 할인율 or 할인금액 적용
                orderPrice += (orderItem.getUnitPrice() * orderItem.getQuantity());

            } catch (FeignException e) {
                // 보상 트랜잭션 - 비동기 처리 (Kafka 사용)
                createOrderRollbackTransaction(deductedTimeSales, deductedProducts, usedCoupons);
                throw new BusinessException(ExceptionMessage.ORDER_CREATION_FAILED);
            }
        }

        // 적립금 사용 시
        try {
            int point = requestDto.point();
//            if (point > 0) {
//                // 적립금 조회 및 차감 요청
//                userClient.checkAndUseRewardPoint(userId);
//                order.usePoint(point);
//            }

        } catch (FeignException e) {
            // 보상 트랜잭션 - 비동기 처리 (Kafka 사용)
            createOrderRollbackTransaction(deductedTimeSales, deductedProducts, usedCoupons);
            throw new BusinessException(ExceptionMessage.ORDER_CREATION_FAILED);
        }

        // 주문 - 금액 & 상품 목록 지정
        order.setOrderPriceAndItems(orderPrice, orderItems);
    }
}
