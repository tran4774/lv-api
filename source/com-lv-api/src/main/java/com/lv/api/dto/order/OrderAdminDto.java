package com.lv.api.dto.order;

import com.lv.api.dto.ABasicAdminDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OrderAdminDto extends ABasicAdminDto {

    @ApiModelProperty(name = "subTotal")
    private Double subTotal;

    @ApiModelProperty(name = "shippingCharge")
    private Double shippingCharge;

    @ApiModelProperty(name = "province")
    private String province;

    @ApiModelProperty(name = "district")
    private String district;

    @ApiModelProperty(name = "ward")
    private String ward;

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

    @ApiModelProperty(name = "orderStatus")
    private List<OrderStatusDto> orderStatuses = new ArrayList<>();
}
