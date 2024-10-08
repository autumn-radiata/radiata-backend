package radiata.service.order.api.presentation;

import lombok.RequiredArgsConstructor;
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

        OrderResponseDto response = orderService.createOrder(requestDto, userId);

        return SuccessResponse.success("주문 접수 완료", response);
    }

    // 주문 상세 조회 - GET
    // 주문 목록 조회 - GET
    // 주문 취소 요청 - POST
    // 주문 환불 요청 - POST
    // 주문 상태 변경(결제 대기 중) - PATCH
    // 주문 상태 변경(결제 완료) - PATCH
    // 주문 상태 변경(배송 대기 중) - PATCH
    // 주문 상태 변경(배송 중) - PATCH
    // 주문 상태 변경(배송 완료) - PATCH
    // 주문 내역 삭제

}