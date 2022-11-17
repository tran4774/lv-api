package com.lv.api.form.order;

import com.lv.api.validation.PaymentMethod;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CreateOrderForm {

    @NotNull
    @ApiModelProperty(name = "provinceId", required = true)
    private Long provinceId;

    @NotNull
    @ApiModelProperty(name = "districtId", required = true)
    private Long districtId;

    @NotNull
    @ApiModelProperty(name = "wardId", required = true)
    private Long wardId;

    @NotBlank
    @ApiModelProperty(name = "addressDetails", required = true)
    private String addressDetails;

    @NotBlank
    @ApiModelProperty(name = "receiverName", required = true)
    private String receiverName;

    @NotBlank
    @ApiModelProperty(name = "receiverPhone", required = true)
    private String receiverPhone;

    @PaymentMethod
    @ApiModelProperty(name = "paymentMethod", required = true)
    private Integer paymentMethod;

    @ApiModelProperty(name = "note")
    private String note;

    @ApiModelProperty(name = "promoCode")
    private String promoCode;

    @Size(min = 1)
    @ApiModelProperty(name = "orderItems", required = true)
    private List<@Valid CreateOrderItemForm> orderItems = new ArrayList<>();
}
