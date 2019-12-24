package com.example.demo.dao;

import com.example.demo.entity.HavePicture;

public interface HavePictureMapper {
    int deleteByPrimaryKey(Integer pictureId);

    int insert(HavePicture record);

    int insertSelective(HavePicture record);

    HavePicture selectByPrimaryKey(Integer pictureId);

    int updateByPrimaryKeySelective(HavePicture record);

    int updateByPrimaryKey(HavePicture record);
}