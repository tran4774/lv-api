package com.lv.api.controller;

import com.lv.api.constant.Constants;
import com.lv.api.dto.ApiMessageDto;
import com.lv.api.dto.ErrorCode;
import com.lv.api.dto.productcategory.ProductCategoryDto;
import com.lv.api.exception.RequestException;
import com.lv.api.form.productcategory.ChangeOrderProductCategoryFrom;
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
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

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
    public ApiMessageDto<List<ProductCategoryDto>> list(ProductCategoryCriteria productCategoryCriteria) {
        List<ProductCategory> categoryList = productCategoryRepository.findAll(productCategoryCriteria.getSpecification(), Sort.by(Sort.Order.asc("orderSort")));
        return new ApiMessageDto<>(productCategoryMapper.fromProductCategoryListToDtoList(categoryList), "Get list successfully");
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
        ProductCategory productCategory = productCategoryMapper.fromCreateProductCategoryFormToEntity(createProductCategoryForm);
        if (createProductCategoryForm.getParentId() != null) {
            ProductCategory parent = productCategoryRepository.findById(createProductCategoryForm.getParentId())
                    .orElseThrow(() -> new RequestException(ErrorCode.PRODUCT_CATEGORY_ERROR_NOT_FOUND, "Parent not found"));
            productCategory.setParentCategory(parent);
        }
        Integer orderSort = productCategoryRepository.countByParentCategoryId(createProductCategoryForm.getParentId()) + 1;
        productCategory.setOrderSort(orderSort);
        productCategoryRepository.save(productCategory);
        return new ApiMessageDto<>("Create product category successfully");
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> update(@Valid @RequestBody UpdateProductCategoryForm updateProductCategoryForm, BindingResult bindingResult) {
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

    @Transactional
    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> delete(@PathVariable(name = "id") Long id) {
        ProductCategory productCategory = productCategoryRepository.findById(id)
                .orElseThrow(() -> new RequestException(ErrorCode.PRODUCT_CATEGORY_ERROR_NOT_FOUND, "Product category not found"));
        commonApiService.deleteFile(productCategory.getIcon());
        Long parentProductCategoryId = null;
        if (productCategory.getParentCategory() != null) {
            parentProductCategoryId = productCategory.getParentCategory().getId();
        }
        productCategoryRepository.updateOrderSort(parentProductCategoryId, productCategory.getOrderSort(), Integer.MAX_VALUE, -1);
        productCategoryRepository.delete(productCategory);
        return new ApiMessageDto<>("Delete product category successfully");
    }

    //    @Transactional
    @GetMapping(value = "/get-all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<Collection<ProductCategoryDto>> getAll() {
        List<ProductCategory> categoryList = productCategoryRepository.findAllByStatus(Constants.STATUS_ACTIVE);
        Map<Long, ProductCategoryDto> categoryDtoMap = new ConcurrentHashMap<>();
        categoryList.forEach(category -> categoryDtoMap.put(category.getId(), productCategoryMapper.fromProductCategoryEntityToDtoFE(category)));
        categoryList.forEach(category -> {
            ProductCategory parent = category.getParentCategory();
            if (parent != null && !Objects.equals(parent.getStatus(), Constants.STATUS_ACTIVE) && categoryDtoMap.containsKey(parent.getId())) {
                ProductCategoryDto productCategoryParentDto = categoryDtoMap.get(parent.getId());
                if (productCategoryParentDto.getChildCategories() == null) {
                    productCategoryParentDto.setChildCategories(new ArrayList<>());
                }
                productCategoryParentDto.getChildCategories().add(categoryDtoMap.get(category.getId()));
                categoryDtoMap.remove(category.getId());
            }
        });
        return new ApiMessageDto<>(categoryDtoMap.values(), "Get list successfully");
    }

    @Transactional
    @PutMapping(value = "/change-order", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> changeOrder(@Valid @RequestBody ChangeOrderProductCategoryFrom changeOrderProductCategoryFrom, BindingResult bindingResult) {
        ProductCategory productCategory = productCategoryRepository.findById(changeOrderProductCategoryFrom.getId())
                .orElseThrow(() -> new RequestException(ErrorCode.PRODUCT_CATEGORY_ERROR_NOT_FOUND, "Product category not found"));
        ProductCategory parentProductCategory = productCategory.getParentCategory();
        int newOrder = changeOrderProductCategoryFrom.getNewOrder() + 1;
        Long parentProductCategoryId = null;
        if (productCategory.getOrderSort() == newOrder) {
            return new ApiMessageDto<>("Order sort don't need to change");
        }
        int coefficient = 1;
        int start = newOrder;
        int end = productCategory.getOrderSort();
        if (productCategory.getOrderSort() < newOrder) {
            coefficient = -1;
            start = productCategory.getOrderSort();
            end = newOrder;
        }
        if (parentProductCategory != null) {
            parentProductCategoryId = parentProductCategory.getId();
        }
        productCategoryRepository.updateOrderSort(parentProductCategoryId, start, end, coefficient);
        productCategory.setOrderSort(newOrder);
        productCategoryRepository.save(productCategory);
        return new ApiMessageDto<>("Change order successfully");
    }
}
