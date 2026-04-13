package com.polaris.common.satoken.interceptor;

import com.polaris.common.context.holder.UserContextHolder;
import com.polaris.common.core.domain.model.LoginUser;
import com.polaris.common.satoken.utils.LoginHelper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 用户上下文拦截器
 * <p>
 * 每次 HTTP 请求进入时，从 Sa-Token Session 中读取当前登录用户，
 * 填入 {@link UserContextHolder}（ThreadLocal），
 * 使 mybatis/log 等模块通过 UserContextHelper 获取用户信息而无需依赖 Sa-Token。
 * <p>
 * 请求结束时（{@code afterCompletion}）必须清理 ThreadLocal，
 * 防止线程池复用时数据串漏。
 *
 * @see UserContextHolder
 * @see com.polaris.common.satoken.config.UserContextWebConfig
 */
public class UserContextInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            if (LoginHelper.isLogin()) {
                LoginUser loginUser = LoginHelper.getLoginUser();
                if (loginUser != null) {
                    UserContextHolder.set(loginUser);
                }
            }
        } catch (Exception ignored) {
            // 未登录的公开接口不需要上下文，静默忽略
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 请求结束后清理，防止线程池复用时上下文污染
        UserContextHolder.clear();
    }
}

