package com.example.demo.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * @program: SE_image_share
 * @description: 计算图片被点赞数
 * @author: Xuefei Lv
 * @create: 2019/12/26
 **/

@Getter
@Setter
public class LikeNum {
    private Integer pictureId;
    private Integer likeNum;
}
