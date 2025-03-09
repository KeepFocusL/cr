package com.example.cr.user.controller;

import com.example.cr.user.entity.User;
import com.example.cr.user.mapper.UserMapper;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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

    @BeforeEach
    void before(){
        // 0、删除数据库的所有内容
        userMapper.deleteByExample(null);
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
            user.setId(System.currentTimeMillis() + i);
            user.setMobile(i + "");
            userMapper.insert(user);
        }

        // 2、真正的发起请求
        // 3、验证：一定返回 10 条数据
        mockMvc.perform(get("/user/count"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(number))
        ;
    }

    @Test
    void register() throws Exception {
        // 1、准备

        // 2、操作
        String mobile = "123456789";
        mockMvc.perform(post("/user/register").param("mobile", mobile))
                .andExpect(status().isOk())
        ;
        // 3、验证
        User user = userMapper.selectByExample(null).get(0);
        Assertions.assertEquals(mobile, user.getMobile());
    }

    @Test
    void register_shouldThrowExceptionWhenMobileAlreadyRegistered() throws Exception {
        // 1、准备
        String mobile = "123456789";

        User user = new User();
        user.setId(System.currentTimeMillis());
        user.setMobile(mobile);
        userMapper.insert(user);

        // 2、操作
        // 正常应该测试返回 RuntimeException，由于默认情况 Spring Boot 的 Controller 层对运行时异常做了二次封装，
        // 所以这里测试捕获 ServletException
        ServletException exception = Assertions.assertThrows(ServletException.class, () -> {
            mockMvc.perform(post("/user/register").param("mobile", mobile));
        });

        // 3、验证
        Assertions.assertTrue(exception.getMessage().contains("该手机号已注册"));
    }
}
