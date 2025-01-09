package cn.lanqiao.studentdormitory.controller;



import cn.lanqiao.studentdormitory.result.Resultx;
import cn.lanqiao.studentdormitory.pojo.Student;

import cn.lanqiao.studentdormitory.service.IStudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author author
 * @since 2024-12-31
 */
@RestController
@RequestMapping("/student")
@Slf4j
public class StudentController {

@Autowired
private IStudentService iStudentService;
    /***
     *数据查询
     */

    @GetMapping
    public Resultx<List<Student>> select() {
        log.info("查询数据");
        List<Student> list = iStudentService.list();
        return Resultx.success(list);
    }


    @GetMapping("/search/findByName")
    public Resultx<List<Student>> selectByName(@RequestParam("name") String findByName) {
        log.info("查询学生{}", findByName);

        // 查询学生列表
        List<Student> studentList = iStudentService.lambdaQuery().eq(Student::getName, findByName).list();

        // 检查列表是否为空
        if (studentList.isEmpty()) {
            return Resultx.error("未找到该学生");
        }

        return Resultx.success(studentList); // 返回整个列表
    }


    @GetMapping("/search/findBySn")
    public Resultx<List<Student>> selectBySn(@RequestParam("sn") String studentSn) {
        log.info("查询学生{}sn", studentSn);

        // 查询学生列表
        List<Student> studentList = iStudentService.lambdaQuery().eq(Student::getSn, studentSn).list();

        // 检查列表是否为空
        if (studentList.isEmpty()) {
            return Resultx.error("未找到该学生");
        }

        return Resultx.success(studentList); // 返回整个列表
    }


    /***
     * 新增学生
     */
    /***
     * 添加数据
     */
    @PostMapping
    public Resultx<String> save(@RequestBody Student student){
        log.info("添加学生数据参数{}", student);
        boolean result = iStudentService.save(student);
        if (result){
            return Resultx.success("添加成功");
        }else {
            return Resultx.error("添加失败");
        }

    }


    /***
     * 根据id删除数据
     */

    @DeleteMapping("/{id}")
    public Resultx delete (@PathVariable Integer id){
        log.info("根据id删除单个数据{}",id);
        boolean result = iStudentService.removeById(id);
        if (result){
            return Resultx.success("删除成功");
        }else {
            return Resultx.error("删除失败");
        }
    }


    /***
     * 修改数据
     * @param id
     * @param student
     * @return
     */

    @PutMapping("/{id}")
    public Resultx update(@PathVariable Integer id,@RequestBody Student student){
        student.setId(id);
        log.info("修改学生信息{}",student);
        boolean result = iStudentService.updateById(student);
        if (result){
            return Resultx.success("修改成功");
        }else {
            return Resultx.error("修改失败");
        }
    }



}
