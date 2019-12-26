package com.example.demo.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.dao.FavoritePictureMapper;
import com.example.demo.entity.FavoritePicture;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @program: SE_imsge_share
 * @description: 用户与图片间的互动，点赞，收藏，评论
 * @author: Xuefei Lv
 * @create: 2019/12/24
 **/

@Api(tags = {"用户图片交互类"})
@RestController
@Controller
public class UsersInteractWithPictureController {
 @Autowired
 private FavoritePictureMapper favoritePictureMapper;


    @ApiOperation(
            value = "取消收藏",
            notes = "取消收藏，同时承担检测是否已收藏的任务",
            produces = "application/json"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pid", value = "图片ID", required = true, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "uid", value = "用户ID", required = true, dataType = "Integer", paramType = "query")
    })
    @RequestMapping(value="/pictureCollectDelete",method= RequestMethod.POST)
    public Object pictureCollectDelete(HttpServletRequest req, HttpSession session) {
        Integer pid = Integer.valueOf(req.getParameter("pid"));
        Integer uid = Integer.valueOf(req.getParameter("uid"));
        JSONObject jsonObject = new JSONObject();
        try {
            List<FavoritePicture> favoritePictureList =  favoritePictureMapper.selectByUserIdAndPictureId(uid,pid);
            if(favoritePictureList.isEmpty()){
                jsonObject.put("message","未收藏");
                return jsonObject;
            }else {
                favoritePictureMapper.deleteByPrimaryKey(pid,uid);
                jsonObject.put("message","取消收藏成功");
                return jsonObject;
            }
        }catch (Exception e){
            System.out.println(e);
            jsonObject.put("message","error");
        }
        return jsonObject;
    }

    @ApiOperation(
            value = "收藏图片",
            notes = "收藏图片",
            produces = "application/json"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pid", value = "图片ID", required = true, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "uid", value = "用户ID", required = true, dataType = "Integer", paramType = "query")
    })
    @RequestMapping(value="/pictureCollect",method= RequestMethod.POST)
    public Object pictureCollect(HttpServletRequest req, HttpSession session) {
        Integer pid = Integer.valueOf(req.getParameter("pid"));
        Integer uid = Integer.valueOf(req.getParameter("uid"));
        JSONObject jsonObject = new JSONObject();
        try {
            FavoritePicture favoritePicture = new FavoritePicture();
            favoritePicture.setPictureId(pid);
            favoritePicture.setUserId(uid);
            favoritePictureMapper.insertSelective(favoritePicture);
            jsonObject.put("message","收藏成功");
            return jsonObject;
        }catch (Exception e){
            System.out.println(e);
            jsonObject.put("message","error");
        }
        return jsonObject;
    }
}
