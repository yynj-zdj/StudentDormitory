package cn.lanqiao.studentdormitory.service.Impl;

import cn.lanqiao.studentdormitory.mapper.StudentMapper;
import cn.lanqiao.studentdormitory.pojo.Student;
import cn.lanqiao.studentdormitory.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class StudentServiceImpl implements StudentService {
    //依赖注入
    @Autowired
    private StudentMapper studentsMapper;

    //注入redis以来
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public List<Student> selectAll() {
        List<Student> students = studentsMapper.selectAll();
        if (students != null) {
            return students;
        } else {
            return null;
        }
    }
}
