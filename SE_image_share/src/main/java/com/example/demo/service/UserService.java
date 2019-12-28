package com.example.demo.service;

import com.example.demo.entity.Follow;
import com.example.demo.entity.User;

import java.util.List;

public interface UserService {
    Integer phoneVerify(String phone, String pwd);

    Integer emailVerify(String email, String pwd);

    Integer userNameVerify(String userName);

    Integer getIdByPhone(String phone);

    Integer getIdByEmail(String email);

    Integer userRegister(User user);

    User getUserById(Integer userId);

    boolean updateUserMsg(User user);

    boolean uploadAvatar(Integer uid, String head_img);

    Integer getFollowsNum(Integer userId);

    Integer getFansNum(Integer userId);

    List<User> getFollows(Integer userId);

    List<User> getFans(Integer userId);

    boolean deleteFollow(Follow follow);

    boolean addFollow(Follow follow);
}
