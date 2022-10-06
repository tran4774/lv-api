package com.lv.api.mapper;

import com.lv.api.dto.store.StoreDto;
import com.lv.api.form.store.CreateStoreForm;
import com.lv.api.form.store.UpdateStoreForm;
import com.lv.api.storage.model.Store;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface StoreMapper {

    @Named("fromCreateStoreFormToEntityMapper")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "name", target = "name")
    @Mapping(source = "latitude", target = "latitude")
    @Mapping(source = "longitude", target = "longitude")
    @Mapping(source = "addressDetails", target = "addressDetails")
    Store fromCreateStoreFormToEntity(CreateStoreForm createStoreForm);

    @Named("fromStoreEntityToDto")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "latitude", target = "latitude")
    @Mapping(source = "longitude", target = "longitude")
    @Mapping(source = "province.id", target = "provinceId")
    @Mapping(source = "district.id", target = "districtId")
    @Mapping(source = "ward.id", target = "wardId")
    @Mapping(source = "addressDetails", target = "addressDetails")
    StoreDto fromStoreEntityToDto(Store store);

    @Named("fromStoreEntityListToDtoListMapper")
    @IterableMapping(elementTargetType = StoreDto.class, qualifiedByName = "fromStoreEntityToDto")
    List<StoreDto> fromStoreEntityListToDtoList(List<Store> storeList);

    @Named("fromUpdateStoreFormToEntityMapper")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "name", target = "name")
    @Mapping(source = "latitude", target = "latitude")
    @Mapping(source = "longitude", target = "longitude")
    @Mapping(source = "addressDetails", target = "addressDetails")
    void fromUpdateStoreFormToEntity(UpdateStoreForm updateStoreForm, @MappingTarget Store store);
}
