package com.lv.api.mapper;

import com.lv.api.dto.order.OrderAdminDto;
import com.lv.api.dto.order.OrderDto;
import com.lv.api.dto.order.OrderStatusDto;
import com.lv.api.storage.model.Order;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {OrderItemMapper.class, CustomerMapper.class, LocationMapper.class}
)
public interface OrderMapper {

    @Named("fromOrderEntityToAdminDtoListPageMapper")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "customer", target = "customer", qualifiedByName = "fromCustomerEntityToAdminDtoMapper")
    @Mapping(source = "subTotal", target = "subTotal")
    @Mapping(source = "shippingCharge", target = "shippingCharge")
    @Mapping(source = "province", target = "province", qualifiedByName = "fromEntityToLocationDtoMapper")
    @Mapping(source = "district", target = "district", qualifiedByName = "fromEntityToLocationDtoMapper")
    @Mapping(source = "ward", target = "ward", qualifiedByName = "fromEntityToLocationDtoMapper")
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
    @Mapping(source = "province", target = "province", qualifiedByName = "fromEntityToLocationDtoMapper")
    @Mapping(source = "district", target = "district", qualifiedByName = "fromEntityToLocationDtoMapper")
    @Mapping(source = "ward", target = "ward", qualifiedByName = "fromEntityToLocationDtoMapper")
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
    @Mapping(source = "orderItems", target = "orderItems", qualifiedByName = "fromOrderItemListToDtoListMapper")
    OrderAdminDto fromOrderEntityToAdminDtoDetails(Order order);

    @Named("fromOrderEntityToOrderDtoMapper")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "subTotal", target = "subTotal")
    @Mapping(source = "shippingCharge", target = "shippingCharge")
    @Mapping(source = "province", target = "province", qualifiedByName = "fromEntityToLocationDtoMapper")
    @Mapping(source = "district", target = "district", qualifiedByName = "fromEntityToLocationDtoMapper")
    @Mapping(source = "ward", target = "ward", qualifiedByName = "fromEntityToLocationDtoMapper")
    @Mapping(source = "addressDetails", target = "addressDetails")
    @Mapping(source = "receiverFullName", target = "receiverFullName")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "paymentMethod", target = "paymentMethod")
    @Mapping(source = "note", target = "note")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "status", target = "status")
    OrderDto fromOrderEntityToOrderDtoListPage(Order order);

    @Named("fromOrderEntityListToOrderDtoListPageMapper")
    @IterableMapping(elementTargetType = OrderDto.class, qualifiedByName = "fromOrderEntityToOrderDtoMapper")
    List<OrderDto> fromOrderEntityListToOrderDtoListPage(List<Order> orders);

    @Named("fromOrderEntityToOrderDtoDetails")
    @Mapping(source = "id", target = "id")
    @Mapping(source = "subTotal", target = "subTotal")
    @Mapping(source = "shippingCharge", target = "shippingCharge")
    @Mapping(source = "province", target = "province", qualifiedByName = "fromEntityToLocationDtoMapper")
    @Mapping(source = "district", target = "district", qualifiedByName = "fromEntityToLocationDtoMapper")
    @Mapping(source = "ward", target = "ward", qualifiedByName = "fromEntityToLocationDtoMapper")
    @Mapping(source = "addressDetails", target = "addressDetails")
    @Mapping(source = "receiverFullName", target = "receiverFullName")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "paymentMethod", target = "paymentMethod")
    @Mapping(source = "note", target = "note")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "orderItems", target = "orderItems", qualifiedByName = "fromOrderItemListToDtoListMapper")
    OrderDto fromOrderEntityToOrderDtoDetails(Order order);

    @Named("fromOrderEntityToOrderStatusDtoMapper")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "modifiedDate", target = "dateTime", dateFormat = "dd/MM/yyyy HH:mm:ss")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "shippingCharge", target = "shippingCharge")
    @Mapping(source = "province", target = "province", qualifiedByName = "fromEntityToLocationDtoMapper")
    @Mapping(source = "district", target = "district", qualifiedByName = "fromEntityToLocationDtoMapper")
    @Mapping(source = "ward", target = "ward", qualifiedByName = "fromEntityToLocationDtoMapper")
    @Mapping(source = "addressDetails", target = "addressDetails")
    @Mapping(source = "note", target = "note")
    OrderStatusDto fromOrderEntityToOrderStatusDto(Order orderStatus);

    @IterableMapping(elementTargetType = OrderStatusDto.class, qualifiedByName = "fromOrderEntityToOrderStatusDtoMapper")
    List<OrderStatusDto> fromOrderEntityAuditListToOrderStatusDtoList(List<Order> orders);
}
