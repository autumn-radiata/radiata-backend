package radiata.service.timesale.core.implementation;

import java.time.LocalDateTime;
import radiata.common.annotation.Implementation;
import radiata.common.domain.timesale.dto.request.TimeSaleCreateRequestDto;
import radiata.common.exception.BusinessException;
import radiata.common.message.ExceptionMessage;
import radiata.service.timesale.core.implementation.interfaces.TimeSaleCreateRequestDtoValidator;

@Implementation
public class TimeSaleCreateRequestDtoValidatorImpl implements TimeSaleCreateRequestDtoValidator {

    @Override
    public void validate(TimeSaleCreateRequestDto requestDto) {
        LocalDateTime now = LocalDateTime.now();

        if (requestDto.timeSaleEndDate().isBefore(now)) {
            throw new BusinessException(ExceptionMessage.TIME_SALE_END_DATE_IS_BEFORE_NOW);
        }

        if (requestDto.timeSaleStartDate().isAfter(requestDto.timeSaleEndDate())) {
            throw new BusinessException(ExceptionMessage.TIME_SALE_START_DATE_IS_AFTER_END_DATE);
        }

        if (requestDto.timeSaleStartDate().isEqual(requestDto.timeSaleEndDate())) {
            throw new BusinessException(ExceptionMessage.TIME_SALE_START_DATE_IS_EQUALS_END_DATE);
        }
    }

}
