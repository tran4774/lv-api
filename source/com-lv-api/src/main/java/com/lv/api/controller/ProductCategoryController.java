package com.lv.api.controller;

import com.lv.api.dto.ApiMessageDto;
import com.lv.api.dto.ErrorCode;
import com.lv.api.dto.ResponseListObj;
import com.lv.api.dto.productcategory.ProductCategoryDto;
import com.lv.api.exception.RequestException;
import com.lv.api.form.productcategory.CreateProductCategoryForm;
import com.lv.api.form.productcategory.UpdateProductCategoryForm;
import com.lv.api.mapper.ProductCategoryMapper;
import com.lv.api.service.CommonApiService;
import com.lv.api.storage.criteria.ProductCategoryCriteria;
import com.lv.api.storage.model.ProductCategory;
import com.lv.api.storage.repository.ProductCategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/product-category")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
@RequiredArgsConstructor
public class ProductCategoryController extends ABasicController {
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductCategoryMapper productCategoryMapper;
    private final CommonApiService commonApiService;

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<ProductCategoryDto>> list(ProductCategoryCriteria productCategoryCriteria, Pageable pageable) {
        Page<ProductCategory> categoryPage = productCategoryRepository.findAll(productCategoryCriteria.getSpecification(), pageable);
        List<ProductCategoryDto> productCategoryDtoList = productCategoryMapper.fromProductCategoryListToDtoList(categoryPage.getContent());
        return new ApiMessageDto<>(new ResponseListObj<>(productCategoryDtoList, categoryPage), "Get list successfully");
    }

    @GetMapping(value = "/auto-complete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<List<ProductCategoryDto>> autoComplete(ProductCategoryCriteria productCategoryCriteria) {
        List<ProductCategory> productCategoryList = productCategoryRepository.findAll(productCategoryCriteria.getSpecification(), Sort.by(Sort.Order.asc("name")));
        return new ApiMessageDto<>(
                productCategoryMapper.fromProductCategoryListToDtoList(productCategoryList),
                "Get list successfully"
        );
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ProductCategoryDto> get(@PathVariable(name = "id") Long id) {
        ProductCategory productCategory = productCategoryRepository.findById(id)
                .orElseThrow(() -> new RequestException(ErrorCode.PRODUCT_CATEGORY_ERROR_NOT_FOUND, "Product category not found"));
        return new ApiMessageDto<>(productCategoryMapper.fromProductCategoryEntityToDto(productCategory), "Get product category successfully");
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> create(@Valid @RequestBody CreateProductCategoryForm createProductCategoryForm, BindingResult bindingResult) {
        if (!isAdmin()) {
            throw new RequestException(ErrorCode.PRODUCT_CATEGORY_ERROR_UNAUTHORIZED, "Not allowed to create");
        }
        ProductCategory productCategory = productCategoryMapper.fromCreateProductCategoryFormToEntity(createProductCategoryForm);
        if (createProductCategoryForm.getParentId() != null) {
            ProductCategory parent = productCategoryRepository.findById(createProductCategoryForm.getParentId())
                    .orElseThrow(() -> new RequestException(ErrorCode.PRODUCT_CATEGORY_ERROR_NOT_FOUND, "Parent not found"));
            productCategory.setParentCategory(parent);
        }
        productCategoryRepository.save(productCategory);
        return new ApiMessageDto<>("Create product category successfully");
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> update(@Valid @RequestBody UpdateProductCategoryForm updateProductCategoryForm, BindingResult bindingResult) {
        if (!isAdmin()) {
            throw new RequestException(ErrorCode.PRODUCT_CATEGORY_ERROR_UNAUTHORIZED, "Not allowed to create");
        }
        ProductCategory productCategory = productCategoryRepository.findById(updateProductCategoryForm.getId())
                .orElseThrow(() -> new RequestException(ErrorCode.PRODUCT_CATEGORY_ERROR_NOT_FOUND, "Parent not found"));
        if (updateProductCategoryForm.getParentId() != null) {
            ProductCategory parent = productCategoryRepository.findById(updateProductCategoryForm.getParentId())
                    .orElseThrow(() -> new RequestException(ErrorCode.PRODUCT_CATEGORY_ERROR_NOT_FOUND, "Parent not found"));
            productCategory.setParentCategory(parent);
        }
        if (StringUtils.isNoneBlank(productCategory.getIcon()) && !updateProductCategoryForm.getIcon().equals(productCategory.getIcon())) {
            commonApiService.deleteFile(productCategory.getIcon());
        }
        productCategoryMapper.fromUpdateProductCategoryFormToEntity(updateProductCategoryForm, productCategory);
        productCategoryRepository.save(productCategory);
        return new ApiMessageDto<>("Update product category successfully");
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> delete(@PathVariable(name = "id") Long id) {
        if (!isAdmin()) {
            throw new RequestException(ErrorCode.PRODUCT_CATEGORY_ERROR_UNAUTHORIZED, "Not allowed to create");
        }
        ProductCategory productCategory = productCategoryRepository.findById(id)
                .orElseThrow(() -> new RequestException(ErrorCode.PRODUCT_CATEGORY_ERROR_NOT_FOUND, "Parent not found"));
        commonApiService.deleteFile(productCategory.getIcon());
        productCategoryRepository.delete(productCategory);
        return new ApiMessageDto<>("Delete product category successfully");
    }
}
