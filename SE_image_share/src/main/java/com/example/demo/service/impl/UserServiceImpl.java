package com.example.demo.service.impl;

import com.example.demo.dao.FollowMapper;
import com.example.demo.dao.UserMapper;
import com.example.demo.entity.Follow;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private FollowMapper followMapper;

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

    @Override
    public Integer getFollowsNum(Integer userId) {
        return followMapper.getFollows(userId).size();
    }

    @Override
    public Integer getFansNum(Integer userId) {
        return followMapper.getFans(userId).size();
    }

    @Override
    public List<User> getFollows(Integer userId) {
        List<Follow> follows = followMapper.getFollows(userId);
        List<User> users = new ArrayList<>();
        for (Follow follow : follows) {
            users.add(userMapper.selectByPrimaryKey(follow.getFollowingId()));
        }
        return users;
    }

    @Override
    public List<User> getFans(Integer userId) {
        List<Follow> follows = followMapper.getFans(userId);
        List<User> users = new ArrayList<>();
        for (Follow follow : follows) {
            users.add(userMapper.selectByPrimaryKey(follow.getUserId()));
        }
        return users;
    }

    @Override
    public boolean deleteFollow(Follow follow) {
        return followMapper.deleteByPrimaryKey(follow.getUserId(), follow.getFollowingId()) > 0;
    }

    @Override
    public boolean addFollow(Follow follow) {
        if (followMapper.ifFollowing(follow) != 0) {
            return false;
        }
        return followMapper.insert(follow) > 0;
    }
}
