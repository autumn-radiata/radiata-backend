package radiata.service.timesale.core.implementation.interfaces;

import radiata.common.domain.timesale.dto.request.TimeSaleCreateRequestDto;

public interface TimeSaleCreateRequestDtoValidator {

    void validate(TimeSaleCreateRequestDto dto);

}
