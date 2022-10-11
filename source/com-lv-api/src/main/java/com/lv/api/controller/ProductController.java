package com.lv.api.controller;

import com.lv.api.constant.Constants;
import com.lv.api.dto.ApiMessageDto;
import com.lv.api.dto.ErrorCode;
import com.lv.api.dto.ResponseListObj;
import com.lv.api.dto.product.ProductDto;
import com.lv.api.exception.RequestException;
import com.lv.api.form.product.CreateProductForm;
import com.lv.api.form.product.UpdateProductForm;
import com.lv.api.form.productvariant.UpdateProductVariantForm;
import com.lv.api.mapper.ProductConfigMapper;
import com.lv.api.mapper.ProductMapper;
import com.lv.api.mapper.ProductVariantMapper;
import com.lv.api.service.CommonApiService;
import com.lv.api.storage.criteria.ProductCriteria;
import com.lv.api.storage.model.Product;
import com.lv.api.storage.model.ProductCategory;
import com.lv.api.storage.model.ProductConfig;
import com.lv.api.storage.model.ProductVariant;
import com.lv.api.storage.repository.ProductCategoryRepository;
import com.lv.api.storage.repository.ProductConfigRepository;
import com.lv.api.storage.repository.ProductRepository;
import com.lv.api.storage.repository.ProductVariantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/product")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
@RequiredArgsConstructor
public class ProductController extends ABasicController {
    private final ProductRepository productRepository;
    private final ProductConfigRepository productConfigRepository;
    private final ProductVariantRepository productVariantRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductMapper productMapper;
    private final ProductConfigMapper productConfigMapper;
    private final ProductVariantMapper productVariantMapper;

    private final CommonApiService commonApiService;

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<ProductDto>> list(@Valid ProductCriteria productCriteria, BindingResult bindingResult, Pageable pageable) {
        Page<Product> productPage = productRepository.findAll(productCriteria.getSpecification(), pageable);
        List<ProductDto> productDtoList = productMapper.fromProductEntityListToDtoList(productPage.getContent());
        return new ApiMessageDto<>(
                new ResponseListObj<>(
                        productDtoList,
                        productPage
                ),
                "Get list product successfully"
        );
    }

    @GetMapping(value = "/auto-complete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<List<ProductDto>> autoComplete(@Valid ProductCriteria productCriteria) {
        Page<Product> productPage = productRepository.findAll(productCriteria.getSpecification(), Pageable.unpaged());
        List<ProductDto> productDtoList = productMapper.fromProductEntityListToDtoList(productPage.getContent());
        return new ApiMessageDto<>(productDtoList, "Get list successfully");
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ProductDto> get(@PathVariable(name = "id") Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RequestException(ErrorCode.PRODUCT_NOT_FOUND, "Product not found"));
        ProductDto productDto = productMapper.fromProductEntityToDtoDetails(product);
        return new ApiMessageDto<>(productDto, "Get product successfully");
    }

    @Transactional
    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> create(@Valid @RequestBody CreateProductForm createProductForm, BindingResult bindingResult) {
        Product product = productMapper.fromCreateProductFormToEntity(createProductForm);
        if (createProductForm.getCategoryId() != null) {
            ProductCategory category = productCategoryRepository.findById(createProductForm.getCategoryId())
                    .orElseThrow(() -> new RequestException(ErrorCode.PRODUCT_CATEGORY_ERROR_NOT_FOUND, "Product category not found"));
            product.setCategory(category);
        }
        if (product.getKind().equals(Constants.PRODUCT_KIND_GROUP) && product.getParentProduct() != null) {
            Product parentProduct = productRepository.findById(createProductForm.getProductParentId())
                    .orElseThrow(() -> new RequestException(ErrorCode.PRODUCT_NOT_FOUND, "Parent product not found"));
            product.setParentProduct(parentProduct);
        }
        productRepository.save(product);
        return new ApiMessageDto<>("Create product successfully");
    }

    @Transactional
    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> update(@Valid @RequestBody UpdateProductForm updateProductForm, BindingResult bindingResult) {
        Product product = productRepository.findById(updateProductForm.getId())
                .orElseThrow(() -> new RequestException(ErrorCode.PRODUCT_NOT_FOUND, "Product not found"));
        List<ProductConfig> productConfigs = product.getProductConfigs();
        updateProductForm.getProductConfigs().forEach(updateProductConfigForm -> {
            ProductConfig productConfig = productConfigs.stream()
                    .filter(productCfg -> productCfg.getId().equals(updateProductConfigForm.getId()))
                    .findFirst()
                    .orElseThrow(() -> new RequestException(ErrorCode.PRODUCT_CONFIG_NOT_FOUND, "Product config not found"));
            List<ProductVariant> variants =  productConfig.getVariants();
            updateProductConfigForm.getVariants().forEach(updateProductVariantForm -> {
                ProductVariant productVariant = variants.stream()
                        .filter(variant -> variant.getId().equals(updateProductVariantForm.getId()))
                        .findFirst()
                        .orElseThrow(() -> new RequestException(ErrorCode.PRODUCT_VARIANT_NOT_FOUND, "Product variant not found"));
                productVariantMapper.fromUpdateProductVariantFormToEntity(updateProductVariantForm, productVariant);
                productVariantRepository.save(productVariant);
                variants.remove(productVariant);
            });
            productConfigMapper.frommUpdateProductConfigFormToEntity(updateProductConfigForm, productConfig);
            productConfig.setVariants(variants);
            productConfigRepository.save(productConfig);
            productConfigs.remove(productConfig);
            productVariantRepository.deleteAll(variants);
        });
        productMapper.fromUpdateProductFormToEntity(updateProductForm, product);
        productConfigRepository.deleteAll(productConfigs);

        if (updateProductForm.getCategoryId() != null) {
            ProductCategory category = productCategoryRepository.findById(updateProductForm.getCategoryId())
                    .orElseThrow(() -> new RequestException(ErrorCode.PRODUCT_CATEGORY_ERROR_NOT_FOUND, "Product category not found"));
            product.setCategory(category);
        } else {
            product.setCategory(null);
        }

        if (!product.getKind().equals(updateProductForm.getKind())) {
            if (updateProductForm.getKind().equals(Constants.PRODUCT_KIND_GROUP)) {
                Product parentProduct = productRepository.findById(updateProductForm.getProductParentId())
                        .orElseThrow(() -> new RequestException(ErrorCode.PRODUCT_NOT_FOUND, "Parent product not found"));
                product.setParentProduct(parentProduct);
            } else {
                product.setParentProduct(null);
            }
        }
        productRepository.save(product);
        return new ApiMessageDto<>("Update product successfully");
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> delete(@PathVariable(name = "id") Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RequestException(ErrorCode.PRODUCT_NOT_FOUND, "Product not found"));

        productRepository.delete(product);
        return new ApiMessageDto<>("Delete product successfully");
    }
}
