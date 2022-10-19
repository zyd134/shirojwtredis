package com.zhang.controller;

import com.zhang.exception.BusinessException;
import com.zhang.exception.code.BaseResponseCode;
import com.zhang.utils.DataResult;
import com.zhang.vo.request.TestRequestVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
        int i=1/0; // 测试异常
        DataResult<String> result=DataResult.success(" 测试成功 ");
        return result;
    }

    @GetMapping("/business/error")
    @ApiOperation(value = "测试主动抛出业务异常接口")
    public DataResult<String> testBusinessError(@RequestParam String type){
        if(!(type.equals("1")||type.equals("2")||type.equals("3"))){
            throw new BusinessException(BaseResponseCode.DATA_ERROR);
        }
        DataResult<String> result=new DataResult(0,type);
        return result;
    }

    @PostMapping("/validator")
    @ApiOperation(value = "测试validator对接口")
    public DataResult<String> validator(@RequestBody @Valid TestRequestVO testRequestVO){
        DataResult<String> result=DataResult.success(" 测试成功 ");
        return result;
    }

}
