package com.lv.api.mapper;

import com.lv.api.dto.order.OrderItemDto;
import com.lv.api.storage.model.OrderItem;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface OrderItemMapper {

    @Named("fromOrderItemEntityToDtoMapper")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "productName", target = "productName")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "discount", target = "discount")
    @Mapping(source = "quantity", target = "quantity")
    @Mapping(source = "extraVariant", target = "extraVariant")
    @Mapping(source = "orderSort", target = "orderSort")
    OrderItemDto fromOrderItemEntityToDto(OrderItem orderItem);

    @Named("fromOrderItemListToDtoListMapper")
    @IterableMapping(elementTargetType = OrderItemDto.class, qualifiedByName = "fromOrderItemEntityToDtoMapper")
    List<OrderItemDto> fromOrderItemListToDtoList(List<OrderItem> orderItems);
}
