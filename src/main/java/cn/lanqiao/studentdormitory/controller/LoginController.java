package cn.lanqiao.studentdormitory.controller;


import cn.lanqiao.studentdormitory.pojo.Admin;
import cn.lanqiao.studentdormitory.service.LoginService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
public class LoginController {
    @Autowired
    private LoginService loginService;  // 注入用户服务

    // 跳转到登录页面
    @GetMapping("/login")
    public String toLogin() {
        return "login.html";
    }
    
    // 处理登录请求
    @RequestMapping("/toLogin")
    public String toLogin(Admin admin, HttpSession session, Model model){
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name",admin.getName());
        queryWrapper.eq("Password",admin.getPassword());
        queryWrapper.eq("state","1");
        //MyBatisPlus实现登录
        Admin one = loginService.getOne(queryWrapper);
        if (one !=null){
            //状态必须是1
            if (one.getState() == 1){
                session.setAttribute("admin",one);
                //登录成功
                return "index";
            }else {
                model.addAttribute("message","管理员账号已被禁用");
                return "login";
            }
        }else {
            model.addAttribute("message","账号或密码错误");
            return "login";
        }
    }
    
    // 退出登录
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
