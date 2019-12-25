package com.example.demo.dao;

import com.example.demo.entity.HavePicture;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface HavePictureMapper {
    int deleteByPrimaryKey(Integer pictureId);

    int insert(HavePicture record);

    int insertSelective(HavePicture record);

    HavePicture selectByPrimaryKey(Integer pictureId);

    int updateByPrimaryKeySelective(HavePicture record);

    int updateByPrimaryKey(HavePicture record);
}