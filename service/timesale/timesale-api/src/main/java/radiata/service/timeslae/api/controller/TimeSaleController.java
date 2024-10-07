package radiata.service.timeslae.api.controller;

import static org.springframework.data.domain.Sort.Direction.DESC;
import static radiata.common.message.SuccessMessage.CREATE_TIME_SALE;
import static radiata.common.message.SuccessMessage.GET_TIME_SALE;
import static radiata.common.message.SuccessMessage.GET_TIME_SALES;
import static radiata.common.response.SuccessResponse.success;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import radiata.common.domain.timesale.dto.condition.TimeSaleSearchCondition;
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

    @GetMapping("/timesales")
    public ResponseEntity<? extends CommonResponse> getTimeSales(
        @ModelAttribute
        TimeSaleSearchCondition condition,
        @PageableDefault(size = 10, sort = "createdAt", direction = DESC)
        Pageable pageable
    ) {

        return ResponseEntity.status(GET_TIME_SALES.getHttpStatus())
            .body(success(GET_TIME_SALES.getMessage(), timeSaleApiService.getTimeSales(condition, pageable)));
    }

    @GetMapping("/timesales/{timeSaleId}")
    public ResponseEntity<? extends CommonResponse> getTimeSale(
        @PathVariable
        String timeSaleId
    ) {

        return ResponseEntity.status(GET_TIME_SALE.getHttpStatus())
            .body(success(GET_TIME_SALE.getMessage(), timeSaleApiService.getTimeSale(timeSaleId)));
    }
}
