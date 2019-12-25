package com.example.demo.controller;

import com.alibaba.fastjson.JSONArray;
import com.example.demo.dao.PictureMapper;
import com.example.demo.entity.Picture;
import com.example.demo.entity.Type;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
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
import java.util.List;

/**
 * @program: SE_imsge_share
 * @description: 图片控制类，查看图片信息，查看图片列表
 * @author: Xuefei Lv
 * @create: 2019/12/24
 **/

@Api(value = "消息",description = "消息操作 API", position = 100, protocols = "http")
@RestController
@Controller
public class PictureController {
@Autowired
private PictureMapper pictureMapper;
private Type type = new Type();

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

    @RequestMapping(value="/keywordSearch",method= RequestMethod.POST)
    public Object keywordSearch(HttpServletRequest req, HttpSession session) {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {
            String keyWords = req.getParameter("keyword");
            List<Picture> pictureList =  pictureMapper.selectByKeyWords(keyWords);
            jsonArray = JSONArray.parseArray(JSONObject.toJSONString(pictureList));
            jsonObject.put("message","success");
            jsonObject.put("result", jsonArray);
        }catch (Exception e){
            System.out.println(e);
            jsonObject.put("message","error");
        }
        return jsonObject;
    }




    @Configuration
    public class MyPicConfig implements WebMvcConfigurer {
        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            registry.addResourceHandler("/pictures/**").addResourceLocations("file:E:/大三上/软件工程/SE project/SE-project/SE_image_share/pictures/");
        }
    }
}
