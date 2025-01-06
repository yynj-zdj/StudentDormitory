package cn.lanqiao.studentdormitory.controller;


import cn.lanqiao.studentdormitory.pojo.Student;
import cn.lanqiao.studentdormitory.result.Result;
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
    public Result<List<Student>> select() {
        log.info("查询数据");
        List<Student> list = iStudentService.list();
        return Result.success(list);
    }


    @GetMapping("/search/findByName")
    public Result<List<Student>> selectByName(@RequestParam("name") String findByName) {
        log.info("查询学生{}", findByName);

        // 查询学生列表
        List<Student> studentList = iStudentService.lambdaQuery().eq(Student::getName, findByName).list();

        // 检查列表是否为空
        if (studentList.isEmpty()) {
            return Result.error("未找到该学生");
        }

        return Result.success(studentList); // 返回整个列表
    }


    @GetMapping("/search/findBySn")
    public Result<List<Student>> selectBySn(@RequestParam("sn") String studentSn) {
        log.info("查询学生{}sn", studentSn);

        // 查询学生列表
        List<Student> studentList = iStudentService.lambdaQuery().eq(Student::getSn, studentSn).list();

        // 检查列表是否为空
        if (studentList.isEmpty()) {
            return Result.error("未找到该学生");
        }

        return Result.success(studentList); // 返回整个列表
    }


    /***
     * 新增学生
     */
    /***
     * 添加数据
     */
    @PostMapping
    public Result<String> save(@RequestBody Student student){
        log.info("添加学生数据参数{}", student);
        boolean result = iStudentService.save(student);
        if (result){
            return Result.success("添加成功");
        }else {
            return Result.error("添加失败");
        }

    }


    /***
     * 根据id删除数据
     */

    @DeleteMapping("/{id}")
    public Result delete (@PathVariable Integer id){
        log.info("根据id删除单个数据{}",id);
        boolean result = iStudentService.removeById(id);
        if (result){
            return Result.success("删除成功");
        }else {
            return Result.error("删除失败");
        }
    }


    /***
     * 修改数据
     * @param id
     * @param student
     * @return
     */

    @PutMapping("/{id}")
    public Result update(@PathVariable Integer id,@RequestBody Student student){
        student.setId(id);
        log.info("修改学生信息{}",student);
        boolean result = iStudentService.updateById(student);
        if (result){
            return Result.success("修改成功");
        }else {
            return Result.error("修改失败");
        }
    }



}
