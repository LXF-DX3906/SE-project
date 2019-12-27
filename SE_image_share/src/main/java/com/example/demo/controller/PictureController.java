package com.example.demo.controller;

import com.alibaba.fastjson.JSONArray;
import com.example.demo.dao.HavePictureMapper;
import com.example.demo.dao.LikePictureMapper;
import com.example.demo.dao.PictureMapper;
import com.example.demo.dao.UserMapper;
import com.example.demo.entity.LikeNum;
import com.example.demo.entity.LikePicture;
import com.example.demo.entity.Picture;
import com.example.demo.entity.Type;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.*;
import org.apache.ibatis.annotations.Lang;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * @program: SE_imsge_share
 * @description: 图片控制类，查看图片信息，查看图片列表
 * @author: Xuefei Lv
 * @create: 2019/12/24
 **/

@Api(tags = {"图片控制类"})
@RestController
public class PictureController {
@Autowired
private PictureMapper pictureMapper;
private Type type = new Type();
@Autowired
private LikePictureMapper likePictureMapper;
@Autowired
private HavePictureMapper havePictureMapper;
@Autowired
private UserMapper userMapper;

    @ApiOperation(
            value = "按类型搜索",
            notes = "按图片的type_name搜索",
            produces = "application/json"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tid", value = "类型ID", required = true, dataType = "Integer", paramType = "query")
    })
    @RequestMapping(value="/typeSearch",method= RequestMethod.POST)
    public Object typeSearch(HttpServletRequest req, HttpSession session) {
        Integer tid = Integer.valueOf(req.getParameter("tid"));
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {
            List<Picture> pictureList =  pictureMapper.selectByType(type.getType_dict().get(tid));
            jsonArray = JSONArray.parseArray(JSONObject.toJSONString(pictureList));
            jsonObject.put("message","success");
            jsonObject.put("result", jsonArray);
        }catch (Exception e){
            System.out.println(e);
            jsonObject.put("message","error");
        }
        return jsonObject;
    }

    @ApiOperation(
            value = "按关键词搜索",
            notes = "按图片的description搜索",
            produces = "application/json"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", value = "关键词", required = true, dataType = "long", paramType = "query")
    })
    @RequestMapping(value="/keywordSearch",method= RequestMethod.POST)
    public Object keywordSearch(HttpServletRequest req, HttpSession session) {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {
            String keyWords = req.getParameter("keyword");
            List<Picture> pictureList = pictureMapper.selectByKeyWords(keyWords);
            jsonArray = JSONArray.parseArray(JSONObject.toJSONString(pictureList));
            jsonObject.put("message","success");
            jsonObject.put("result", jsonArray);
        }catch (Exception e){
            System.out.println(e);
            jsonObject.put("message","error");
        }
        return jsonObject;
    }


    @ApiOperation(
            value = "推荐图片",
            notes = "推荐页图片，按点赞量排序",
            produces = "application/json"
    )
    @RequestMapping(value = "/pictureUsersList",method = RequestMethod.GET)
    public Object pictureUsersList(HttpServletRequest req, HttpSession session) {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {
            //得到所有的图片信息列表
            List<Picture> pictureList = pictureMapper.selectAll();
            //得到所有的图片id以及其点赞数
            List<LikeNum> likeNumList = likePictureMapper.getAllLikeNum();
            //将得到的图片以及点赞数转换成对应的字典<pictureId, likeNum>
            Map<Integer,Integer> likeNumDict = new HashMap<>();
            for(LikeNum item: likeNumList){
                likeNumDict.put(item.getPictureId(),item.getLikeNum());
            }
            //遍历图片信息列表,将likeNum放入图片信息中
            for (Picture item: pictureList){
                JSONObject tempJsonObject = new JSONObject();
                tempJsonObject = JSONObject.parseObject(JSONObject.toJSONString(item));
                tempJsonObject.put("likeNum",likeNumDict.get(tempJsonObject.getInteger("pictureId")));
                tempJsonObject.put("userId",havePictureMapper.selectUserIdByPictureId(tempJsonObject.getInteger("pictureId")));
                tempJsonObject.put("userName",userMapper.selectByPrimaryKey(tempJsonObject.getInteger("userId")).getUserName());
                tempJsonObject.put("headImg",userMapper.selectByPrimaryKey(tempJsonObject.getInteger("userId")).getHeadImg());
                jsonArray.add(tempJsonObject);
            }
            //根据jsonArray中的likeNum从大到小排序
            jsonArray.sort(Comparator.comparing(obj -> ((JSONObject) obj).getInteger("likeNum")).reversed());
            jsonObject.put("message","success");
            jsonObject.put("result", jsonArray);
            return jsonObject;
        }catch (Exception e){
            return jsonObject.put("message","error");
        }
    }


    @Configuration
    public class MyPicConfig implements WebMvcConfigurer {
        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            registry.addResourceHandler("/pictures/**").addResourceLocations("file:C:/Users/10638/Desktop/SoftwareProject/Service/SE_image_share/pictures/");
//            registry.addResourceHandler("/pictures/**").addResourceLocations("file:E:/大三上/软件工程/SE project/SE-project/SE_image_share/pictures/");
        }
    }
}
