package com.lv.api.form.news;

import com.lv.api.validation.Hashtag;
import com.lv.api.validation.Status;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class UpdateNewsForm {

    @NotNull(message = "id cannot be null")
    @ApiModelProperty(required = true)
    private Long id;

    @NotEmpty(message = "title cannot be null")
    @ApiModelProperty(required = true)
    private String title;

    @NotEmpty(message = "content cannot be null")
    @ApiModelProperty(required = true)
    private String content;

    @NotEmpty(message = "avatar cannot be null")
    @ApiModelProperty(required = true)
    private String avatar;

    private String banner;

    @NotEmpty(message = "description cannot be null")
    @ApiModelProperty(required = true)
    private String description;

    private Integer pinTop;

    @NotNull(message = "status cannot be null")
    @ApiModelProperty(required = true)
    @Status
    private Integer status;

    @Hashtag
    @ApiModelProperty(name = "tags", required = false)
    private String tags;
}
