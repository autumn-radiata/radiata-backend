package radiata.service.timesale.core.service;

import java.util.Comparator;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import radiata.common.domain.timesale.dto.response.TimeSaleProductCreateRequestDto;
import radiata.common.domain.timesale.dto.response.TimeSaleProductResponseDto;
import radiata.common.exception.BusinessException;
import radiata.common.message.ExceptionMessage;
import radiata.service.timesale.core.domain.TimeSale;
import radiata.service.timesale.core.domain.TimeSaleProduct;
import radiata.service.timesale.core.implementation.interfaces.TimeSaleProductIdCreator;
import radiata.service.timesale.core.implementation.interfaces.TimeSaleProductReader;
import radiata.service.timesale.core.implementation.interfaces.TimeSaleReader;
import radiata.service.timesale.core.infrastructure.repository.TimeSaleProductResponseRedisRepository;
import radiata.service.timesale.core.service.interfaces.TimeSaleProductService;
import radiata.service.timesale.core.service.mapper.TimeSaleProductMapper;

@Service
@Transactional
@RequiredArgsConstructor
public class TimeSaleProductServiceImpl implements TimeSaleProductService {

    private final TimeSaleReader timeSaleReader;
    private final TimeSaleProductIdCreator timeSaleProductIdCreator;
    private final TimeSaleProductMapper timeSaleProductMapper;
    private final TimeSaleProductReader timeSaleProductReader;
    private final TimeSaleProductResponseRedisRepository timeSaleProductResponseRedisRepository;

    @Override
    @CacheEvict(value = "cacheProductTimeSales", key = "#request.productId()")
    public TimeSaleProductResponseDto createTimeSaleProduct(
            TimeSaleProductCreateRequestDto request) {

        TimeSale timeSale = timeSaleReader.read(request.timeSaleId());

        // TODO ProductId 검증 필요

        String id = timeSaleProductIdCreator.create();
        TimeSaleProduct timeSaleProduct = timeSaleProductMapper.toEntity(request, id);
        timeSale.addTimeSaleProduct(timeSaleProduct);

        return timeSaleProductMapper.toDto(timeSaleProduct);
    }

    @Override
    public void sale(String timeSaleProductId) {

        TimeSaleProduct timeSaleProduct = timeSaleProductReader.read(timeSaleProductId);
        timeSaleProduct.sale();

        timeSaleProductResponseRedisRepository.delete(timeSaleProduct.getProductId());
    }

    @Override
    public TimeSaleProductResponseDto getMaxDiscountTimeSaleProduct(String productId) {

        return timeSaleProductMapper.toDto(
                timeSaleReader.readByProductId(productId)
                        .getTimeSaleProducts().stream()
                        .sorted(Comparator.comparing(TimeSaleProduct::getId))
                        .max(Comparator.comparing(TimeSaleProduct::getDiscountRate)).orElseThrow(
                                () -> new BusinessException(ExceptionMessage.NOT_FOUND)
                        ));
    }

    @Override
    @Cacheable(value = "cacheProductTimeSales", key = "#productId")
    public TimeSaleProductResponseDto getMaxDiscountTimeSaleProductHasStock(String productId) {

        TimeSaleProductResponseDto responseDto = timeSaleProductMapper.toDto(
                timeSaleReader.readByProductId(productId)
                        .getTimeSaleProducts().stream()
                        .filter(a -> a.getSaleQuantity() < a.getTotalQuantity())
                        .sorted(Comparator.comparing(TimeSaleProduct::getId))
                        .max(Comparator.comparing(TimeSaleProduct::getDiscountRate)).orElseThrow(
                                () -> new BusinessException(ExceptionMessage.NOT_FOUND)
                        ));

        return responseDto;
    }
}
