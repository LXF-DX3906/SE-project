package com.example.demo.dao;

import com.example.demo.entity.FavoritePicture;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface FavoritePictureMapper {
    int deleteByPrimaryKey(@Param("pictureId") Integer pictureId, @Param("userId") Integer userId);

    int insert(FavoritePicture record);

    int insertSelective(FavoritePicture record);
}