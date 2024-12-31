package cn.lanqiao.studentdormitory.controller;

import cn.lanqiao.studentdormitory.pojo.Student;
import cn.lanqiao.studentdormitory.service.Impl.StudentServiceImpl;
import cn.lanqiao.studentdormitory.service.StudentService;
import cn.lanqiao.studentdormitory.utils.ResponseUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@RequestMapping("/Student")
public class StudentController {
    @Autowired
    private StudentServiceImpl studentService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    //JSON序列化工具
    @RequestMapping("/select")
    public ResponseUtils<List<Student>> select(){
        try {
            //因为前端并没有携带参数，我们可以直接操作数据库查询出相关的数据
            List<Student> students = studentService.selectAll();
            if (students ==null){
                //查询为空(失败)
                return new ResponseUtils(500,"查询失败");
            }else {
                //查询成功
                return new ResponseUtils(200,"查询成功",students);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseUtils(400,"管理员查询异常");
        }
    }
}
