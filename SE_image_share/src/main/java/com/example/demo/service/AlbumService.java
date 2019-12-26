package com.example.demo.service;

import com.example.demo.entity.Album;
import com.example.demo.entity.Picture;

import java.util.List;

public interface AlbumService {
    List<Album> getAlbum(Integer onwerId);

    List<Picture> getPicturesByAlbumId(Integer albumId);
}
