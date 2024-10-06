package radiata.service.order.core.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import radiata.common.domain.order.dto.request.OrderCreateRequestDto;
import radiata.common.domain.order.dto.request.OrderItemCreateRequestDto;
import radiata.common.domain.order.dto.response.OrderItemResponseDto;
import radiata.common.domain.order.dto.response.OrderResponseDto;
import radiata.service.order.core.domain.model.constant.OrderStatus;
import radiata.service.order.core.domain.model.entity.Order;
import radiata.service.order.core.domain.model.entity.OrderItem;
import radiata.service.order.core.implemetation.OrderIdCreator;
import radiata.service.order.core.implemetation.OrderReader;
import radiata.service.order.core.implemetation.OrderSaver;
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


    // 주문 생성
    @Transactional
    public OrderResponseDto createOrder(OrderCreateRequestDto requestDto, String userId) {
        // 주문 ID 생성
        String orderId = orderIdCreator.create();
        // 초기 주문 생성
        Order order = orderSaver.save(orderMapper.toEntity(requestDto, orderId, userId));

        // 주문할 상품 목록 - 설정을 위한
        Set<OrderItem> orderItems = new HashSet<>();
        // 총 주문 금액 - 설정을 위한
        AtomicInteger orderPrice = new AtomicInteger(0); // AtomicInteger로 초기화

        // 비동기 작업 리스트
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (OrderItemCreateRequestDto itemCreateDto : requestDto.itemList()) {
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                // TODO - FeignClient 사용(Product, CouponIssue, Point)
                /* 1️⃣ 재고 확인 및 차감
                    1) 성공 - 다음

                    2) 실패 - 재고 차감 -> 증감 요청
                */

                /* 2️⃣ 쿠폰 사용 여부 체크
                    1) 미사용 - Null
                    👉 다음 단계

                    2) 사용 - NotNull
                    👉 쿠폰 상태 값 변경 시도
                        1) 성공 - 쿠폰 사용(상태 값 ISSUED -> USED 변경) -> 다음
                        2) 실패 - 재고 차감 -> 증감 요청(보상1)
                 */

                /* 3️⃣ 적립금 사용 여부 체크
                    1) 미사용 - Null
                    👉 다음 단계

                    2) 사용 - NotNull
                    👉 적립금 차감 시도
                        1) 성공 - 적립금 차감 -> 다음
                        2) 실패 -
                            쿠폰 상태 USED -> ISSUED 로 요청(보상2)
                            재고 차감 -> 증감 요청(보상1)
                */

                // 주문 상품 ID 생성
                String orderItemId = orderIdCreator.create();
                // 주문 상품 객체 생성
                OrderItem orderItem = orderItemMapper.toEntity(itemCreateDto, orderItemId, order);
                // 주문 상품 목록에 추가
                synchronized (orderItems) { // Concurrent Modification 방지
                    orderItems.add(orderItem);
                }
                // TODO - 쿠폰 할인율, 적립금 정해지면 적용시켜야 됨
                // 주문 금액 추가
                orderPrice.addAndGet(orderItem.getQuantity() * orderItem.getUnitPrice()); // 안전하게 업데이트
            });
            futures.add(future);
        }

        // 모든 비동기 작업 완료 대기
        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        // 모든 작업이 완료될 때까지 기다림
        allOf.join();

        // 주문에 상품목록 지정 - setOrderItems
        order.setOrderItems(orderItems);
        // 결제 금액 지정 - setOrderPrice
        order.setOrderPrice(orderPrice.get()); // AtomicInteger에서 값 가져오기

        // OrderResponseDto에 변환하여 itemList 추가
        Set<OrderItemResponseDto> orderItemsResponseDto = orderItems.stream()
            .map(orderItemMapper::toDto)
            .collect(Collectors.toSet());

        // 반환
        return orderMapper.toDto(order).withItemList(orderItemsResponseDto);
    }


    // TODO 결제(대기중, 완료), 배송(중, 대기중, 완료) - 사용 예정
    // 주문 상태 변경
    @Transactional
    public OrderResponseDto updateOrderStatus(OrderStatus requestStatus, String orderId) {
        // 주문 조회
        Order order = orderReader.readOrder(orderId);
        // 주문 상태 변경
        order.updateOrderStatus(requestStatus);
        // 반환
        return orderMapper.toDto(order);
    }

    @Transactional(readOnly = true)
    public OrderResponseDto getOrder(String orderId, String userId) {
        // 주문 조회
        Order order = orderReader.readOrder(orderId);
        // 사용자의 주문 내역인지 확인

        return orderMapper.toDto(order);
    }
}
