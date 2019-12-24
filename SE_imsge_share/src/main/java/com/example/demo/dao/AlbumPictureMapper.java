package com.example.demo.dao;

import com.example.demo.entity.AlbumPicture;
import org.apache.ibatis.annotations.Param;

public interface AlbumPictureMapper {
    int deleteByPrimaryKey(@Param("albumId") Integer albumId, @Param("pictureId") Integer pictureId);

    int insert(AlbumPicture record);

    int insertSelective(AlbumPicture record);
}