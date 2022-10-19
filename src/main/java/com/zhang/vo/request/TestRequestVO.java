package com.zhang.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 使用Hibernate Validator对 输入 参数进行校验
 */
@Data
@Valid
public class TestRequestVO {
    /**
     * @NotEmpty 用在集合类上面
     * @NotBlank 用在String上面
     * @NotNull 用在基本数据类型上
     * @Valid:启用校验
     */
    @NotEmpty(message = "list数据不能为空")
    @ApiModelProperty(value = "list 集合数据")
    private List<String> list;

    @NotBlank(message = "用户名不能为空")
    @ApiModelProperty(value = "String数据")
    private String username;

    @NotNull(message = "年龄不能为空")
    @ApiModelProperty(value = "基础类型数据")
    private Integer age; // 注意此处必须是Integer，即必须是基础数据类型对应的包装类
}
