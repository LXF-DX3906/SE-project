package com.example.demo.controller;

import com.alibaba.fastjson.JSONArray;
import com.example.demo.dao.PictureMapper;
import com.example.demo.entity.Picture;
import com.example.demo.entity.Type;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.*;
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

@Api(tags = {"图片控制类"})
@RestController
@Controller
public class PictureController {
    @Autowired
    private PictureMapper pictureMapper;
    private Type type = new Type();

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




    @Configuration
    public class MyPicConfig implements WebMvcConfigurer {
        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            registry.addResourceHandler("/pictures/**").addResourceLocations("file:C:\\Users\\10638\\Desktop\\SoftwareProject\\Service\\SE_image_share\\pictures\\");
        }
    }
}
