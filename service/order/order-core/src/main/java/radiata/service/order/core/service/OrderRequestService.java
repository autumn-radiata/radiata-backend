package radiata.service.order.core.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import radiata.common.domain.order.dto.request.OrderEasyPaymentRequestDto;
import radiata.common.domain.order.dto.request.OrderTossPaymentRequestDto;
import radiata.common.domain.order.dto.response.OrderResponseDto;
import radiata.service.order.core.domain.model.constant.OrderStatus;
import radiata.service.order.core.domain.model.entity.Order;
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
    private final ProcessService processService;


    // 간편 결제 요청
    @Transactional
    public OrderResponseDto sendEasyPaymentRequest(String orderId, String userId,
        OrderEasyPaymentRequestDto requestDto) {
        // 주문 조회
        Order order = orderReader.readOrder(orderId);
        // 유저의 주문인지 체크
        orderValidator.validateUserOwnsOrder(order.getUserId(), userId);
        // 주문 상태 체크 & 변경(결제 대기 중)
        orderValidator.checkCanMoveToNextStatus(order, OrderStatus.PAYMENT_PENDING);
        // 주문 금액 일치 확인
        orderValidator.IsEqualsOrderPrice(requestDto.amount(), order.getOrderPrice());
        // 간편 결제 요청
        processService.requestEasyPayment(requestDto, order, userId);
        // 주문 상태 체크 & 변경(결제 완료)
        orderValidator.checkCanMoveToNextStatus(order, OrderStatus.PAYMENT_COMPLETED);
        // 반환
        return orderMapper.toDto(order).withItemList(orderItemService.toDtoSet(order.getOrderItems()));
    }


    // 토스 결제 요청
    @Transactional
    public OrderResponseDto sendTossPaymentRequest(String orderId, String userId,
        OrderTossPaymentRequestDto requestDto) {
        // 주문 조회
        Order order = orderReader.readOrder(orderId);
        // 유저의 주문인지 체크
        orderValidator.validateUserOwnsOrder(order.getUserId(), userId);
        // 주문 상태 체크 & 변경(결제 대기 중)
        orderValidator.checkCanMoveToNextStatus(order, OrderStatus.PAYMENT_PENDING);
        // 주문 금액 일치 확인
        orderValidator.IsEqualsOrderPrice(requestDto.amount(), order.getOrderPrice());
        // 토스 페이 결제요청
        processService.requestTossPayment(requestDto, order, userId);
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
        // 결제 단계 상태만 취소 가능
        orderValidator.checkStatusIsPaymentLevel(order.getStatus());
        // 주문 취소 - 롤백 처리
        processService.processCancelOrder(order);
        // 주문 상태 체크
        orderValidator.checkStatusIsPaymentCancelRequested(order.getStatus());
        // 주문 상태 업데이트 - 주문 취소 완료
        order.updateOrderStatus(OrderStatus.PAYMENT_CANCEL_COMPLETED);
        // 반환
        return orderMapper.toDto(order).withItemList(orderItemService.toDtoSet(order.getOrderItems()));
    }
}
