package radiata.service.order.api.presentation;

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
import radiata.common.domain.order.dto.response.OrderResponseDto;
import radiata.common.response.SuccessResponse;
import radiata.service.order.core.service.OrderService;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // TODO - 타입 수정 가능성O & api-service 구현?

    // 주문 등록 - POST
    @PostMapping
    public SuccessResponse<OrderResponseDto> createOrder(@RequestBody OrderCreateRequestDto requestDto,
        @RequestHeader("X-UserId") String userId) {

        return SuccessResponse.success("주문 접수 완료", orderService.createOrder(requestDto, userId));
    }

    // 주문 상세 조회 - GET
    @GetMapping("/{orderId}")
    public SuccessResponse<OrderResponseDto> getOrder(@PathVariable("orderId") String orderId,
        @RequestHeader("X-UserId") String userId) {

        return SuccessResponse.success("주문 상세 조회 완료", orderService.getOrder(orderId, userId));
    }

    // 주문 목록 조회 - GET
    // 주문 취소 요청 - POST ("/{orderId}/canceled")
    // 주문 환불 요청 - POST("/{orderId}/refunded")
    // 주문 상태 변경(결제 대기 중)
    @PatchMapping("/{orderId}/payment-pending")
    public SuccessResponse<OrderResponseDto> pendingPayment(@PathVariable("orderId") String orderId) {

        return SuccessResponse.success(" 주문 상태: [결제 대기 중] ",
            orderService.updateStatusPendingPayment(orderId));
    }

    // 주문 상태 변경(결제 완료) - PATCH
    @PatchMapping("/{orderId}/payment-completed")
    public SuccessResponse<OrderResponseDto> completePayment(@PathVariable("orderId") String orderId,
        String paymentId) {

        return SuccessResponse.success(" 주문 상태: [결제 완료] ",
            orderService.updateStatusCompletedPayment(orderId, paymentId));
    }
    // 주문 상태 변경(배송 대기 중) - PATCH("/{orderId}/shipping-pending")
    // 주문 상태 변경(배송 중) - PATCH("/{orderId}/shipping-in-progress")
    // 주문 상태 변경(배송 완료) - PATCH("/{orderId}/shipping-completed")
    // 주문 내역 삭제

}