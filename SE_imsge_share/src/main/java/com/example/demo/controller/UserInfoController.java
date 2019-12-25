package com.example.demo.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;

import java.sql.Date;

import com.example.demo.service.UserService;
import com.example.demo.entity.User;

/**
 * @program: SE_imsge_share
 * @description: 用户控制类，编辑个人信息，查看个人信息
 * @author: Xuefei Lv
 * @create: 2019/12/24
 **/

@RestController
@Controller
public class UserInfoController {
    @Autowired
    private UserService userService;

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
        } catch (Exception e) {
            jsonObject.put("message", "数据库错误");
        }
        return jsonObject;
    }


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


    @ResponseBody
    @RequestMapping(value = "/updateInfo", method = RequestMethod.POST)
    public Object updateInfo(HttpServletRequest req) {
        JSONObject jsonObject = new JSONObject();
        String userId = req.getParameter("uid").trim();
        String userName = req.getParameter("username").trim();
        String sex = req.getParameter("sex").trim();
        Date birthday = null;
        if (!req.getParameter("birthday").trim().equals("")) {
            birthday = Date.valueOf(req.getParameter("birthday").trim());
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
