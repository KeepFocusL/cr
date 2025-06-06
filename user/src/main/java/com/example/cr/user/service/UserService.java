package com.example.cr.user.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import com.example.cr.common.exception.CommonBusinessException;
import com.example.cr.common.exception.UserNotExistsException;
import com.example.cr.common.response.LoginResponse;
import com.example.cr.common.response.R;
import com.example.cr.common.util.SnowflakeUtil;
import com.example.cr.user.entity.User;
import com.example.cr.user.entity.UserExample;
import com.example.cr.common.exception.UserAlreadyExistsException;
import com.example.cr.user.mapper.UserMapper;
import com.example.cr.user.request.LoginRequest;
import com.example.cr.user.request.SendCodeRequest;
import com.example.cr.user.request.UserRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    UserMapper userMapper;

    @Value("${adminIds}")
    List<String> adminIds;

    public long count(){
        return userMapper.countByExample(null);
    }

    public int register(UserRequest request){
        String mobile = request.getMobile();
        // 去数据库查询 mobile 是否存在
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andMobileEqualTo(mobile);
        List<User> users = userMapper.selectByExample(userExample);
        boolean empty = users.isEmpty();
        if (!empty){
            throw new UserAlreadyExistsException("该手机号已注册");
        }

        User user = new User();
        user.setId(SnowflakeUtil.getId());
        user.setMobile(mobile);
        return userMapper.insert(user);
    }

    public void sendCode(SendCodeRequest request) {
        String mobile = request.getMobile();
        List<User> users = selectByMobile(mobile);
        if (users.isEmpty()){
            // 如果为空，说明手机号没有被注册过
            User user = new User();
            user.setId(SnowflakeUtil.getId());
            user.setMobile(mobile);
            userMapper.insert(user);
            log.info("未注册过的手机号：{}, 自动插入数据库", mobile);
        }

        // 生成一个验证码
//        String code = RandomUtil.randomString(4);
        String code = "6666";
        log.info("生成验证码: {}, 手机号: {}, 业务场景: {}", code, mobile, "登录");

        // td-1：保存到验证码历史记录表
        // 建议的表字段 (id, code, mobile, 有效期, 已使用?, 业务类型[登录/忘记密码...], 生成时间, 使用时间)
        // 暂时通过打印日志模拟
        log.info("【模拟】已经保存到验证码历史记录表。验证码：{}，手机号：{}", code, mobile);

        // td-2：对接真实的短信发送服务
        log.info("【模拟】短信发送成功。验证码：{}，手机号：{}", code, mobile);
    }

    /*
     - 已解决 -
    暂时先直接返回 User，实际中不应该把用户所有的信息全部返回出去，因为：
    1. 会涉及隐私的问题，比如密码字段
    2. 会涉及到很多不必要的字段，比如数据库中的创建时间、更新时间、额外标记字段等等
    3. 还会涉及到有些字段并不是 User 这个实体的字段，比如 token 等
     */
    public LoginResponse login(LoginRequest request, boolean isAdminLogin) {
        String code = request.getCode();
        String mobile = request.getMobile();
        List<User> users = selectByMobile(mobile);

        // 校验验证码是否正确
        if (!"6666".equals(code)){
            throw new CommonBusinessException("验证码错误");
        }

        // 校验用户是否存在
        if (users.isEmpty()){
            throw new UserNotExistsException("用户不存在");
        }

        User user = users.get(0);

        // 如果 isAdminLogin 等于 true，只有是 adminIds 配置的用户才能进行登录
        if (isAdminLogin && !adminIds.contains(user.getId().toString())) {
            throw new CommonBusinessException("权限不足！");
        }

        return BeanUtil.copyProperties(user, LoginResponse.class);
    }

    private List<User> selectByMobile(String mobile) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andMobileEqualTo(mobile);
        return userMapper.selectByExample(userExample);
    }
}
