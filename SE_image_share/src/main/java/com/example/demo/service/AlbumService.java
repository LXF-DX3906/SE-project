package com.example.demo.service;

import com.example.demo.entity.Album;
import com.example.demo.entity.Picture;
import io.swagger.models.auth.In;

import java.util.List;

public interface AlbumService {
    List<Album> getAlbum(Integer ownerId);

    List<Picture> getPicturesByAlbumId(Integer albumId);

    boolean updateAlbumMsg(Album album);

    boolean createAlbum(Album album);

    boolean deleteAlbum(Album album);

    boolean deletePictures(Integer albumId, Integer pictureId);

    List<Integer> findAlbumPicture(Integer albumId);

    boolean insertPicture(Integer albumId, Integer pictureId);
}
