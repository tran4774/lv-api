package com.lv.api.dto.order;

import com.lv.api.dto.ABasicAdminDto;
import com.lv.api.dto.customer.CustomerAdminDto;
import com.lv.api.dto.customer.CustomerDto;
import com.lv.api.dto.location.LocationDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OrderAdminDto extends ABasicAdminDto {

    @ApiModelProperty(name = "customer")
    private CustomerAdminDto customer;

    @ApiModelProperty(name = "subTotal")
    private Double subTotal;

    @ApiModelProperty(name = "shippingCharge")
    private Double shippingCharge;

    @ApiModelProperty(name = "province")
    private LocationDto province;

    @ApiModelProperty(name = "district")
    private LocationDto district;

    @ApiModelProperty(name = "ward")
    private LocationDto ward;

    @ApiModelProperty(name = "receiverFullName")
    private String receiverFullName;

    @ApiModelProperty(name = "phone")
    private String phone;

    @ApiModelProperty(name = "addressDetails")
    private String addressDetails;

    @ApiModelProperty(name = "paymentMethod")
    private Integer paymentMethod;

    @ApiModelProperty(name = "note")
    private String note;

    @ApiModelProperty(name = "orderItems")
    private List<OrderItemDto> orderItems = new ArrayList<>();
}
