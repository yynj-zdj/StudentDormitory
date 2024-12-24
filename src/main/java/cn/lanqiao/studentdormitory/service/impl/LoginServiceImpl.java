package cn.lanqiao.studentdormitory.service.impl;

import cn.lanqiao.studentdormitory.mapper.LoginMapper;
import cn.lanqiao.studentdormitory.pojo.Admin;
import cn.lanqiao.studentdormitory.service.LoginService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;




@Service
public class LoginServiceImpl extends ServiceImpl<LoginMapper, Admin> implements LoginService{

}
