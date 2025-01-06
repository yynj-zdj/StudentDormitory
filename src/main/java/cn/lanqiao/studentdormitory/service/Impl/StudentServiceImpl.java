package cn.lanqiao.studentdormitory.service.impl;

import cn.lanqiao.studentdormitory.pojo.Student;
import cn.lanqiao.studentdormitory.mapper.StudentMapper;
import cn.lanqiao.studentdormitory.service.IStudentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author author
 * @since 2024-12-31
 */
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements IStudentService {

}
