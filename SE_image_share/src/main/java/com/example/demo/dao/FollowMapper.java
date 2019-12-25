package com.example.demo.dao;

import com.example.demo.entity.Follow;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface FollowMapper {
    int deleteByPrimaryKey(@Param("userId") Integer userId, @Param("followingId") Integer followingId);

    int insert(Follow record);

    int insertSelective(Follow record);
}