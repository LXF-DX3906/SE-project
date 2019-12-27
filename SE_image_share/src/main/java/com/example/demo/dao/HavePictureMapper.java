package com.example.demo.dao;

import com.example.demo.entity.HavePicture;
import com.example.demo.entity.Picture;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface HavePictureMapper {
    int deleteByPrimaryKey(Integer pictureId);

    int insert(HavePicture record);

    int insertSelective(HavePicture record);

    HavePicture selectByPrimaryKey(Integer pictureId);

    int updateByPrimaryKeySelective(HavePicture record);

    int updateByPrimaryKey(HavePicture record);

    int selectUserIdByPictureId(Integer pictureId);

    List<Integer> getPictureId(Integer userId);
}