package com.zhang.controller;

import com.zhang.utils.DataResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: TestController
 */
@RestController
@Api(tags = "测试接口模块", description = "主要是为了提供测试接口用")
@RequestMapping("/test")
public class TestController {
    @GetMapping("/index")
    @ApiOperation(value = "引导页接口")
    public String testResult() {
        return "Hello World";
    }

    @GetMapping("/home")
    @ApiOperation(value = "测试DataResult接口")
    public DataResult<String> getHome(){
        DataResult<String> result=DataResult.success(" 测试成功 ");
        return result;
    }

}
