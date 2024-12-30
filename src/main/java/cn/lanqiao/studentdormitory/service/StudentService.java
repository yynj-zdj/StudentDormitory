package cn.lanqiao.studentdormitory.service;

import cn.lanqiao.studentdormitory.pojo.Student;

import java.util.List;

public interface StudentService {
    /**
     * 查询功能
     */
    List<Student> selectAll();
}
