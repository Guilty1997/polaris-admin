package com.polaris.common.context.holder;

import com.polaris.common.core.domain.model.LoginUser;

/**
 * 用户上下文持有者
 * <p>
 * 基于 InheritableThreadLocal 存储当前请求的登录用户信息。
 * InheritableThreadLocal 允许子线程（如异步任务）继承父线程的上下文。
 * <p>
 * 填充时机：由 polaris-common-satoken 在登录成功后调用 set()，
 * 清理时机：由 polaris-common-satoken 的拦截器在请求结束时调用 clear()。
 *
 * @see com.polaris.common.context.helper.UserContextHelper
 */
public class UserContextHolder {

    private static final ThreadLocal<LoginUser> USER_THREAD_LOCAL = new InheritableThreadLocal<>();

    private UserContextHolder() {
    }

    /**
     * 设置当前用户上下文
     *
     * @param loginUser 登录用户信息
     */
    public static void set(LoginUser loginUser) {
        USER_THREAD_LOCAL.set(loginUser);
    }

    /**
     * 获取当前用户上下文
     *
     * @return 登录用户信息，未登录时返回 null
     */
    public static LoginUser get() {
        return USER_THREAD_LOCAL.get();
    }

    /**
     * 清除当前用户上下文
     * 请求结束时必须调用，防止内存泄漏
     */
    public static void clear() {
        USER_THREAD_LOCAL.remove();
    }

}
