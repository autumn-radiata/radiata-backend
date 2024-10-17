package radiata.service.brand.api.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import radiata.common.domain.brand.request.ProductCreateRequestDto;
import radiata.common.domain.brand.request.ProductDeductRequestDto;
import radiata.common.domain.brand.request.ProductSearchCondition;
import radiata.common.domain.brand.response.ProductGetResponseDto;
import radiata.common.message.SuccessMessage;
import radiata.common.response.CommonResponse;
import radiata.common.response.SuccessResponse;
import radiata.service.brand.core.service.ProductCommandService;
import radiata.service.brand.core.service.ProductQueryService;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductCommandService productCommandService;
    private final ProductQueryService productQueryService;

    /**
     * 상품 생성
     */
    @PostMapping
    public CommonResponse createProduct(@Valid @RequestBody ProductCreateRequestDto dto) {
        ProductGetResponseDto response = productCommandService.createProduct(dto);
        return SuccessResponse.success(SuccessMessage.OK.getMessage(), response);
    }

    /**
     * 단일 상품 조회
     */
    @GetMapping("/{productId}")
    public CommonResponse getProduct(@PathVariable("productId") String productId) {
        ProductGetResponseDto response = productQueryService.getProduct(productId);
        return SuccessResponse.success(SuccessMessage.OK.getMessage(), response);
    }

    /**
     * 상품 목록 조회
     */
    /*@Cacheable(
        cacheNames = "itemSearchCache",
        key = "{ args[0], args[1].pageNumber, args[1].pageSize }"
    )*/
    @GetMapping("/list")
    public CommonResponse getProducts(
        @PageableDefault(page = 1, size = 10, sort = "createdAt", direction = Direction.ASC) Pageable pageable,
        @ModelAttribute ProductSearchCondition condition
    ) {
        Page<ProductGetResponseDto> response = productQueryService.getProducts(condition, pageable);
        return SuccessResponse.success(SuccessMessage.OK.getMessage(), response);
    }

    /**
     * 상품 수정
     */

    /**
     * 상품 삭제
     */
    @DeleteMapping("/{productId}")
    public CommonResponse deleteProduct(@PathVariable String productId) {
        productCommandService.removeProduct(productId);
        return SuccessResponse.success(SuccessMessage.OK.getMessage());
    }

    /**
     * 상품 재고 차감
     */
    @PatchMapping("/deduct")
    public CommonResponse deductProduct(@RequestBody ProductDeductRequestDto request) {
        productCommandService.deductInventory(request);
        return SuccessResponse.success(SuccessMessage.OK.getMessage());
    }


}
