package radiata.service.order.core.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import radiata.common.domain.order.dto.request.OrderCreateRequestDto;
import radiata.common.domain.order.dto.response.OrderResponseDto;
import radiata.service.order.core.domain.model.constant.OrderStatus;
import radiata.service.order.core.domain.model.entity.Order;
import radiata.service.order.core.implemetation.OrderIdCreator;
import radiata.service.order.core.implemetation.OrderReader;
import radiata.service.order.core.implemetation.OrderSaver;
import radiata.service.order.core.service.mapper.OrderMapper;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderIdCreator orderIdCreator;
    private final OrderSaver orderSaver;
    private final OrderMapper orderMapper;
    private final OrderReader orderReader;

    // 주문 생성
    @Transactional
    public OrderResponseDto createOrder(OrderCreateRequestDto requestDto, String userId) {
        // 주문 ID 생성
        String orderId = orderIdCreator.createOrderId();
        // 초기 주문 생성
        Order order = orderSaver.save(orderMapper.toEntity(requestDto, orderId, userId));

        // 주문상품 목록 확인 - itemList
        // 재고 확인 및 차감
        // 1) 성공 - 다음
        // 2) 실패 - 재고 차감 -> 증감 요청

        // 쿠폰 사용 가능여부 확인
        // 쿠폰 상태 값 변경 시도
        // 1) 성공 - 쿠폰 사용(상태 값 ISSUED -> USED 변경) -> 다음
        // 2) 실패 - 재고 차감 -> 증감 요청

        // 적립금 사용 가능여부 확인
        // 적립금 차감 시도
        // 1) 성공 - 적립금 차감 -> 다음
        // 2) 실패 - 쿠폰 상태 USED -> ISSUED 로 요청 & 재고 차감 -> 증감 요청
        // 주문에 상품목록 지정 - setOrderItems
        // 결제 금액 지정 - setOrderPrice
        // 저장 - 주문 등록 완료

        // 반환
        return orderMapper.toDto(order);
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
