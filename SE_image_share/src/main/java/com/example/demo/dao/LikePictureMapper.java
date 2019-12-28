package com.example.demo.dao;

import com.example.demo.entity.LikeNum;
import com.example.demo.entity.LikePicture;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository

public interface LikePictureMapper {
    int deleteByPrimaryKey(@Param("pictureId") Integer pictureId, @Param("userId") Integer userId);

    int insert(LikePicture record);

    int insertSelective(LikePicture record);

    int getLikeCountById(Integer pictureId);

    List<LikeNum> getAllLikeNum();

    List<LikePicture> getLikePictureByUserId(Integer userId);

    Integer globalDeletePicture(Integer pictureId);
}