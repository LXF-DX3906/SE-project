package com.example.demo.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;

import com.example.demo.service.UserService;
import com.example.demo.entity.User;

/**
 * @program: SE_imsge_share
 * @description: 用户信息控制类，编辑个人信息，查看个人信息
 * @author: Rui Xiao
 * @create: 2019/12/24
 **/

@Api(tags = {"用户信息控制类"})
@RestController
@Controller
public class UserInfoController {
    @Autowired
    private UserService userService;

    @ApiOperation(
            value = "获取用户信息",
            notes = "根据用户的userId获取用户信息",
            produces = "application/json"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户id", required = true, dataType = "Integer", paramType = "query")
    })
    @ResponseBody
    @RequestMapping(value = "/basicInfo", method = RequestMethod.POST)
    public Object userInfo(HttpServletRequest req) {
        JSONObject jsonObject = new JSONObject();
        String userId = req.getParameter("uid").trim();
        if (userId.equals("") || userId == null){
            jsonObject.put("message", "userId为空");
            return jsonObject;
        }
        try {
            User user = userService.getUserById(Integer.parseInt(userId));
            Integer followsNum = userService.getFollowsNum(Integer.parseInt(userId));
            Integer fansNum = userService.getFansNum(Integer.parseInt(userId));
            jsonObject.put("message", "登陆成功");
            jsonObject.put("username", user.getUserName());
            jsonObject.put("phone", user.getPhone());
            jsonObject.put("email", user.getEmail());
            jsonObject.put("head_image", user.getHeadImg());
            jsonObject.put("sex", Integer.parseInt(user.getSex() == null ? "0" : user.getSex()));
            jsonObject.put("birthday", user.getBirthday());
            jsonObject.put("introduce", user.getIntroduction());
            jsonObject.put("province", user.getProvince());
            jsonObject.put("city", user.getCity());
            jsonObject.put("follow", followsNum);
            jsonObject.put("fans", fansNum);
        } catch (Exception e) {
            jsonObject.put("message", "数据库错误");
        }
        return jsonObject;
    }

    @ApiOperation(
            value = "编辑电话",
            notes = "根据用户新编辑的phone更新电话",
            produces = "application/json"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户id", required = true, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "phone", value = "新编辑的电话", required = true, dataType = "String", paramType = "query")
    })
    @ResponseBody
    @RequestMapping(value = "/updatePhone", method = RequestMethod.POST)
    public Object updatePhone(HttpServletRequest req) {
        JSONObject jsonObject = new JSONObject();
        String userId = req.getParameter("uid").trim();
        String phone = req.getParameter("phone").trim();
        Integer existId = userService.getIdByPhone(phone);
        if (userId.equals("")
                || userId == null
                || (existId != null && !Integer.valueOf(userId).equals(existId))
                || phone.equals("")
                || phone == null) {
            jsonObject.put("message", "电话已存在");
            return jsonObject;
        } else if (Integer.valueOf(userId).equals(existId)) {
            jsonObject.put("message", "电话未修改");
            return jsonObject;
        }
        User user = new User();
        user.setUserId(Integer.parseInt(userId));
        user.setPhone(phone);
        try {
            boolean res = userService.updateUserMsg(user);
            if (res) {
                jsonObject.put("message", "编辑成功");
            } else {
                jsonObject.put("message", "编辑内容存在错误");
            }
        } catch (Exception e) {
            jsonObject.put("message", "数据库错误");
        }
        return jsonObject;
    }

    @ApiOperation(
            value = "编辑邮箱",
            notes = "根据用户新编辑的email更新邮箱",
            produces = "application/json"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户id", required = true, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "email", value = "新编辑的邮箱", required = true, dataType = "String", paramType = "query")
    })
    @ResponseBody
    @RequestMapping(value = "/updateEmail", method = RequestMethod.POST)
    public Object updateEmail(HttpServletRequest req) {
        JSONObject jsonObject = new JSONObject();
        String userId = req.getParameter("uid").trim();
        String email = req.getParameter("email").trim();
        Integer existId = userService.getIdByEmail(email);
        if (userId.equals("")
                || userId == null
                || (existId != null && !Integer.valueOf(userId).equals(existId))
                || email.equals("")
                || email == null) {
            jsonObject.put("message", "邮箱已存在");
            return jsonObject;
        } else if (Integer.valueOf(userId).equals(existId)) {
            jsonObject.put("message", "邮箱未改变");
            return jsonObject;
        }
        User user = new User();
        user.setUserId(Integer.parseInt(userId));
        user.setEmail(email);
        try {
            boolean res = userService.updateUserMsg(user);
            if (res) {
                jsonObject.put("message", "编辑成功");
            } else {
                jsonObject.put("message", "编辑内容存在错误");
            }
        } catch (Exception e) {
            jsonObject.put("message", "数据库错误");
        }
        return jsonObject;
    }

    @ApiOperation(
            value = "编辑密码",
            notes = "根据用户新编辑的password更新密码",
            produces = "application/json"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户id", required = true, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "password", value = "新编辑的密码", required = true, dataType = "String", paramType = "query")
    })
    @ResponseBody
    @RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
    public Object updatePassword(HttpServletRequest req) {
        JSONObject jsonObject = new JSONObject();
        String userId = req.getParameter("uid").trim();
        String pwd = req.getParameter("password").trim();
        if (userId.equals("") || userId == null || pwd.equals("") || pwd == null) {
            jsonObject.put("message", "未传参");
            return jsonObject;
        }
        User user = new User();
        user.setUserId(Integer.parseInt(userId));
        user.setPwd(pwd);
        try {
            boolean res = userService.updateUserMsg(user);
            if (res) {
                jsonObject.put("message", "编辑成功");
            } else {
                jsonObject.put("message", "编辑内容存在错误");
            }
        } catch (Exception e) {
            jsonObject.put("message", "数据库错误");
        }
        return jsonObject;
    }

    @ApiOperation(
            value = "编辑其他信息",
            notes = "根据用户新编辑的各类其他信息更新",
            produces = "application/json"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户id", required = true, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "username", value = "用户昵称", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "sex", value = "用户性别", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "birthday", value = "用户生日", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "introduce", value = "用户介绍", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "province", value = "用户所在省", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "city", value = "用户所在城市", required = false, dataType = "String", paramType = "query"),
    })
    @ResponseBody
    @RequestMapping(value = "/updateInfo", method = RequestMethod.POST)
    public Object updateInfo(HttpServletRequest req) {
        JSONObject jsonObject = new JSONObject();
        String userId = req.getParameter("uid").trim();
        String userName = req.getParameter("username").trim();
        String sex = req.getParameter("sex").trim();
        Date birthday = null;
        if (!req.getParameter("birthday").trim().equals("")) {
            birthday = Date.valueOf(req.getParameter("birthday").trim().substring(0, 10));
        }
        String introduction = req.getParameter("introduce").trim();
        String province = req.getParameter("province").trim();
        String city = req.getParameter("city").trim();
        Integer existId = (userName == null) ? null : userService.userNameVerify(userName);
        if (userId.equals("")
                || userId == null
                || (existId != null && !Integer.valueOf(userId).equals(existId))) {
            jsonObject.put("message", "用户名已存在");
            return jsonObject;
        }
        User user = new User();
        user.setUserId(Integer.valueOf(userId));
        user.setUserName(userName);
        user.setSex(sex);
        user.setBirthday(birthday);
        user.setIntroduction(introduction);
        user.setProvince(province);
        user.setCity(city);
        try {
            boolean res = userService.updateUserMsg(user);
            if (res) {
                jsonObject.put("message", "编辑成功");
            } else {
                jsonObject.put("message", "编辑内容存在错误");
            }
        } catch (Exception e) {
            jsonObject.put("message", "数据库错误");
        }
        return jsonObject;
    }
}
