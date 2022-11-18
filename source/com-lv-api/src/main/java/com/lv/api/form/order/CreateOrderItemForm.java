package com.lv.api.form.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CreateOrderItemForm {

    @NotNull
    @ApiModelProperty(name = "productName")
    private Long productId;

    @NotNull
    @ApiModelProperty(name = "quantity")
    private Integer quantity;

    @ApiModelProperty(name = "productConfigs")
    private List<OrderProductConfig> productConfigs = new ArrayList<>();

    @Getter
    @Setter
    public static class OrderProductConfig {
        private Long configId;
        private List<Long> variantIds;
    }
}
