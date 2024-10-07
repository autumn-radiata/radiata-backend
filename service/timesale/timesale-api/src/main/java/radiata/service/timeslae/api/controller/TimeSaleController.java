package radiata.service.timeslae.api.controller;

import static radiata.common.message.SuccessMessage.CREATE_TIME_SALE;
import static radiata.common.response.SuccessResponse.success;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import radiata.common.domain.timesale.dto.request.TimeSaleCreateRequestDto;
import radiata.common.response.CommonResponse;
import radiata.service.timeslae.api.service.TimeSaleApiService;

@RestController
@RequiredArgsConstructor
public class TimeSaleController {

    private final TimeSaleApiService timeSaleApiService;


    @PostMapping("/timesales")
    public ResponseEntity<? extends CommonResponse> createTimeSale(
        @Valid @RequestBody
        TimeSaleCreateRequestDto requestDto
    ) {

        return ResponseEntity.status(CREATE_TIME_SALE.getHttpStatus())
            .body(success(CREATE_TIME_SALE.getMessage(), timeSaleApiService.createTimeSale(requestDto)));
    }

    @GetMapping("/test")
    public String test(
    ) {

        return "test";
    }
}
