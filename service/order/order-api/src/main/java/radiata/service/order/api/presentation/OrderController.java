package radiata.service.order.api.presentation;

import static radiata.common.message.SuccessMessage.CANCEL_ORDER;
import static radiata.common.message.SuccessMessage.COMPLETE_ORDER_PAYMENT;
import static radiata.common.message.SuccessMessage.CREATE_ORDER;
import static radiata.common.message.SuccessMessage.GET_ORDER;
import static radiata.common.message.SuccessMessage.UPDATE_ORDER;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import radiata.common.domain.order.dto.request.OrderCreateRequestDto;
import radiata.common.domain.order.dto.request.OrderEasyPaymentRequestDto;
import radiata.common.domain.order.dto.request.OrderTossPaymentRequestDto;
import radiata.common.domain.order.dto.response.OrderResponseDto;
import radiata.common.response.SuccessResponse;
import radiata.service.order.core.service.OrderCreateService;
import radiata.service.order.core.service.OrderReadService;
import radiata.service.order.core.service.OrderRequestService;
import radiata.service.order.core.service.OrderUpdateService;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderCreateService orderCreateService;
    private final OrderRequestService orderRequestService;
    private final OrderReadService orderReadService;
    private final OrderUpdateService orderUpdateService;


    // 주문 등록 - POST
    @PostMapping
    public SuccessResponse<OrderResponseDto> createOrder(@Valid @RequestBody OrderCreateRequestDto requestDto,
        @RequestHeader("X-UserId") String userId) {

        return SuccessResponse.success(CREATE_ORDER.getMessage(), orderCreateService.createOrder(requestDto, userId));
    }

    // 주문 상세 조회 - GET
    @GetMapping("/{orderId}")
    public SuccessResponse<OrderResponseDto> getOrder(@PathVariable("orderId") String orderId,
        @RequestHeader("X-UserId") String userId) {

        return SuccessResponse.success(GET_ORDER.getMessage(), orderReadService.getOrder(orderId, userId));
    }

    // 주문 목록 조회 - GET
    // 주문 취소 요청 - POST
    @PostMapping("/{orderId}/canceled")
    public SuccessResponse<OrderResponseDto> cancelOrder(@PathVariable("orderId") String orderId,
        @RequestHeader("X-UserId") String userId) {

        return SuccessResponse.success(CANCEL_ORDER.getMessage(), orderRequestService.cancelOrder(orderId, userId));
    }

    // 주문 환불 요청 - POST("/{orderId}/refunded")

    // 주문 결제 승인 요청 및 완료 - 간편결제
    @PostMapping("/{orderId}/payment-requested/easypay")
    public SuccessResponse<OrderResponseDto> requestCompleteEasyPayment(@PathVariable("orderId") String orderId,
        @RequestHeader("X-UserId") String userId,
        @RequestBody OrderEasyPaymentRequestDto requestDto) {

        return SuccessResponse.success(COMPLETE_ORDER_PAYMENT.getMessage(),
            orderRequestService.sendEasyPaymentRequest(orderId, userId, requestDto));
    }


    // 주문 결제 승인 요청 및 완료 - 토스
    @PostMapping("/{orderId}/payment-requested/toss")
    public SuccessResponse<OrderResponseDto> requestCompleteTossPayment(@PathVariable("orderId") String orderId,
        @RequestHeader("X-UserId") String userId,
        @RequestBody OrderTossPaymentRequestDto requestDto) {

        return SuccessResponse.success(COMPLETE_ORDER_PAYMENT.getMessage(),
            orderRequestService.sendTossPaymentRequest(orderId, userId, requestDto));
    }

    // 주문 상태 변경(배송 대기 중) - PATCH
    @PatchMapping("/{orderId}/shipping-pending")
    public SuccessResponse<OrderResponseDto> pendShipping(@PathVariable("orderId") String orderId) {

        return SuccessResponse.success(UPDATE_ORDER.getMessage(),
            orderUpdateService.updateStatusPendingShipping(orderId));
    }

    // 주문 상태 변경(배송 중) - PATCH
    @PatchMapping("/{orderId}/shipping-in-progress")
    public SuccessResponse<OrderResponseDto> shippingInProgress(@PathVariable("orderId") String orderId) {

        return SuccessResponse.success(UPDATE_ORDER.getMessage(),
            orderUpdateService.updateStatusShipping(orderId));
    }

    // 주문 상태 변경(배송 완료) - PATCH
    @PatchMapping("/{orderId}/shipping-completed")
    public SuccessResponse<OrderResponseDto> completeShipping(@PathVariable("orderId") String orderId) {

        return SuccessResponse.success(UPDATE_ORDER.getMessage(),
            orderUpdateService.updateStatusCompletedShipping(orderId));
    }
    // 주문 내역 삭제

}