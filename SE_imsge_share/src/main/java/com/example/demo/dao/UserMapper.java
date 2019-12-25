package com.example.demo.dao;

import com.example.demo.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {
    int deleteByPrimaryKey(Integer userId);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer userId);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    Integer phoneVerify(String phone, String pwd);

    Integer emailVerify(String email, String pwd);

    Integer userNameVerify(String userName);
}