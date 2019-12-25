package com.example.demo.dao;

import com.example.demo.entity.AlbumPicture;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface AlbumPictureMapper {
    int deleteByPrimaryKey(@Param("albumId") Integer albumId, @Param("pictureId") Integer pictureId);

    int insert(AlbumPicture record);

    int insertSelective(AlbumPicture record);
}