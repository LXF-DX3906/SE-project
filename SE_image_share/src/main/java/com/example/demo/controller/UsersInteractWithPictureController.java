package com.example.demo.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.dao.*;
import com.example.demo.entity.*;
import com.example.demo.service.UserService;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: SE_imsge_share
 * @description: 用户与图片间的互动，点赞，收藏，评论
 * @author: Xuefei Lv
 * @create: 2019/12/24
 **/

@Api(tags = {"用户图片交互类"})
@RestController
public class UsersInteractWithPictureController {
    @Autowired
    private FavoritePictureMapper favoritePictureMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private LikePictureMapper likePictureMapper;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private HavePictureMapper havePictureMapper;
    @Autowired
    private PictureMapper pictureMapper;

    @ApiOperation(
            value = "取消收藏",
            notes = "取消收藏，同时承担检测是否已收藏的任务",
            produces = "application/json"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pid", value = "图片ID", required = true, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "uid", value = "用户ID", required = true, dataType = "Integer", paramType = "query")
    })
    @RequestMapping(value="/api/pictureCollectDelete",method= RequestMethod.DELETE)
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
    @RequestMapping(value="/api/pictureCollect",method= RequestMethod.POST)
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

    @ApiOperation(
            value = "增加评论",
            notes = "为图片增加评论",
            produces = "application/json"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pid", value = "图片ID", required = true, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "uid", value = "用户ID", required = true, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "content", value = "评论内容", required = true, dataType = "String", paramType = "query")
    })
    @RequestMapping(value="/api/userComment",method= RequestMethod.POST)
    public Object addComment(HttpServletRequest req, HttpSession session) {
        Integer pid = Integer.valueOf(req.getParameter("pid"));
        Integer uid = Integer.valueOf(req.getParameter("uid"));
        String content = req.getParameter("content");
        JSONObject jsonObject = new JSONObject();
        try {
            Comment comment = new Comment();
            comment.setContent(content);
            comment.setPictureId(pid);
            comment.setUserId(uid);
            commentMapper.insertSelective(comment);
            jsonObject.put("message","评论成功");
            return jsonObject;
        }catch (Exception e){
            System.out.println(e);
            jsonObject.put("message","error");
        }
        return jsonObject;
    }

    @ApiOperation(
            value = "图片点赞",
            notes = "图片点赞",
            produces = "application/json"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pid", value = "图片ID", required = true, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "uid", value = "用户ID", required = true, dataType = "Integer", paramType = "query")
    })
    @RequestMapping(value="/api/pictureLike",method= RequestMethod.POST)
    public Object pictureLike(HttpServletRequest req, HttpSession session) {
        String a= req.getParameter("pid");
        Integer pid = Integer.valueOf(req.getParameter("pid"));
        Integer uid = Integer.valueOf(req.getParameter("uid"));
        JSONObject jsonObject = new JSONObject();
        try {
            LikePicture likePicture = new LikePicture();
            likePicture.setUserId(uid);
            likePicture.setPictureId(pid);
            likePictureMapper.insertSelective(likePicture);
            jsonObject.put("message","点赞成功");
            return jsonObject;
        }catch (Exception e){
            System.out.println(e);
            jsonObject.put("message","error");
        }
        return jsonObject;
    }

    @ApiOperation(
            value = "取消点赞",
            notes = "取消图片点赞",
            produces = "application/json"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pid", value = "图片ID", required = true, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "uid", value = "用户ID", required = true, dataType = "Integer", paramType = "query")
    })
    @RequestMapping(value="/api/pictureLikeDelete",method= RequestMethod.DELETE)
    public Object pictureLikeDelete(HttpServletRequest req, HttpSession session) {
        Integer pid = Integer.valueOf(req.getParameter("pid"));
        Integer uid = Integer.valueOf(req.getParameter("uid"));
        JSONObject jsonObject = new JSONObject();
        try {
            likePictureMapper.deleteByPrimaryKey(pid, uid);
            jsonObject.put("message","取消点赞成功");
            return jsonObject;
        }catch (Exception e){
            System.out.println(e);
            jsonObject.put("message","error");
        }
        return jsonObject;
    }

    @ApiOperation(
            value = "获得已点赞图片",
            notes = "获取当前用户已经点赞的图片",
            produces = "application/json"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户ID", required = true, dataType = "Integer", paramType = "query")
    })
    @RequestMapping(value="/api/like",method= RequestMethod.GET)
    public Object like(HttpServletRequest req, HttpSession session) {
        Integer uid = Integer.valueOf(req.getParameter("uid"));
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {
            List<LikePicture> likePictureList = likePictureMapper.getLikePictureByUserId(uid);
            for(LikePicture item : likePictureList){
                JSONObject tempJsonObject = new JSONObject();
                Picture picture = pictureMapper.selectByPrimaryKey(item.getPictureId());
                tempJsonObject = JSONObject.parseObject(JSONObject.toJSONString(picture));
                tempJsonObject.put("uid", havePictureMapper.selectByPrimaryKey(item.getPictureId()).getUserId());
                tempJsonObject.put("like_num", likePictureMapper.getLikeCountById(item.getPictureId()));
                jsonArray.add(tempJsonObject);
            }
            jsonObject.put("result", jsonArray);
            jsonObject.put("message","加载用户点赞图片成功");
            return jsonObject;
        }catch (Exception e){
            System.out.println(e);
            jsonObject.put("message","error");
        }
        return jsonObject;
    }

    @ApiOperation(
            value = "获得已收藏图片",
            notes = "获取当前用户已经收藏的图片",
            produces = "application/json"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户ID", required = true, dataType = "Integer", paramType = "query")
    })
    @RequestMapping(value="/api/collect",method= RequestMethod.GET)
    public Object collect(HttpServletRequest req, HttpSession session) {
        Integer uid = Integer.valueOf(req.getParameter("uid"));
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {
            List<Integer> favoritePictureIdList = favoritePictureMapper.selectByUserId(uid);
            List<Picture> pictures = new ArrayList<>();
            for(Integer item : favoritePictureIdList){
                pictures.add(pictureMapper.selectByPrimaryKey(item));
            }
            jsonArray = JSONArray.parseArray(JSONObject.toJSONString(pictures));
            jsonObject.put("result", jsonArray);
            jsonObject.put("message","获取成功");
            return jsonObject;
        }catch (Exception e){
            System.out.println(e);
            jsonObject.put("message","error");
        }
        return jsonObject;
    }
}
