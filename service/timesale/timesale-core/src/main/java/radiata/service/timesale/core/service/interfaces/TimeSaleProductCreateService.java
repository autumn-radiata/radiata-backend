package radiata.service.timesale.core.service.interfaces;

import radiata.common.domain.timesale.dto.response.TimeSaleProductCreateRequestDto;

public interface TimeSaleProductCreateService {

    void createTimeSaleProduct(TimeSaleProductCreateRequestDto timeSaleProduct);

}
