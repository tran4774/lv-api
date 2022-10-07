package com.lv.api.controller;

import com.lv.api.dto.ApiMessageDto;
import com.lv.api.dto.ErrorCode;
import com.lv.api.dto.ResponseListObj;
import com.lv.api.dto.varianttemplate.VariantTemplateDto;
import com.lv.api.exception.RequestException;
import com.lv.api.form.varianttemplate.CreateVariantTemplateForm;
import com.lv.api.form.varianttemplate.UpdateVariantTemplateForm;
import com.lv.api.mapper.VariantTemplateMapper;
import com.lv.api.storage.criteria.VariantTemplateCriteria;
import com.lv.api.storage.model.VariantTemplate;
import com.lv.api.storage.repository.VariantTemplateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/variant-template")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
@RequiredArgsConstructor
public class VariantTemplateController {
    private final VariantTemplateRepository variantTemplateRepository;
    private final VariantTemplateMapper variantTemplateMapper;

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<VariantTemplateDto>> list(VariantTemplateCriteria variantTemplateCriteria, Pageable pageable) {
        Page<VariantTemplate> variantTemplatePage = variantTemplateRepository.findAll(variantTemplateCriteria.getSpecification(), pageable);
        List<VariantTemplateDto> variantTemplateDtoList = variantTemplateMapper.fromVariantTemplateEntityListToDtoList(variantTemplatePage.getContent());
        return new ApiMessageDto<>(new ResponseListObj<>(
                variantTemplateDtoList, variantTemplatePage
        ), "Get list successfully");
    }

    @GetMapping(value = "/auto-complete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<List<VariantTemplateDto>> autoComplete(VariantTemplateCriteria variantTemplateCriteria) {
        Page<VariantTemplate> variantTemplatePage = variantTemplateRepository.findAll(variantTemplateCriteria.getSpecification(), Pageable.unpaged());
        List<VariantTemplateDto> variantTemplateDtoList = variantTemplateMapper.fromVariantTemplateEntityListToDtoList(variantTemplatePage.getContent());
        return new ApiMessageDto<>(variantTemplateDtoList, "Get list successfully");
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<VariantTemplateDto> get(@PathVariable(name = "id") Long id) {
        VariantTemplate variantTemplate = variantTemplateRepository.findById(id)
                .orElseThrow(() -> new RequestException(ErrorCode.VARIANT_TEMPLATE_NOT_FOUND, "Variant template not found"));
        return new ApiMessageDto<>(variantTemplateMapper.fromVariantTemplateEntityToDto(variantTemplate), "Get variant template successfully");
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> create(@Valid @RequestBody CreateVariantTemplateForm createVariantTemplateForm, BindingResult bindingResult) {
        VariantTemplate variantTemplate = variantTemplateMapper.fromCreateVariantTemplateFormToEntity(createVariantTemplateForm);
        variantTemplateRepository.save(variantTemplate);
        return new ApiMessageDto<>("Create variant template successfully");
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> update(@Valid @RequestBody UpdateVariantTemplateForm updateVariantTemplateForm, BindingResult bindingResult) {
        VariantTemplate variantTemplate = variantTemplateRepository.findById(updateVariantTemplateForm.getId())
                .orElseThrow(() -> new RequestException(ErrorCode.VARIANT_TEMPLATE_NOT_FOUND, "Variant template not found"));
        variantTemplateMapper.fromUpdateVariantTemplateFormToEntity(updateVariantTemplateForm, variantTemplate);
        variantTemplateRepository.save(variantTemplate);
        return new ApiMessageDto<>("Update variant template successfully");
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> delete(@PathVariable(name = "id") Long id) {
        VariantTemplate variantTemplate = variantTemplateRepository.findById(id)
                .orElseThrow(() -> new RequestException(ErrorCode.VARIANT_TEMPLATE_NOT_FOUND, "Variant template not found"));
        variantTemplateRepository.delete(variantTemplate);
        return new ApiMessageDto<>("Delete variant template successfully");
    }
}
