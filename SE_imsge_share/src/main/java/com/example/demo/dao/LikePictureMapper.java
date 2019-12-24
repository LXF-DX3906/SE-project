package com.example.demo.dao;

import com.example.demo.entity.LikePicture;
import org.apache.ibatis.annotations.Param;

public interface LikePictureMapper {
    int deleteByPrimaryKey(@Param("pictureId") Integer pictureId, @Param("userId") Integer userId);

    int insert(LikePicture record);

    int insertSelective(LikePicture record);
}