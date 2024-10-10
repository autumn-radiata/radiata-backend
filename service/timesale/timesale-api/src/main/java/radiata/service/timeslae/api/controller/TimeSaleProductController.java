package radiata.service.timeslae.api.controller;

import static radiata.common.message.SuccessMessage.CREATE_TIME_SALE_PRODUCT;
import static radiata.common.message.SuccessMessage.SALE_TIME_SALE_PRODUCT;
import static radiata.common.response.SuccessResponse.success;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import radiata.common.domain.timesale.dto.response.TimeSaleProductCreateRequestDto;
import radiata.common.response.CommonResponse;
import radiata.service.timeslae.api.service.TimeSaleProductApiService;

@RestController
@RequiredArgsConstructor
public class TimeSaleProductController {

    private final TimeSaleProductApiService timeSaleProductApiService;

    @PostMapping("/timesale-products")
    public ResponseEntity<? extends CommonResponse> createTimeSaleProduct(
        @Valid @RequestBody
        TimeSaleProductCreateRequestDto requestDto
    ) {

        return ResponseEntity.status(CREATE_TIME_SALE_PRODUCT.getHttpStatus())
            .body(success(CREATE_TIME_SALE_PRODUCT.getMessage(),
                timeSaleProductApiService.createTimeSaleProduct(requestDto)));
    }

    @PatchMapping("/timesale-products/{timeSaleProductId}")
    public ResponseEntity<? extends CommonResponse> saleTimeSaleProduct(
        @PathVariable
        String timeSaleProductId
    ) {

        timeSaleProductApiService.saleTimeSaleProduct(timeSaleProductId);

        return ResponseEntity.status(SALE_TIME_SALE_PRODUCT.getHttpStatus())
            .body(success(SALE_TIME_SALE_PRODUCT.getMessage()));
    }
}
