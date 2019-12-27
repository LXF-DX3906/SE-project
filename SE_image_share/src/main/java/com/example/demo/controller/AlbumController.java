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
    @RequestMapping(value="/galleryList",method= RequestMethod.POST)
    public Object galleryList(HttpServletRequest req, HttpSession session) {
        Integer userId = Integer.valueOf(req.getParameter("uid"));
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
    @RequestMapping(value="/galleryDetail",method= RequestMethod.POST)
    public Object galleryDetail(HttpServletRequest req, HttpSession session) {
        Integer albumId = Integer.valueOf(req.getParameter("gid"));
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
}
