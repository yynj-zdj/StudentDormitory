package cn.lanqiao.studentdormitory.service;

import cn.lanqiao.studentdormitory.pojo.Admin;
import com.baomidou.mybatisplus.extension.service.IService;

public interface LoginService extends IService<Admin> {
    Admin login(String name, String password, Integer type);
}
