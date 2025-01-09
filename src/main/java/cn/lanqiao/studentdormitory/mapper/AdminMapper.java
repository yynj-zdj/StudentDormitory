package cn.lanqiao.studentdormitory.mapper;

import cn.lanqiao.studentdormitory.pojo.Admin;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AdminMapper extends BaseMapper<Admin> {
    
    @Select("SELECT id, name, password, 1 as type FROM admin WHERE name = #{name} AND password = #{password}")
    Admin selectAdmin(String name, String password);
    
    @Select("SELECT id, name, password, 2 as type FROM dormitory_manager WHERE name = #{name} AND password = #{password}")
    Admin selectDormManager(String name, String password);
    
    @Select("SELECT id, name, password, 3 as type FROM student WHERE name = #{name} AND password = #{password}")
    Admin selectStudent(String name, String password);
} 