package com.lv.api.form.account;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@ApiModel
public class LoginForm {
    @NotEmpty(message = "username cant not be null")
    @ApiModelProperty(name = "username", required = true)
    private String username;
    @NotEmpty(message = "password cant not be null")
    @ApiModelProperty(name = "password", required = true)
    private String password;
    @NotNull(message = "App can not be null")
    @ApiModelProperty(name = "app")
    private Integer app;
}
