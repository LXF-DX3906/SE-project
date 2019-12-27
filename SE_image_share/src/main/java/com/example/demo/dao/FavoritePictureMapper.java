package com.example.demo.dao;

import com.example.demo.entity.FavoritePicture;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface FavoritePictureMapper {
    int deleteByPrimaryKey(@Param("pictureId") Integer pictureId, @Param("userId") Integer userId);

    int insert(FavoritePicture record);

    int insertSelective(FavoritePicture record);

    List<FavoritePicture> selectByUserIdAndPictureId(@Param("userId") Integer userId, @Param("pictureId") Integer pictureId);
}