package com.example.demo.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.example.demo.dao.*;
import com.example.demo.entity.*;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.service.PictureService;
import io.swagger.annotations.*;
import org.apache.ibatis.annotations.Lang;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
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
    @Autowired
    private PictureService pictureService;
    @Autowired
    private CommentMapper commentMapper;

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
                if (!likeNumDict.containsKey(item.getPictureId())) {
                    likeNumDict.put(item.getPictureId(), 0);
                }
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

    @ApiOperation(
            value = "获得用户的图片",
            notes = "按用户uid获得",
            produces = "application/json"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户ID", required = true, dataType = "Integer", paramType = "query")
    })
    @RequestMapping(value="/myPictures",method= RequestMethod.POST)
    public Object myPictures(HttpServletRequest req, HttpSession session) {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        Integer userId = Integer.valueOf(req.getParameter("uid").trim());
        try {
            List<Picture> pictures = pictureService.getPicturesByUserId(userId);
            for (Picture picture : pictures) {
                JSONObject tempJsonObject = new JSONObject();
                tempJsonObject = JSONObject.parseObject(JSONObject.toJSONString(picture));
                tempJsonObject.put("likeNum", likePictureMapper.getLikeCountById(picture.getPictureId()));
                jsonArray.add(tempJsonObject);
            }
            jsonObject.put("message", "获取成功");
            jsonObject.put("result", jsonArray);
        } catch (Exception e) {
            jsonObject.put("message", "数据库错误");
        }
        return jsonObject;
    }

    @ApiOperation(
            value = "按图片id搜索",
            notes = "按图片的id搜索",
            produces = "application/json"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pid", value = "图片id", required = true, dataType = "Integer", paramType = "query")
    })
    @RequestMapping(value="/pictureDetail",method= RequestMethod.POST)
    public Object pictureIdSearch(HttpServletRequest req, HttpSession session) {
        JSONObject jsonObject = new JSONObject();
        JSONObject comment = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        try {
            String userId = req.getParameter("pid");
            int a = Integer.parseInt(userId);
            List<Comment> commentList = commentMapper.selectByPictureId(Integer.parseInt(userId));
            for(Comment item : commentList){
                JSONObject tempJsonObject = new JSONObject();
                tempJsonObject.put("from_username", userMapper.selectByPrimaryKey(item.getUserId()).getUserName());
                tempJsonObject.put("content", item.getContent());
                tempJsonObject.put("from_head_image", userMapper.selectByPrimaryKey(item.getUserId()).getHeadImg());
                jsonArray.add(tempJsonObject);
            }
            comment.put("comment", jsonArray);
            jsonObject.put("result", comment);
            jsonObject.put("message","success");
        }catch (Exception e){
            System.out.println(e);
            jsonObject.put("message","数据库错误");
        }
        return jsonObject;
    }

    @ApiOperation(
            value = "上传图片",
            notes = "从前端获取图片文件与图片尺寸，写入到数据库与文件系统"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户ID", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "width", value = "图片宽度", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "height", value = "图片高度", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "description", value = "图片描述", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "type_name", value = "图片类型", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "img", value = "图片文件", required = true, dataType = "MultipartFile", paramType = "form")
    })
    @RequestMapping(value="/pictureUpload",method= RequestMethod.POST)
    public Object pictureUpload(HttpServletRequest request , HttpSession session) throws IOException {
        MultipartHttpServletRequest params=((MultipartHttpServletRequest) request);
        List<MultipartFile> file = ((MultipartHttpServletRequest) request).getFiles("img");

        JSONObject jsonObject = new JSONObject();
        try {
            int uid = Integer.parseInt(params.getParameter("uid"));
            String width = params.getParameter("width");
            String height = params.getParameter("height");
            String description = params.getParameter("description");
            String type_name = params.getParameter("type_name");


            String dirPath = System.getProperty("user.dir") + System.getProperty("file.separator") + "pictures" + System.getProperty("file.separator") + uid + System.getProperty("file.separator") + "pictures" + System.getProperty("file.separator");
            String fileName = file.get(0).getOriginalFilename();
            assert fileName != null;
            String fileSuffix = fileName.substring(fileName.lastIndexOf("."));
            Picture picture = new Picture();
            picture.setTypeName(type_name);
            picture.setDescription(description);
            picture.setHeight(height);
            picture.setWidth(width);

            pictureMapper.insertSelective(picture);
            int pid = picture.getPictureId();
            picture.setPosition("pictures/" + uid + "/pictures/" + pid + fileSuffix);
            pictureMapper.updateByPrimaryKeySelective(picture);
            String localFileName =  pid +  fileSuffix;
            String filePath = dirPath + localFileName;
            File localFile = new File(filePath);
            File imagePath = new File(dirPath);
            if (!imagePath.exists()) {
                imagePath.mkdirs();
            }
            file.get(0).transferTo(localFile);
            jsonObject.put("message","添加成功");
        }catch (Exception e){
            jsonObject.put("message","error");
        }
        return jsonObject;
    }












    @Configuration
    public class MyPicConfig implements WebMvcConfigurer {
        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            registry.addResourceHandler("/pictures/**").addResourceLocations("file:C:/Users/10638/Desktop/SoftwareProject/Service/SE_image_share/pictures/");
//            registry.addResourceHandler("/pictures/**").addResourceLocations("file:D:/GitHub/SE-project/SE_image_share/pictures/");
        }
    }
}
