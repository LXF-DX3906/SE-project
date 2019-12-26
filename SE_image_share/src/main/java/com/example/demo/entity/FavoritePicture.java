package com.example.demo.entity;

import org.springframework.stereotype.Repository;

public class FavoritePicture {
    private Integer pictureId;

    private Integer userId;

    public Integer getPictureId() {
        return pictureId;
    }

    public void setPictureId(Integer pictureId) {
        this.pictureId = pictureId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}