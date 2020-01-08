package com.example.demo.controller;

import com.alibaba.fastjson.JSONArray;
import com.example.demo.dao.*;
import com.example.demo.entity.*;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.service.PictureService;
import com.example.demo.utils.ImageUtil;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.demo.utils.baiduUtils.Base64Util;
import com.example.demo.utils.baiduUtils.HttpUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
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
    @RequestMapping(value="/api/typeSearch",method= RequestMethod.GET)
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
    @RequestMapping(value="/api/keywordSearch",method= RequestMethod.GET)
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
    @RequestMapping(value = "/api/pictureUsersList",method = RequestMethod.GET)
    public Object pictureUsersList(HttpServletRequest req, HttpSession session) {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {
            //得到所有的图片信息列表
            List<Picture> pictureList = pictureMapper.selectAll();
            //得到所有的图片id以及其点赞数
            List<LikeNum> likeNumList = pictureService.getAllLikeNum();
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
                if(likeNumDict.get(tempJsonObject.getInteger("pictureId")) == null){
                    tempJsonObject.put("likeNum",0);
                }else {
                    tempJsonObject.put("likeNum", likeNumDict.get(tempJsonObject.getInteger("pictureId")));
                }
                tempJsonObject.put("userId",pictureService.selectUserIdByPictureId(tempJsonObject.getInteger("pictureId")));
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
    @RequestMapping(value="/api/myPictures",method= RequestMethod.GET)
    public Object myPictures(HttpServletRequest req, HttpSession session) {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        Integer userId = Integer.valueOf(req.getParameter("uid").trim());
        try {
            List<Picture> pictures = pictureService.getPicturesByUserId(userId);
            for (Picture picture : pictures) {
                JSONObject tempJsonObject = new JSONObject();
                tempJsonObject = JSONObject.parseObject(JSONObject.toJSONString(picture));
                tempJsonObject.put("likeNum", pictureService.getLikeCountById(picture.getPictureId()));
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
    @RequestMapping(value="/api/pictureDetail",method= RequestMethod.GET)
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
    @RequestMapping(value="/api/pictureUpload",method= RequestMethod.POST)
    public Object pictureUpload(HttpServletRequest request , HttpSession session) throws IOException {
        MultipartHttpServletRequest params=((MultipartHttpServletRequest) request);
        List<MultipartFile> file = ((MultipartHttpServletRequest) request).getFiles("img");

        JSONObject jsonObject = new JSONObject();
        try {
            //解析从前端获取的数据
            int uid = Integer.parseInt(params.getParameter("uid"));
            String width = params.getParameter("width");
            String height = params.getParameter("height");
            String description = params.getParameter("description");
            String type_name = params.getParameter("type_name");
            //根据uid拼接图片存放目录
            String dirPath = System.getProperty("user.dir") + System.getProperty("file.separator") + "pictures" + System.getProperty("file.separator") + uid + System.getProperty("file.separator") + "pictures" + System.getProperty("file.separator");
            //获取图片文件名
            String fileName = file.get(0).getOriginalFilename();
            assert fileName != null;
            //获取图片文件名后缀
            String fileSuffix = fileName.substring(fileName.lastIndexOf("."));
            //将图片信息插入数据库
            Picture picture = new Picture();
            picture.setTypeName(type_name);
            picture.setDescription(description);
            picture.setHeight(height);
            picture.setWidth(width);
            pictureMapper.insertSelective(picture);
            //获取自增后的pid
            int pid = picture.getPictureId();

            String result = ImageUtil.addImgToBaiDu(file.get(0),pid);

            //将uid与pid插入表HavePicture
            HavePicture havePicture =new HavePicture();
            havePicture.setPictureId(pid);
            havePicture.setUserId(uid);
            havePictureMapper.insertSelective(havePicture);
            //根据自增后的pid拼接出图片的position，更新图片信息
            picture.setPosition("pictures/" + uid + "/pictures/" + pid + fileSuffix);
            pictureMapper.updateByPrimaryKeySelective(picture);
            String localFileName =  pid +  fileSuffix;
            String filePath = dirPath + localFileName;
            //创建图片文件并保存到文件系统
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


    @ApiOperation(
            value = "用户删除图片",
            notes = "根据图片pid将用户uid的图片删除",
            produces = "application/json"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户ID", required = true, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "pid", value = "图片ID", required = true, dataType = "Integer", paramType = "query")
    })
    @RequestMapping(value="/api/deletePicture",method= RequestMethod.DELETE)
    public Object deletePicture(HttpServletRequest req, HttpSession session) {
        JSONObject jsonObject = new JSONObject();
        Integer userId = Integer.valueOf(req.getParameter("uid").trim());
        Integer pictureId = Integer.valueOf(req.getParameter("pid").trim());
        HavePicture havePicture = new HavePicture();
        havePicture.setPictureId(pictureId);
        havePicture.setUserId(userId);
        try {
            boolean res = pictureService.deletePicture(havePicture);
            if (res) {
                jsonObject.put("message", "删除成功");
            } else {
                jsonObject.put("message", "删除失败");
            }
        } catch (Exception e) {
            jsonObject.put("message", "数据库错误");
        }
        return jsonObject;
    }

    @ApiOperation(
            value = "以图搜图",
            notes = "以图搜图"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "img", value = "图片文件", required = true, dataType = "MultipartFile", paramType = "form")
    })
    @RequestMapping(value="/api/searchImgByImg",method= RequestMethod.POST)
    public Object searchImgByImg(HttpServletRequest req, HttpSession session) throws IOException {
        List<MultipartFile> file = ((MultipartHttpServletRequest) req).getFiles("img");
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        try {
            //在百度图像搜索库中搜索图像
            String result = ImageUtil.searchImgInBaiDu(file.get(0));
            //将获得的结果转化为jSONObject
            JSONObject tempJsonObject = JSONObject.parseObject(result);
            JSONArray tempJsonArray = JSONObject.parseArray(tempJsonObject.getString("result"));

            for(Object item: tempJsonArray){
                JSONObject temp = JSONObject.parseObject(item.toString());
                String brief = temp.getString("brief");
                String score = temp.getString("score");
                try {
                    if (pictureService.isNum(brief)) {
                        Picture tempPicture = pictureMapper.selectByPrimaryKey(Integer.valueOf(brief));
                        JSONObject tempPictureJson = new JSONObject();
                        tempPictureJson.put("pictureId", tempPicture.getPictureId());
                        tempPictureJson.put("position", tempPicture.getPosition());
                        tempPictureJson.put("width", tempPicture.getWidth());
                        tempPictureJson.put("height", tempPicture.getHeight());
                        tempPictureJson.put("typeName", tempPicture.getTypeName());
                        tempPictureJson.put("description", tempPicture.getDescription());
                        tempPictureJson.put("score", score);
                        jsonArray.add(tempPictureJson);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            jsonObject.put("result",jsonArray);
//            System.out.println(result);
            jsonObject.put("message","上传成功");
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("message","error");
        }
        return jsonObject;
    }

    @Configuration
    public class MyPicConfig implements WebMvcConfigurer {
        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
          //  registry.addResourceHandler("/pictures/**").addResourceLocations("file:C:/Users/10638/Desktop/SoftwareProject/Service/SE_image_share/pictures/");
           //registry.addResourceHandler("/pictures/**").addResourceLocations("file:D:/GitHub/SE-project/SE_image_share/pictures/");
//           registry.addResourceHandler("/pictures/**").addResourceLocations("file:E:\\大三上\\软件工程\\SE project\\SE-project\\SE_image_share\\pictures/");
            registry.addResourceHandler("/pictures/**").addResourceLocations("file:/usr/local/SE_project/pictures/");
        }
    }
}
