package com.example.demo.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.dao.AlbumMapper;
import com.example.demo.entity.Album;
import com.example.demo.entity.AlbumPicture;
import com.example.demo.entity.Picture;
import com.example.demo.service.AlbumService;
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
import java.util.List;

/**
 * @program: SE_imsge_share
 * @description: 相册控制类，创建相册，删除、编辑等
 * @author: Xuefei Lv
 * @create: 2019/12/24
 **/

@Api(tags = {"相册控制类"})
@RestController
public class AlbumController {
    @Autowired
    AlbumService albumService;


    @ApiOperation(
            value = "获取相册",
            notes = "按用户uid获取",
            produces = "application/json"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户ID", required = true, dataType = "Integer", paramType = "query")
    })
    @RequestMapping(value="/albumList",method= RequestMethod.POST)
    public Object albumList(HttpServletRequest req, HttpSession session) {
        Integer userId = Integer.valueOf(req.getParameter("uid").trim());
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {
            List<Album> albumList = albumService.getAlbum(userId);
            jsonArray = JSONArray.parseArray(JSONObject.toJSONString(albumList));
            jsonObject.put("albums", jsonArray);
            List<List<Picture>> pictures = new ArrayList<>();
            for (Album album : albumList) {
                List<Picture> picture = albumService.getPicturesByAlbumId(album.getAlbumId());
                pictures.add(picture);
            }
            jsonArray = JSONArray.parseArray(JSONObject.toJSONString(pictures));
            jsonObject.put("pictures", jsonArray);
            jsonObject.put("message", "获取成功");
        } catch (Exception e) {
            jsonObject.put("message", "数据库错误");
        }
        return jsonObject;
    }

    @ApiOperation(
            value = "获取相册内容",
            notes = "按相册gid获取",
            produces = "application/json"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "gid", value = "相册ID", required = true, dataType = "Integer", paramType = "query")
    })
    @RequestMapping(value="/albumDetail",method= RequestMethod.POST)
    public Object albumDetail(HttpServletRequest req, HttpSession session) {
        Integer albumId = Integer.valueOf(req.getParameter("gid").trim());
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {
            List<Picture> pictures = albumService.getPicturesByAlbumId(albumId);
            jsonArray = JSONArray.parseArray(JSONObject.toJSONString(pictures));
            jsonObject.put("pictures", jsonArray);
            jsonObject.put("message", "获取成功");
        } catch (Exception e) {
            jsonObject.put("message", "数据库错误");
        }
        return jsonObject;
    }

    @ApiOperation(
            value = "编辑相册基本信息",
            notes = "按相册gid编辑",
            produces = "application/json"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "gid", value = "相册ID", required = true, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "uid", value = "用户ID", required = true, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "albumName", value = "相册名字", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "相册状态", required = true, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "description", value = "相册描述", required = false, dataType = "String", paramType = "query"),
    })
    @RequestMapping(value="/albumUpdate",method= RequestMethod.POST)
    public Object albumUpdate(HttpServletRequest req, HttpSession session) {
        JSONObject jsonObject = new JSONObject();
        Integer albumId = Integer.valueOf(req.getParameter("gid").trim());
        Integer ownerId = Integer.valueOf(req.getParameter("uid").trim());
        String albumName = req.getParameter("albumName").trim();
        String status = req.getParameter("status").trim();
        String description = req.getParameter("description").trim();
        Album album = new Album();
        album.setAlbumId(albumId);
        album.setOwnerId(ownerId);
        album.setAlbumName(albumName);
        album.setStatus(status);
        album.setDescription(description);
        try {
            boolean res = albumService.updateAlbumMsg(album);
            if (res) {
                jsonObject.put("message", "修改成功");
            } else {
                jsonObject.put("message","修改失败");
            }
        } catch (Exception e) {
            jsonObject.put("message", "数据库错误");
        }
        return jsonObject;
    }

    @ApiOperation(
            value = "创建新相册",
            notes = "根据用户uid创建",
            produces = "application/json"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户ID", required = true, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "albumName", value = "相册名字", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "相册状态", required = true, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "description", value = "相册描述", required = false, dataType = "String", paramType = "query"),
    })
    @RequestMapping(value="/createAlbum",method= RequestMethod.POST)
    public Object createAlbum(HttpServletRequest req, HttpSession session) {
        JSONObject jsonObject = new JSONObject();
        Integer ownerId = Integer.valueOf(req.getParameter("uid").trim());
        String albumName = req.getParameter("albumName").trim();
        String status = req.getParameter("status").trim();
        String description = req.getParameter("description").trim();
        Album album = new Album();
        album.setOwnerId(ownerId);
        album.setAlbumName(albumName);
        album.setStatus(status);
        album.setDescription(description);
        try {
            boolean res = albumService.createAlbum(album);
            if (res) {
                jsonObject.put("message", "创建成功");
            } else {
                jsonObject.put("message","创建失败");
            }
        } catch (Exception e) {
            jsonObject.put("message", "数据库错误");
        }
        return jsonObject;
    }

    @ApiOperation(
            value = "删除相册",
            notes = "根据用户uid删除对应gid的相册",
            produces = "application/json"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户ID", required = true, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "gid", value = "相册ID", required = true, dataType = "Integer", paramType = "query"),
    })
    @RequestMapping(value="/albumDelete",method= RequestMethod.POST)
    public Object albumDelete(HttpServletRequest req, HttpSession session) {
        JSONObject jsonObject = new JSONObject();
        Integer albumId = Integer.valueOf(req.getParameter("gid").trim());
        Integer ownerId = Integer.valueOf(req.getParameter("uid").trim());
        Album album = new Album();
        album.setAlbumId(albumId);
        album.setOwnerId(ownerId);
        try {
            boolean res1 = albumService.deleteAlbum(album);
            boolean res2 = true;
            List<Picture> pictures = albumService.getPicturesByAlbumId(albumId);
            if (pictures.size() > 0) {
                for (Picture picture : pictures) {
                    res2 = res2 && albumService.deletePictures(albumId, picture.getPictureId());
                }
            }
            if (res1 && res2) {
                jsonObject.put("message", "删除成功");
            } else {
                jsonObject.put("message","删除失败");
            }
        } catch (Exception e) {
            jsonObject.put("message", "数据库错误");
        }
        return jsonObject;
    }

    @ApiOperation(
            value = "删除相册中的图片",
            notes = "根据相册gid删除对应pid的图片",
            produces = "application/json"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "gid", value = "相册ID", required = true, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "pid", value = "图片ID", required = true, dataType = "Integer", paramType = "query"),
    })
    @RequestMapping(value="/deleteFromAlbum",method= RequestMethod.POST)
    public Object deleteFromAlbum(HttpServletRequest req, HttpSession session) {
        JSONObject jsonObject = new JSONObject();
        Integer albumId = Integer.valueOf(req.getParameter("gid").trim());
        Integer pictureId = Integer.valueOf(req.getParameter("pid").trim());
        try {
            boolean res = albumService.deletePictures(albumId, pictureId);
            if (res) {
                jsonObject.put("message", "删除成功");
            } else {
                jsonObject.put("message","删除失败");
            }
        } catch (Exception e) {
            jsonObject.put("message", "数据库错误");
        }
        return jsonObject;
    }
}
