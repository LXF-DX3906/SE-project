package com.example.demo.service;

import com.example.demo.entity.User;

public interface UserService {
    Integer phoneVerify(String phone, String pwd);

    Integer emailVerify(String email, String pwd);

    Integer userNameVerify(String userName);

    Integer getIdByPhone(String phone);

    Integer getIdByEmail(String email);

    Integer userRegister(User user);

    User getUserById(Integer userId);

    boolean updateUserMsg(User user);
}
