package com.example.demo.service.impl;

import com.example.demo.dao.HavePictureMapper;
import com.example.demo.dao.LikePictureMapper;
import com.example.demo.dao.PictureMapper;
import com.example.demo.entity.LikeNum;
import com.example.demo.entity.Picture;
import com.example.demo.service.PictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PictureServiceImpl implements PictureService {
    @Autowired
    HavePictureMapper havePictureMapper;
    @Autowired
    PictureMapper pictureMapper;
    @Autowired
    private LikePictureMapper likePictureMapper;

    @Override
    public List<Picture> getPicturesByUserId(Integer userId) {
        List<Integer> pictureIds = havePictureMapper.getPictureId(userId);
        List<Picture> pictures = new ArrayList<>();
        for (Integer pictureId : pictureIds) {
            pictures.add(pictureMapper.selectByPrimaryKey(pictureId));
        }
        return pictures;
    }
    @Override
    public List<LikeNum> getAllLikeNum(){ return likePictureMapper.getAllLikeNum(); };

    @Override
    public int getLikeCountById(Integer pictureId){return likePictureMapper.getLikeCountById(pictureId);};

    @Override
    public int selectUserIdByPictureId(Integer pictureId){return havePictureMapper.selectUserIdByPictureId(pictureId);};
}
