package com.lv.api.mapper;

import com.lv.api.dto.order.OrderAdminDto;
import com.lv.api.dto.order.OrderStatusDto;
import com.lv.api.storage.model.Order;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {OrderItemMapper.class}
)
public interface OrderMapper {

    @Named("fromOrderEntityToAdminDtoListPageMapper")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "subTotal", target = "subTotal")
    @Mapping(source = "shippingCharge", target = "shippingCharge")
    @Mapping(source = "province.name", target = "province")
    @Mapping(source = "district.name", target = "district")
    @Mapping(source = "ward.name", target = "ward")
    @Mapping(source = "addressDetails", target = "addressDetails")
    @Mapping(source = "receiverFullName", target = "receiverFullName")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "paymentMethod", target = "paymentMethod")
    @Mapping(source = "note", target = "note")
    @Mapping(source = "modifiedDate", target = "modifiedDate")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "modifiedBy", target = "modifiedBy")
    @Mapping(source = "createdBy", target = "createdBy")
    @Mapping(source = "status", target = "status")
    OrderAdminDto fromOrderEntityToAdminDtoListPage(Order order);

    @Named("fromOrderEntityListToAdminDtoListPageMapper")
    @IterableMapping(elementTargetType = OrderAdminDto.class, qualifiedByName = "fromOrderEntityToAdminDtoListPageMapper")
    List<OrderAdminDto> fromOrderEntityListToAdminDtoListPage(List<Order> orders);

    @Named("fromOrderEntityToDtoDetailsMapper")
    @Mapping(source = "id", target = "id")
    @Mapping(source = "subTotal", target = "subTotal")
    @Mapping(source = "shippingCharge", target = "shippingCharge")
    @Mapping(source = "province.name", target = "province")
    @Mapping(source = "district.name", target = "district")
    @Mapping(source = "ward.name", target = "ward")
    @Mapping(source = "addressDetails", target = "addressDetails")
    @Mapping(source = "paymentMethod", target = "paymentMethod")
    @Mapping(source = "note", target = "note")
    @Mapping(source = "orderItems", target = "orderItems", qualifiedByName = "fromOrderItemListToDtoListMapper")
    OrderAdminDto fromOrderEntityToAdminDtoDetails(Order order);

    @Named("fromOrderEntityToOrderStatusDtoMapper")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "createdDate", target = "dateTime", dateFormat = "dd/MM/yyyy HH:mm:ss")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "shippingCharge", target = "shippingCharge")
    @Mapping(source = "province.name", target = "province")
    @Mapping(source = "district.name", target = "district")
    @Mapping(source = "ward.name", target = "ward")
    @Mapping(source = "addressDetails", target = "addressDetails")
    @Mapping(source = "note", target = "note")
    OrderStatusDto fromOrderEntityToOrderStatusDto(Order orderStatus);
}
