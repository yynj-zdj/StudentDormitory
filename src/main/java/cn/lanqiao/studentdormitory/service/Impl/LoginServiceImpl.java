package cn.lanqiao.studentdormitory.service.Impl;

import cn.lanqiao.studentdormitory.mapper.AdminMapper;
import cn.lanqiao.studentdormitory.pojo.Admin;
import cn.lanqiao.studentdormitory.service.LoginService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl extends ServiceImpl<AdminMapper, Admin> implements LoginService {
    
    @Override
    public Admin login(String name, String password, Integer type) {
        switch(type) {
            case 1: // 管理员
                return baseMapper.selectAdmin(name, password);
            case 2: // 宿管
                return baseMapper.selectDormManager(name, password);
            case 3: // 学生
                return baseMapper.selectStudent(name, password);
            default:
                return null;
        }
    }
}
