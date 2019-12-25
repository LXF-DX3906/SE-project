package com.example.demo.service.impl;

import com.example.demo.dao.UserMapper;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public Integer phoneVerify(String phone, String pwd) {
        return userMapper.phoneVerify(phone, pwd);
    }

    @Override
    public Integer emailVerify(String email, String pwd) {
        return userMapper.emailVerify(email, pwd);
    }

    @Override
    public Integer userNameVerify(String userName) {
        return userMapper.userNameVerify(userName);
    }

    @Override
    public Integer getIdByPhone(String phone) {
        return userMapper.phoneVerify(phone, null);
    }

    @Override
    public Integer getIdByEmail(String email) {
        return userMapper.emailVerify(email, null);
    }

    @Override
    public Integer userRegister(User user) {
        userMapper.insertSelective(user);
        return user.getUserId();
    }

    @Override
    public User getUserById(Integer userId) {
        return userMapper.selectByPrimaryKey(userId);
    }

    @Override
    public boolean updateUserMsg(User user) {
        return userMapper.updateByPrimaryKeySelective(user) > 0;
    }
}
