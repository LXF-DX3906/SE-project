package com.example.demo.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
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

@RestController
@Controller
public class UsersInteractWithPictureController {
    @Autowired
    UserService userService;












    /**
     * 临时的，没什么用，前端这部分都是没写完的，干脆不要了
     **/
    @RequestMapping(value="/pushList",method= RequestMethod.POST)
    public Object pushList(HttpServletRequest req, HttpSession session) {
        Integer userId = Integer.valueOf(req.getParameter("uid"));
        JSONObject jsonObject = new JSONObject();
        try {
            List<User> users = userService.getFans(userId);
            JSONArray jsonArray = JSONArray.parseArray(JSONObject.toJSONString(users));
            jsonObject.put("message","获取成功");
            jsonObject.put("result", jsonArray);
        } catch (Exception e) {
            jsonObject.put("message", "数据库错误");
        }
        return jsonObject;
    }
}
