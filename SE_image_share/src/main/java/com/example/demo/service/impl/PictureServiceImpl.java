package com.example.demo.service.impl;

import com.example.demo.dao.*;
import com.example.demo.entity.HavePicture;
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
    AlbumPictureMapper albumPictureMapper;
    @Autowired
    CommentMapper commentMapper;
    @Autowired
    FavoritePictureMapper favoritePictureMapper;
    @Autowired
    LikePictureMapper likePictureMapper;

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
    public boolean deletePicture(HavePicture havePicture) {
        boolean res1 = havePictureMapper.deleteByPrimaryKey(havePicture.getPictureId()) > 0;
        boolean res2 = pictureMapper.deleteByPrimaryKey(havePicture.getPictureId()) > 0;
        boolean res3 = false;
        try {
            albumPictureMapper.globalDeletePicture(havePicture.getPictureId());
            commentMapper.globalDeletePicture(havePicture.getPictureId());
            favoritePictureMapper.globalDeletePicture(havePicture.getPictureId());
            likePictureMapper.globalDeletePicture(havePicture.getPictureId());
            res3 = true;
        } catch (Exception e) {
            res3 = false;
        }
        return res1 && res2 && res3;
    }
    @Override
    public List<LikeNum> getAllLikeNum(){ return likePictureMapper.getAllLikeNum(); };

    @Override
    public int getLikeCountById(Integer pictureId){return likePictureMapper.getLikeCountById(pictureId);};

    @Override
    public int selectUserIdByPictureId(Integer pictureId){return havePictureMapper.selectUserIdByPictureId(pictureId);};

    @Override
    public Boolean isNum(String s){
        char[] ch = s.toCharArray();
        for (char c : ch) {
            if (!(c > '0' && c <= '9')) {
                return false;
            }
        }
        return true;
    }
}
