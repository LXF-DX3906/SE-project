package com.example.demo.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONAware;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.entity.User;
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
import java.util.List;

/**
 * @program: SE_imsge_share
 * @description: 用户互动类，关注用户，浏览用户主页
 * @author: Rui Xiao
 * @create: 2019/12/24
 **/

@Api(tags = {"用户间交互控制类"})
@RestController
@Controller
public class UserInteractionController {
    @Autowired
    UserService userService;

    @ApiOperation(
            value = "获得当前用户关注的用户",
            notes = "按当前用户uid获得",
            produces = "application/json"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "当前用户ID", required = true, dataType = "Integer", paramType = "query")
    })
    @RequestMapping(value="/focus",method= RequestMethod.POST)
    public Object gatFocus(HttpServletRequest req, HttpSession session) {
        Integer userId = Integer.valueOf(req.getParameter("uid"));
        JSONObject jsonObject = new JSONObject();
        try {
            List<User> users = userService.getFollows(userId);
            JSONArray jsonArray = JSONArray.parseArray(JSONObject.toJSONString(users));
            jsonObject.put("message","获取成功");
            jsonObject.put("result", jsonArray);
        } catch (Exception e) {
            jsonObject.put("message", "数据库错误");
        }
        return jsonObject;
    }

    @ApiOperation(
            value = "获得当前用户的粉丝",
            notes = "按当前用户uid获得",
            produces = "application/json"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "当前用户ID", required = true, dataType = "Integer", paramType = "query")
    })
    @RequestMapping(value="/fans",method= RequestMethod.POST)
    public Object gatFans(HttpServletRequest req, HttpSession session) {
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
