package com.example.demo.service;

import com.example.demo.entity.Picture;

import java.util.List;

public interface PictureService {
    List<Picture> getPicturesByUserId(Integer userId);
}
