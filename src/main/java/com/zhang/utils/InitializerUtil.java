package com.zhang.utils;

import com.zhang.config.TokenSetting;
import org.springframework.stereotype.Component;

/**
 * JWT初始化代理类
 * JWT配置类 与 工具类桥梁
 */
@Component
public class InitializerUtil {

    // 将JWT配置类中的默认参数值 通过SET方法 注入到JWT工具类中
    public InitializerUtil(TokenSetting tokenSetting) {
        JwtTokenUtil.setJwtProperties(tokenSetting);
    }
}
