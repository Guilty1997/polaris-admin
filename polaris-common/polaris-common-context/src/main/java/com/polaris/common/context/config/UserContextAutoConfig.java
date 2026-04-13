package com.polaris.common.context.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;

/**
 * 用户上下文自动配置
 * <p>
 * 本模块为纯工具模块（ThreadLocal + 静态工具类），无需注册任何 Spring Bean。
 * 该类存在仅为符合 Spring Boot AutoConfiguration 规范，供 spring.factories 引用。
 * <p>
 * 如后续需要注册 UserContext 相关 Bean（如 Web 拦截器用于自动清理 ThreadLocal），
 * 可在此类中添加相应的 @Bean 方法。
 *
 * @see com.polaris.common.context.holder.UserContextHolder
 * @see com.polaris.common.context.helper.UserContextHelper
 */
@AutoConfiguration
public class UserContextAutoConfig {

}
