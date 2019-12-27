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
    public List<Album> getAlbum(Integer ownerId) {
        return albumMapper.getAlbum(ownerId);
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

    @Override
    public boolean updateAlbumMsg(Album album) {
        return albumMapper.updateByPrimaryKeySelective(album) > 0;
    }

    @Override
    public boolean createAlbum(Album album) {
        albumMapper.insertSelective(album);
        return album.getAlbumId() > 0;
    }

    @Override
    public boolean deleteAlbum(Album album) {
        return albumMapper.deleteAlbum(album) > 0;
    }

    @Override
    public boolean deletePictures(Integer albumId, Integer pictureId) {
        return albumPictureMapper.deleteByPrimaryKey(albumId, pictureId) > 0;
    }
}
