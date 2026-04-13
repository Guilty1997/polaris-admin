package com.polaris.common.satoken.config;

import com.polaris.common.satoken.interceptor.UserContextInterceptor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 用户上下文 Web 配置
 * <p>
 * 注册 {@link UserContextInterceptor}，使每个 HTTP 请求在进入处理器前
 * 自动从 Sa-Token Session 恢复用户上下文到 ThreadLocal，
 * 请求结束后自动清理。
 *
 * @see UserContextInterceptor
 */
@AutoConfiguration
public class UserContextWebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new UserContextInterceptor())
            .addPathPatterns("/**");
    }
}

