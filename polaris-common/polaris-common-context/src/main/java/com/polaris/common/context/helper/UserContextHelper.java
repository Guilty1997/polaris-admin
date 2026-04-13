package com.polaris.common.context.helper;

import cn.hutool.core.collection.CollUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import com.polaris.common.context.holder.UserContextHolder;
import com.polaris.common.core.constant.SystemConstants;
import com.polaris.common.core.constant.TenantConstants;
import com.polaris.common.core.domain.model.LoginUser;

import java.util.Set;

/**
 * 用户上下文工具类
 * <p>
 * 从 ThreadLocal 中读取当前请求的登录用户信息，不依赖任何认证框架（Sa-Token 等）。
 * 各模块（mybatis、log 等）应使用本类替代 LoginHelper，以解除对 satoken 模块的依赖。
 * <p>
 * 方法列表对齐 LoginHelper，方便迁移时一对一替换：
 * - LoginHelper.getLoginUser()   → UserContextHelper.getLoginUser()
 * - LoginHelper.getUserId()      → UserContextHelper.getUserId()
 * - LoginHelper.getTenantId()    → UserContextHelper.getTenantId()
 * - LoginHelper.getDeptId()      → UserContextHelper.getDeptId()
 * - LoginHelper.getDeptName()    → UserContextHelper.getDeptName()
 * - LoginHelper.getDeptCategory()→ UserContextHelper.getDeptCategory()
 * - LoginHelper.isSuperAdmin()   → UserContextHelper.isSuperAdmin()
 * - LoginHelper.isTenantAdmin()  → UserContextHelper.isTenantAdmin()
 *
 * @see com.polaris.common.context.holder.UserContextHolder
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserContextHelper {

    /**
     * 获取当前登录用户
     *
     * @return 登录用户，未设置时返回 null
     */
    @SuppressWarnings("unchecked")
    public static <T extends LoginUser> T getLoginUser() {
        return (T) UserContextHolder.get();
    }

    /**
     * 当前请求是否有用户上下文（即已登录）
     */
    public static boolean hasUser() {
        return UserContextHolder.get() != null;
    }

    /**
     * 获取当前用户 ID
     */
    public static Long getUserId() {
        LoginUser user = UserContextHolder.get();
        return user != null ? user.getUserId() : null;
    }

    /**
     * 获取当前用户账号
     */
    public static String getUsername() {
        LoginUser user = UserContextHolder.get();
        return user != null ? user.getUsername() : null;
    }

    /**
     * 获取当前租户 ID
     */
    public static String getTenantId() {
        LoginUser user = UserContextHolder.get();
        return user != null ? user.getTenantId() : null;
    }

    /**
     * 获取当前部门 ID
     */
    public static Long getDeptId() {
        LoginUser user = UserContextHolder.get();
        return user != null ? user.getDeptId() : null;
    }

    /**
     * 获取当前部门名称
     */
    public static String getDeptName() {
        LoginUser user = UserContextHolder.get();
        return user != null ? user.getDeptName() : null;
    }

    /**
     * 获取当前部门类别编码
     */
    public static String getDeptCategory() {
        LoginUser user = UserContextHolder.get();
        return user != null ? user.getDeptCategory() : null;
    }

    /**
     * 判断指定用户是否为超级管理员
     */
    public static boolean isSuperAdmin(Long userId) {
        return SystemConstants.SUPER_ADMIN_ID.equals(userId);
    }

    /**
     * 判断当前用户是否为超级管理员
     */
    public static boolean isSuperAdmin() {
        return isSuperAdmin(getUserId());
    }

    /**
     * 根据角色权限标识判断是否为租户管理员
     */
    public static boolean isTenantAdmin(Set<String> rolePermission) {
        if (CollUtil.isEmpty(rolePermission)) {
            return false;
        }
        return rolePermission.contains(TenantConstants.TENANT_ADMIN_ROLE_KEY);
    }

    /**
     * 判断当前用户是否为租户管理员
     */
    public static boolean isTenantAdmin() {
        LoginUser user = UserContextHolder.get();
        if (user == null) {
            return false;
        }
        return isTenantAdmin(user.getRolePermission());
    }

}
