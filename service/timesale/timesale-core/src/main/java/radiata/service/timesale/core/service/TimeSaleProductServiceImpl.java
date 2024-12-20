package radiata.service.timesale.core.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import radiata.common.domain.timesale.dto.request.TimeSaleProductSaleRequestDto;
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
    public TimeSaleProductResponseDto sale(String timeSaleProductId, TimeSaleProductSaleRequestDto requestDto) {

        TimeSaleProduct timeSaleProduct = timeSaleProductReader.read(timeSaleProductId);
        timeSaleProduct.sale(requestDto.quantity());

        timeSaleProductResponseRedisRepository.delete(timeSaleProduct.getProductId());

        return timeSaleProductMapper.toDto(timeSaleProduct);
    }

    @Override
    public void decrementSaleQuantity(String timeSaleProductId, Integer quantity) {

        TimeSaleProduct timeSaleProduct = timeSaleProductReader.read(timeSaleProductId);
        timeSaleProduct.decrementSaleQuantity(quantity);
    }

    @Override
    public List<TimeSaleProductResponseDto> getMaxDiscountTimeSaleProduct(List<String> productIds) {

        if (productIds.size() > 100) {
            throw new BusinessException(ExceptionMessage.REQUEST_LIST_SIZE_LIMIT);
        }

        List<TimeSaleProductResponseDto> allTimeSaleProducts = timeSaleReader.readByProductIds(productIds).stream()
            .flatMap(timeSale -> timeSale.getTimeSaleProducts().stream())
            .map(timeSaleProductMapper::toDto)
            .toList();

        Map<String, TimeSaleProductResponseDto> maxDiscountProducts = allTimeSaleProducts.stream()
            .collect(Collectors.toMap(
                TimeSaleProductResponseDto::productId,
                Function.identity(),
                (existing, replacement) -> replacement.discountRate() > existing.discountRate() ? replacement : existing
            ));

        return productIds.stream()
            .filter(maxDiscountProducts::containsKey)
            .map(maxDiscountProducts::get)
            .toList();
    }

    @Override
    public List<TimeSaleProductResponseDto> getMaxDiscountTimeSaleProductHasStock(List<String> productIds) {

        if (productIds.size() > 100) {
            throw new BusinessException(ExceptionMessage.REQUEST_LIST_SIZE_LIMIT);
        }

        List<TimeSaleProductResponseDto> allTimeSaleProducts = timeSaleReader.readByProductIds(productIds).stream()
            .flatMap(timeSale -> timeSale.getTimeSaleProducts().stream())
            .map(timeSaleProductMapper::toDto)
            .toList();

        List<TimeSaleProductResponseDto> availableStockProducts = allTimeSaleProducts.stream()
            .filter(product -> product.saleQuantity() < product.totalQuantity())
            .toList();

        Map<String, TimeSaleProductResponseDto> maxDiscountProducts = availableStockProducts.stream()
            .collect(Collectors.toMap(
                TimeSaleProductResponseDto::productId,
                Function.identity(),
                (existing, replacement) -> replacement.discountRate() > existing.discountRate() ? replacement : existing
            ));

        return new ArrayList<>(maxDiscountProducts.values());
    }
}
