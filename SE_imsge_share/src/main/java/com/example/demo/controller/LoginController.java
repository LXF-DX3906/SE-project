package com.example.demo.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.example.demo.service.UserService;
import com.example.demo.entity.Response;
import com.example.demo.entity.User;

@RestController
@Controller
public class LoginController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/phoneLogin", method = RequestMethod.POST)
    public Object phoneLoginVerify(HttpServletRequest req, HttpSession session) throws IOException {
        JSONObject jsonObject = new JSONObject();
        //Response<Integer> response = new Response<>();
        String phone = req.getParameter("phone");
        String pwd = req.getParameter("password");
        if (phone.equals("") || phone == null || pwd.equals("") || pwd == null) {
            jsonObject.put("message", "账号或密码未传入");
            return jsonObject;
        }
        try {
            Integer id = userService.phoneVerify(phone, pwd);
            if (id != null) {
                session.setAttribute("phone", phone);
                jsonObject.put("message", "登录成功");
                jsonObject.put("uid", id);
//                response.setMessage("登录成功");
//                List<Integer> temp = new ArrayList<>();
//                temp.add(id);
//                response.setResult(temp);
            } else {
                jsonObject.put("message", "用户名或密码错误");
//                response.setMessage("用户名或密码错误");
            }
        } catch (Exception e) {
            jsonObject.put("message", "数据库错误");
//            response.setMessage("数据库错误");
        }
        return jsonObject;
//        return response;
    }


    @ResponseBody
    @RequestMapping(value = "/emailLogin", method = RequestMethod.POST)
    public Object emailLoginVerify(HttpServletRequest req, HttpSession session) throws IOException {
        JSONObject jsonObject = new JSONObject();
        //Response<Integer> response = new Response<>();
        String email = req.getParameter("email");
        String pwd = req.getParameter("password");
        if (email.equals("") || email == null || pwd.equals("") || pwd == null) {
            jsonObject.put("message", "账号或密码未传入");
            return jsonObject;
        }
        try {
            Integer id = userService.emailVerify(email, pwd);
            if (id != null) {
                session.setAttribute("email", email);
                jsonObject.put("message", "登录成功");
                jsonObject.put("uid", id);
//                response.setMessage("登录成功");
//                List<Integer> temp = new ArrayList<>();
//                temp.add(id);
//                response.setResult(temp);
            } else {
                jsonObject.put("message", "用户名或密码错误");
//                response.setMessage("用户名或密码错误");
            }
        } catch (Exception e) {
            jsonObject.put("message", "数据库错误");
//            response.setMessage("数据库错误");
        }
        return jsonObject;
//        return response;
    }


    @ResponseBody
    @RequestMapping(value = "/phoneRegister", method = RequestMethod.POST)
    public Object phoneRegister(HttpServletRequest req) {
        JSONObject jsonObject = new JSONObject();

        String phone = req.getParameter("phone").trim();
        String pwd = req.getParameter("password").trim();
        if (phone.equals("") || phone == null || pwd.equals("") || pwd == null) {
            jsonObject.put("message", "账号或密码未传入");
            return jsonObject;
        } else if (userService.getIdByPhone(phone) != null) {
            jsonObject.put("message", "该手机号已注册");
            return jsonObject;
        }
        User user = new User();
        user.setPhone(phone);
        user.setPwd(pwd);
        try {
            Integer id = userService.userRegister(user);
            jsonObject.put("message", "注册成功");
            jsonObject.put("uid", id);
        } catch (Exception e) {
            jsonObject.put("message", "数据库错误");
        }
        return jsonObject;
    }


    @ResponseBody
    @RequestMapping(value = "/emailRegister", method = RequestMethod.POST)
    public Object emailRegister(HttpServletRequest req) {
        JSONObject jsonObject = new JSONObject();

        String email = req.getParameter("email").trim();
        String pwd = req.getParameter("password").trim();
        if (email.equals("") || email == null || pwd.equals("") || pwd == null) {
            jsonObject.put("message", "账号或密码未传入");
            return jsonObject;
        } else if (userService.getIdByEmail(email) != null) {
            jsonObject.put("message", "该邮箱已注册");
            return jsonObject;
        }
        User user = new User();
        user.setEmail(email);
        user.setPwd(pwd);
        try {
            Integer id = userService.userRegister(user);
            jsonObject.put("message", "注册成功");
            jsonObject.put("uid", id);
        } catch (Exception e) {
            jsonObject.put("message", "数据库错误");
        }
        return jsonObject;
    }

}
