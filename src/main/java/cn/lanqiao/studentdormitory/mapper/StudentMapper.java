package cn.lanqiao.studentdormitory.mapper;

import cn.lanqiao.studentdormitory.pojo.Student;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface StudentMapper {
    /**
     * 查询功能
     */
    @Select("select * from student")
    List<Student> selectAll();
}
