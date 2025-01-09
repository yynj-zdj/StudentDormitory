package cn.lanqiao.studentdormitory.controller;


import cn.lanqiao.studentdormitory.common.Result;
import cn.lanqiao.studentdormitory.enums.ResponseCodeEnum;
import cn.lanqiao.studentdormitory.pojo.Admin;
import cn.lanqiao.studentdormitory.service.LoginService;
import cn.lanqiao.studentdormitory.utils.CaptchaUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


@Controller
public class LoginController {
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private LoginService loginService;

    @GetMapping({"/login"})
    public String toLogin() {
        return "login";
    }

    @PostMapping("/login")
    @ResponseBody
    public Result doLogin(@RequestBody Admin admin, @RequestParam String captcha, HttpSession session) {
        try {
            // 打印接收到的参数
            log.info("接收到登录请求: admin={}, captcha={}", admin, captcha);

            // 参数校验
            if (admin == null || StringUtils.isEmpty(admin.getName()) 
                || StringUtils.isEmpty(admin.getPassword()) 
                || StringUtils.isEmpty(captcha)
                || admin.getType() <= 0) {
                log.warn("参数校验失败");
                return Result.error(ResponseCodeEnum.PARAM_ERROR);
            }

            // 验证码校验
            String sessionCaptcha = (String) session.getAttribute("captcha");
            log.info("验证码校验: 用户输入={}, 会话中的验证码={}", captcha, sessionCaptcha);
            if (sessionCaptcha == null || !captcha.equalsIgnoreCase(sessionCaptcha)) {
                log.warn("验证码校验失败");
                return Result.error(ResponseCodeEnum.CPACHA_INVALID);
            }

            // 查询用户并验证权限
            Admin loginUser = loginService.login(admin.getName(), admin.getPassword(), admin.getType());
            
            if (loginUser == null) {
                return Result.error(ResponseCodeEnum.PASSWORD_ERROR);
            }
            
            // 设置用户权限信息
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("userId", loginUser.getId());
            userInfo.put("userName", loginUser.getName());
            userInfo.put("userType", loginUser.getType());
            
            // 根据不同用户类型设置权限
            Set<String> permissions = new HashSet<>();
            switch(loginUser.getType()) {
                case 1: // 管理员
                    permissions.addAll(Arrays.asList(
                        "student:*", "building:*", "dormitory:*",
                        "live:*", "dormitoryManager:*", "admin:*"
                    ));
                    break;
                case 2: // 宿管
                    permissions.addAll(Arrays.asList(
                        "student:view", "student:edit",
                        "dormitory:view", "dormitory:edit",
                        "live:view"
                    ));
                    break;
                case 3: // 学生
                    permissions.addAll(Arrays.asList(
                        "student:view", "dormitory:view", "live:view"
                    ));
                    break;
            }
            userInfo.put("permissions", permissions);
            
            // 存入session
            session.setAttribute("loginUser", userInfo);
            
            return Result.success("0000", "登录成功","/index");
        } catch (Exception e) {
            log.error("登录异常", e);
            return Result.error(ResponseCodeEnum.SYSTEM_ERROR);
        }
    }

    @GetMapping("/index")
    public String toIndex() {
        return "redirect:/index.html";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/captcha")
    @ResponseBody
    public void getCaptcha(HttpServletResponse response, HttpSession session) throws IOException {
        // 生成验证码
        String captcha = CaptchaUtil.generateCaptcha(4);
        // 将验证码存入session
        session.setAttribute("captcha", captcha);
        
        // 生成图片
        BufferedImage image = CaptchaUtil.createCaptchaImage(captcha);
        
        // 输出图片
        response.setContentType("image/jpeg");
        ImageIO.write(image, "JPEG", response.getOutputStream());
    }
}