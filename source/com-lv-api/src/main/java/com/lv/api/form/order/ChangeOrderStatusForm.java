package com.lv.api.form.order;

import com.lv.api.validation.OrderStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ChangeOrderStatusForm {

    @NotNull
    @ApiModelProperty(name = "orderId", required = true)
    private Long orderId;

    @OrderStatus
    @ApiModelProperty(name = "orderStatus", required = true)
    private Integer orderStatus;
}
