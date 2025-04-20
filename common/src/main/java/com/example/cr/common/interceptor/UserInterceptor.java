package com.example.cr.common.interceptor;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.example.cr.common.context.UserContext;
import com.example.cr.common.response.LoginResponse;
import com.example.cr.common.util.CustomJWTUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class UserInterceptor implements HandlerInterceptor {
    private static final Logger log = LoggerFactory.getLogger(UserInterceptor.class);

    @Autowired
    CustomJWTUtils customJWTUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("UserInterceptor 开始");
        // 获取请求头中 Authorization: Bearer xxx-jwt-token
        String token = request.getHeader("Authorization");
        if (StrUtil.isNotBlank(token)) {
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            log.info("当前请求头中包含的 token = {}", token);
            JSONObject loginUser = customJWTUtils.getJSONObject(token);
            log.info("当前登录会员 = {}", loginUser);
            LoginResponse user = JSONUtil.toBean(loginUser, LoginResponse.class);
            UserContext.setUser(user);
        }
        log.info("UserInterceptor 结束");
        return true;
    }

}
