package radiata.service.timesale.core.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import radiata.common.domain.timesale.dto.response.TimeSaleProductCreateRequestDto;
import radiata.service.timesale.core.domain.TimeSale;
import radiata.service.timesale.core.domain.TimeSaleProduct;
import radiata.service.timesale.core.implementation.interfaces.TimeSaleProductIdCreator;
import radiata.service.timesale.core.implementation.interfaces.TimeSaleProductSaver;
import radiata.service.timesale.core.implementation.interfaces.TimeSaleReader;
import radiata.service.timesale.core.service.interfaces.TimeSaleProductService;
import radiata.service.timesale.core.service.mapper.TimeSaleProductMapper;

@Service
@RequiredArgsConstructor
public class TimeSaleProductServiceImpl implements TimeSaleProductService {

    private final TimeSaleReader timeSaleReader;
    private final TimeSaleProductIdCreator timeSaleProductIdCreator;
    private final TimeSaleProductMapper timeSaleProductMapper;
    private final TimeSaleProductSaver timeSaleProductSaver;

    @Override
    public void createTimeSaleProduct(TimeSaleProductCreateRequestDto request) {

        TimeSale timeSale = timeSaleReader.read(request.timeSaleId());

        // TODO ProductId 검증 필요

        String id = timeSaleProductIdCreator.create();
        TimeSaleProduct timeSaleProduct = timeSaleProductMapper.toEntity(request, id);

        timeSale.addTimeSaleProduct(timeSaleProduct);

        timeSaleProductSaver.save(timeSaleProduct);
    }
}
