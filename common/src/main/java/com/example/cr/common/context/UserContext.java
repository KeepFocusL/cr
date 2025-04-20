package com.example.cr.common.context;

import com.example.cr.common.response.LoginResponse;

public class UserContext {
    // ThreadLocal 线程本地变量
    private static final ThreadLocal<LoginResponse> THREAD_LOCAL_USER = new ThreadLocal<>();

    public static LoginResponse getUser() {
        return THREAD_LOCAL_USER.get();
    }

    public static void setUser(LoginResponse user) {
        THREAD_LOCAL_USER.set(user);
    }

    public static Long getId() {
        return getUser().getId();
    }

}
