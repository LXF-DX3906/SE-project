package com.example.demo.dao;

import com.example.demo.entity.Album;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface AlbumMapper {
    int deleteByPrimaryKey(Integer albumId);

    int insert(Album record);

    int insertSelective(Album record);

    Album selectByPrimaryKey(Integer albumId);

    int updateByPrimaryKeySelective(Album record);

    int updateByPrimaryKey(Album record);
}