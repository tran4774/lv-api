package com.lv.api.dto.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderStatusDto {

    @ApiModelProperty(name = "id")
    private Long id;

    @ApiModelProperty(name = "dateTime")
    private String dateTime;

    @ApiModelProperty(name = "modifiedBy")
    private String modifiedBy;

    @ApiModelProperty(name = "status")
    private Integer status;

    @ApiModelProperty(name = "shippingCharge")
    private Double shippingCharge;

    @ApiModelProperty(name = "province")
    private String province;

    @ApiModelProperty(name = "district")
    private String district;

    @ApiModelProperty(name = "ward")
    private String ward;

    @ApiModelProperty(name = "addressDetails")
    private String addressDetails;

    @ApiModelProperty(name = "note")
    private String note;
}
