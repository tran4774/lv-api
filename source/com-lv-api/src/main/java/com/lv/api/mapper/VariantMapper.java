package com.lv.api.mapper;

import com.lv.api.dto.variant.VariantDto;
import com.lv.api.form.variant.CreateVariantForm;
import com.lv.api.form.variant.UpdateVariantForm;
import com.lv.api.storage.model.Variant;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {VariantTemplateMapper.class}
)
public interface VariantMapper {

    @Named("fromCreateVariantFormToEntityMapper")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "value", target = "value")
    @Mapping(source = "price", target = "price")
    Variant fromCreateVariantFormToEntity(CreateVariantForm createVariantForm);

    @Named("fromVariantEntityToDtoMapper")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "value", target = "value")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "variantTemplate", target = "variantTemplate")
    VariantDto fromVariantEntityToDto(Variant variant);

    @Named("fromVariantEntityListToDtoListMapper")
    @IterableMapping(elementTargetType = VariantDto.class, qualifiedByName = "fromVariantEntityToDtoMapper")
    List<VariantDto> fromVariantEntityListToDtoList(List<Variant> variantList);

    @Named("fromUpdateVariantFormToEntityMapper")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "value", target = "value")
    @Mapping(source = "price", target = "price")
    void fromUpdateVariantFormToEntity(UpdateVariantForm updateVariantForm, @MappingTarget Variant variant);
}
