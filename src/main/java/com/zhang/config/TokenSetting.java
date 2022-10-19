package com.zhang.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * JWT配置类
 */
@Configuration
@ConfigurationProperties(prefix = "jwt") // 将Properties配置文件中以jwt开头的字段注入进来，自动根据本类静态成员变量的变量名进行注入（赋值）
@Data
public class TokenSetting {
    // 注意此类以下成员变量变量名必须和Properties配置文件中jwt开头的字段的变量名一致
    private String secretKey;
    private Duration accessTokenExpireTime;
    private Duration refreshTokenExpireTime;
    private Duration refreshTokenExpireAppTime;
    private String issuer;
}
