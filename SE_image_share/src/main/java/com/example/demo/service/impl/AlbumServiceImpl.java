package com.example.demo.service.impl;

import com.example.demo.dao.AlbumMapper;
import com.example.demo.dao.AlbumPictureMapper;
import com.example.demo.dao.PictureMapper;
import com.example.demo.entity.Album;
import com.example.demo.entity.AlbumPicture;
import com.example.demo.entity.Picture;
import com.example.demo.entity.User;
import com.example.demo.service.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AlbumServiceImpl implements AlbumService {
    @Autowired
    AlbumMapper albumMapper;

    @Autowired
    AlbumPictureMapper albumPictureMapper;

    @Autowired
    PictureMapper pictureMapper;

    @Override
    public List<Album> getAlbum(Integer onwerId) {
        return albumMapper.getAlbum(onwerId);
    }

    @Override
    public List<Picture> getPicturesByAlbumId(Integer albumId) {
        List<AlbumPicture> albumPictureList = albumPictureMapper.getPictures(albumId);
        List<Picture> pictures = new ArrayList<>();
        for (AlbumPicture albumPicture : albumPictureList) {
            pictures.add(pictureMapper.selectByPrimaryKey(albumPicture.getPictureId()));
        }
        return pictures;
    }
}
