package com.example.cr.user.controller;

import com.example.cr.user.entity.User;
import com.example.cr.user.mapper.UserMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserMapper userMapper;

    @Test
    void count() throws Exception {
        // Arrange/Act/Assert 或者 Given/When/Then
        // 0、删除数据库的所有内容
        userMapper.deleteByExample(null);

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
                .andExpect(content().string(String.valueOf(number)))
        ;
    }
}
