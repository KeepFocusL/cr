package com.example.cr.user.controller;

import com.example.cr.common.util.CustomJWTUtils;
import com.example.cr.common.util.SnowflakeUtil;
import com.example.cr.user.entity.User;
import com.example.cr.user.mapper.UserMapper;
import com.example.cr.user.request.UserRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserMapper userMapper;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    CustomJWTUtils customJWTUtils;

    static String token;

    @BeforeEach
    void before(){
        // 0、删除数据库的所有内容
        userMapper.deleteByExample(null);

        // 创建一个 token
        if (token == null){
            token = customJWTUtils.createToken(1L, "18123456789");
        }
    }

    @AfterEach
    void after(){
        // 0、删除数据库的所有内容
        userMapper.deleteByExample(null);
    }

    @Test
    void count() throws Exception {
        // Arrange/Act/Assert 或者 Given/When/Then

        // 1、做准备 准备 10 条数据
        int number = 8;
        for (int i = 0; i < number; i++) {
            User user = new User();
            // 假设对象提供了对应的方法
            user.setId(SnowflakeUtil.getId());
            user.setMobile(i + "");
            userMapper.insert(user);
        }

        // 2、真正的发起请求
        // 3、验证：一定返回 10 条数据
        mockMvc.perform(get("/user/count").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(number))
        ;
    }

    @Test
    void register() throws Exception {
        // 1、准备
        String mobile = "13312345678";

        UserRequest userRequest = new UserRequest();
        userRequest.setMobile(mobile);
        String content = objectMapper.writeValueAsString(userRequest);

        // 2、操作
        mockMvc.perform(post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk())
        ;
        // 3、验证
        User user = userMapper.selectByExample(null).get(0);
        Assertions.assertEquals(mobile, user.getMobile());
    }

    @Test
    void register_shouldThrowExceptionWhenMobileAlreadyRegistered() throws Exception {
        // 1、准备
        String mobile = "13312345678";

        User user = new User();
        user.setId(SnowflakeUtil.getId());
        user.setMobile(mobile);
        userMapper.insert(user);

        String content = objectMapper.writeValueAsString(user);
        // 2、操作
        // 3、验证
        mockMvc.perform(post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.msg").value("该手机号已注册"))
        ;

    }
}
