package com.zhang.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用于封装分页查询时候的条件的VO
 */
@Data
public class UserPageReqVO {

    @ApiModelProperty(value = "页数 默认值为1")
    private Integer pageNum = 1;

    @ApiModelProperty(value = "数据条数 默认值为10")
    private Integer pageSize = 10;
}
