package com.lv.api.form.product;

import com.lv.api.form.productconfig.UpdateProductConfigForm;
import com.lv.api.validation.Hashtag;
import com.lv.api.validation.ProductKind;
import com.lv.api.validation.Status;
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
public class UpdateProductForm {

    @ApiModelProperty(name = "id")
    private Long id;

    @ApiModelProperty(name = "categoryId", notes = "Category có thể null do product có thể không thuộc category nào")
    private Long categoryId;

    @Hashtag
    @Size(max = 255)
    @ApiModelProperty(name = "tags")
    private String tags;

    @ApiModelProperty(name = "description")
    private String description;

    @NotBlank(message = "Product name con not be null")
    @ApiModelProperty(name = "name", required = true)
    private String name;

    @NotNull(message = "Price can not be null")
    @ApiModelProperty(name = "price", required = true)
    private Double price;

    @ApiModelProperty(name = "image")
    private String image;

    @ApiModelProperty(name = "isSoldOut")
    private Boolean isSoldOut = false;

    @ApiModelProperty(name = "productParentId")
    private Long productParentId;

    @Status
    @ApiModelProperty(name = "status")
    private Integer status = 1;

    @ApiModelProperty(name = "productConfigs")
    private List<@Valid UpdateProductConfigForm> productConfigs = new ArrayList<>();
}
