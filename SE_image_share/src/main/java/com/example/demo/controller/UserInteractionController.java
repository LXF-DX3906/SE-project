package com.example.demo.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONAware;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.entity.Follow;
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
    @RequestMapping(value="/api/focus",method= RequestMethod.GET)
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
    @RequestMapping(value="/api/fans",method= RequestMethod.GET)
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

    @ApiOperation(
            value = "当前用户取消关注操作",
            notes = "按其他用户uuid取消对其关注",
            produces = "application/json"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "当前用户ID", required = true, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "uuid", value = "被取关用户ID", required = true, dataType = "Integer", paramType = "query")
    })
    @RequestMapping(value="/api/deleteFocus",method= RequestMethod.DELETE)
    public Object deleteFocus(HttpServletRequest req, HttpSession session) {
        Integer userId = Integer.valueOf(req.getParameter("uid"));
        Integer followingId = Integer.valueOf(req.getParameter("uuid"));
        JSONObject jsonObject = new JSONObject();
        Follow follow = new Follow();
        follow.setUserId(userId);
        follow.setFollowingId(followingId);
        try {
            boolean res = userService.deleteFollow(follow);
            if (res) {
                jsonObject.put("message", "取消关注成功");
            } else {
                jsonObject.put("message", "取消关注失败");
            }
        } catch (Exception e) {
            jsonObject.put("message", "数据库错误");
        }
        return jsonObject;
    }

    @ApiOperation(
            value = "当前用户关注其他用户",
            notes = "按其他用户uuid对其关注",
            produces = "application/json"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "当前用户ID", required = true, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "uuid", value = "被关注用户ID", required = true, dataType = "Integer", paramType = "query")
    })
    @RequestMapping(value="/api/addFocus",method= RequestMethod.POST)
    public Object addFocus(HttpServletRequest req, HttpSession session) {
        Integer userId = Integer.valueOf(req.getParameter("uid"));
        Integer followingId = Integer.valueOf(req.getParameter("uuid"));
        JSONObject jsonObject = new JSONObject();
        Follow follow = new Follow();
        follow.setUserId(userId);
        follow.setFollowingId(followingId);
        try {
            boolean res = userService.addFollow(follow);
            if (res) {
                jsonObject.put("message", "关注成功");
            } else {
                jsonObject.put("message", "已关注该用户");
            }
        } catch (Exception e) {
            jsonObject.put("message", "数据库错误");
        }
        return jsonObject;
    }
}
