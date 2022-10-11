package com.lv.api.mapper;

import com.lv.api.dto.productconfig.ProductConfigDto;
import com.lv.api.form.productconfig.CreateProductConfigForm;
import com.lv.api.form.productconfig.UpdateProductConfigForm;
import com.lv.api.storage.model.ProductConfig;
import com.lv.api.storage.model.ProductVariant;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {ProductVariantMapper.class}
)
public interface ProductConfigMapper {

    @Named("fromCreateProductConfigFormToEntityMapper")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "choiceKind", target = "choiceKind")
    @Mapping(source = "isRequired", target = "isRequired")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "variants", target = "variants", qualifiedByName = "fromCreateProductVariantFormListToEntityListMapper")
    ProductConfig fromCreateProductConfigFormToEntity(CreateProductConfigForm createProductConfigForm);

    @Named("fromCreateProductConfigFormListToEntityListMapper")
    @IterableMapping(elementTargetType = ProductConfig.class, qualifiedByName = "fromCreateProductConfigFormToEntityMapper")
    List<ProductConfig> fromCreateProductConfigFormListToEntityList(List<CreateProductConfigForm> createProductConfigForms);

    @Named("fromProductConfigEntityToDtoMapper")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "choiceKind", target = "choiceKind")
    @Mapping(source = "isRequired", target = "isRequired")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "variants", target = "variants", qualifiedByName = "fromProductVariantEntityToDtoMapper")
    ProductConfigDto fromProductConfigEntityToDto(ProductConfig productConfig);

    @Named("fromUpdateProductConfigFormToEntityMapper")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "choiceKind", target = "choiceKind")
    @Mapping(source = "isRequired", target = "isRequired")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "variants", target = "variants", qualifiedByName = "fromUpdateProductVariantListToEntityListMapper")
    void frommUpdateProductConfigFormToEntity(UpdateProductConfigForm updateProductConfigForm, @MappingTarget ProductConfig productConfig);
}
