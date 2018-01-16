package com.oovever.common;

import com.oovever.model.SysUser;
import com.oovever.model.SysUser;

import javax.servlet.http.HttpServletRequest;

/**
 * 使用ThreadLocal用于用户登录，防止并发过程中登录冲突
 */
public class RequestHolder {

    private static final ThreadLocal<SysUser> userHolder = new ThreadLocal<SysUser>();

    private static final ThreadLocal<HttpServletRequest> requestHolder = new ThreadLocal<HttpServletRequest>();

    public static void add(SysUser sysUser) {
        userHolder.set(sysUser);
    }

    public static void add(HttpServletRequest request) {
        requestHolder.set(request);
    }

    public static SysUser getCurrentUser() {
        return userHolder.get();
    }

    public static HttpServletRequest getCurrentRequest() {
        return requestHolder.get();
    }

    /**
     * 移除local
     */
    public static void remove() {
        userHolder.remove();
        requestHolder.remove();
    }
}
