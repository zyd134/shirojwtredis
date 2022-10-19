package com.zhang.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;
/**
 *
 * 前端传入值方向的VO
 *
 * vo 里的每一个字段，是和你前台 html 页面相对应
 * VO即value object值对象
 * 主要体现在视图的对象，对于一个WEB页面将整个页面的属性封装成一个对象。然后用一个VO对象在控制层与视图层进行传输交换。
 * DTO (经过处理后的PO，可能增加或者减少PO的属性)：
 * Data Transfer Object数据传输对象
 * 主要用于远程调用等需要大量传输对象的地方。
 * 比如我们一张表有100个字段，那么对应的PO就有100个属性。
 * 但是我们界面上只要显示10个字段，
 * 客户端用WEB service来获取数据，没有必要把整个PO对象传递到客户端，
 * 这时我们就可以用只有这10个属性的DTO来传递结果到客户端，这样也不会暴露服务端表结构.到达客户端以后，如果用这个对象来对应界面显示，那此时它的身份就转为VO。
 */
@Data
public class LoginReqVO {
    @ApiModelProperty(value = "账号")
    private String username;
    @ApiModelProperty(value = "用户密码")
    private String password;
    @ApiModelProperty(value = "登录类型(1:pc;2:App)")
    @NotBlank(message = "登录类型不能为空")
    private String type;
}
