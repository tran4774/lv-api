package com.lv.api.dto.store;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoreDto {

    @ApiModelProperty(name = "id")
    private Long id;

    @ApiModelProperty(name = "name")
    private String name;

    @ApiModelProperty(name = "latitude")
    private Double latitude;

    @ApiModelProperty(name = "longitude")
    private Double longitude;

    @ApiModelProperty(name = "provinceId")
    private Long provinceId;

    @ApiModelProperty(name = "districtId")
    private Long districtId;

    @ApiModelProperty(name = "wardId")
    private Long wardId;

    @ApiModelProperty(name = "addressDetails")
    private String addressDetails;
}
