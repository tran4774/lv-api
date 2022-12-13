package com.lv.api.dto.order;

import com.lv.api.dto.location.LocationDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class OrderDto {

    @ApiModelProperty(name = "id")
    private Long id;

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

    @ApiModelProperty(name = "createdDate")
    private Date createdDate;

    @ApiModelProperty(name = "status")
    private Integer status;

    @ApiModelProperty(name = "orderItems")
    private List<OrderItemDto> orderItems = new ArrayList<>();
}
