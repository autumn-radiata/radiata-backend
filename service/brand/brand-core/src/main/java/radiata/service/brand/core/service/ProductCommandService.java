package radiata.service.brand.core.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import radiata.common.domain.brand.request.ProductCreateRequestDto;
import radiata.common.domain.brand.request.ProductDeductRequestDto;
import radiata.common.domain.brand.request.ProductModifyRequestDto;
import radiata.common.domain.brand.response.ProductGetResponseDto;
import radiata.service.brand.core.implement.BrandReader;
import radiata.service.brand.core.implement.CategoryReader;
import radiata.service.brand.core.implement.IdCreator;
import radiata.service.brand.core.implement.ProductReader;
import radiata.service.brand.core.implement.ProductSaver;
import radiata.service.brand.core.implement.ProductValidator;
import radiata.service.brand.core.implement.RedisService;
import radiata.service.brand.core.model.constant.ColorType;
import radiata.service.brand.core.model.constant.GenderType;
import radiata.service.brand.core.model.constant.SizeType;
import radiata.service.brand.core.model.entity.Brand;
import radiata.service.brand.core.model.entity.Category;
import radiata.service.brand.core.model.entity.Product;
import radiata.service.brand.core.service.Mapper.ProductMapper;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductCommandService {

    private final ProductValidator productValidator;
    private final IdCreator productIdCreator;
    private final ProductSaver productSaver;
    private final ProductReader productReader;
    private final BrandReader brandReader;
    private final CategoryReader categoryReader;
    private final ProductMapper productMapper;
    private final RedisService redisService;

    //todo : writeAround전략으로 saveAndFlush 로 변경

    /**
     * 상품 생성
     */
    @Transactional
    public ProductGetResponseDto createProduct(ProductCreateRequestDto dto) {
        productValidator.checkPriceAboveDiscountedAmount(dto.price(), dto.discountAmount());
        Brand brand = brandReader.read(dto.brandId());
        Category category = categoryReader.read(dto.categoryId());
        String id = productIdCreator.create();

        Product product = Product.of(id, brand, category, dto.name(), dto.price(), dto.discountAmount(),
            dto.stock(), GenderType.valueOf(dto.gender()), ColorType.valueOf(dto.color()),
            SizeType.valueOf(dto.size()));
        productSaver.save(product);
        return productMapper.toProductGetResponseDto(product);
    }

    /**
     * 상품 재고 차감
     */
    // todo : 분산락 추가
    @Transactional
    public void deductInventory(ProductDeductRequestDto dto) {

        //db 갱신
        Product product = productReader.read(dto.productId());
        product.subStock(dto.quantity());
        productSaver.save(product);

        //redis 갱신
        ProductGetResponseDto stockSubbedProduct = productMapper.toProductGetResponseDto(product);
        redisService.putProductDto(stockSubbedProduct);
        //redisService.evictPagedProductsCache();

    }

    /**
     * 상품 재고 증감 - 보상 트랜잭션 복구
     */

    @Transactional
    @KafkaListener(topics = "product.add-stock", containerFactory = "KafkaListenerContainerFactory")
    public void increaseStock(ConsumerRecord<String, String> consumerRecord) {
        ProductDeductRequestDto stockAddRequestDto = EventSerializer.deserialize(consumerRecord.value(),
            ProductDeductRequestDto.class);
        Product product = productReader.read(stockAddRequestDto.productId());
        product.addStock(stockAddRequestDto.quantity());
        productSaver.save(product);

        ProductGetResponseDto stockAddedProduct = productMapper.toProductGetResponseDto(product);
        redisService.putProductDto(stockAddedProduct);
        //redisService.evictPagedProductsCache();

    }

    /**
     * 상품 삭제
     */
    @Transactional
    public void removeProduct(String productId) {

        Product product = productReader.read(productId);
        product.delete();

        redisService.evictProduct(productId);

        //redisService.evictPagedProductsCache();

    }

    /**
     * 상품 수정
     */
    @Transactional
    public void modifyProduct(ProductModifyRequestDto dto) {
        productValidator.checkPriceAboveDiscountedAmount(dto.price(), dto.discountAmount());
        Product product = productReader.read(dto.productId());
        Brand brand = brandReader.read(dto.brandId());
        Category category = categoryReader.read(dto.categoryId());

        product.updateInfo(category, brand, dto.name(), dto.price(), dto.discountAmount(),
            dto.stock(), GenderType.valueOf(dto.gender()), ColorType.valueOf(dto.color()),
            SizeType.valueOf(dto.size()));
        ProductGetResponseDto productGetResponseDto = productMapper.toProductGetResponseDto(product);

        productSaver.save(product);
        redisService.putProductDto(productGetResponseDto);
    }


}


