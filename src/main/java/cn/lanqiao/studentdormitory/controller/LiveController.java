package cn.lanqiao.studentdormitory.controller;


import cn.lanqiao.studentdormitory.pojo.*;
import cn.lanqiao.studentdormitory.result.Resultx;
import cn.lanqiao.studentdormitory.service.IDormitoryService;
import cn.lanqiao.studentdormitory.service.ILiveService;
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
@RequestMapping("/live")
@Slf4j
public class LiveController {

    @Autowired
    private ILiveService iLiveService;

    @Autowired
    private IDormitoryService iDormitoryService;


    @Autowired
    private IStudentService iStudentService;

    /***
     *数据查询
     */
    @GetMapping
    public Resultx<List<Live>> select() {
        log.info("查询数据");
        List<Live> list = iLiveService.list();
        return Resultx.success(list);
    }
    @GetMapping("/dormitory")
    public Resultx<List<Dormitory>> selectDormitory() {
        log.info("获取宿舍列表");
        List<Dormitory> list = iDormitoryService.list();
        return Resultx.success(list);
    }

    @GetMapping("/student")
    public Resultx<List<Student>> selectStudent() {
        log.info("获取学生列表");
        List<Student> list = iStudentService.list();
        return Resultx.success(list);
    }


    @GetMapping("/search/findDormitoryBySn")
    public Resultx<List<Dormitory>> selectBySn(@RequestParam("sn") String dormitorySn) {
        log.info("按sn查询宿信息{}sn", dormitorySn);

        // 查询学生列表
        List<Dormitory> DormitoryList = iDormitoryService.lambdaQuery().eq(Dormitory::getSn, dormitorySn).list();

        // 检查列表是否为空
        if (DormitoryList.isEmpty()) {
            return Resultx.error("未找到宿舍sn");
        }

        return Resultx.success(DormitoryList); // 返回整个列表
    }
    @GetMapping("/search/findStudentByName")
    public Resultx<List<Student>> selectByName(@RequestParam("name") String studentName) {
        log.info("按姓名查询学生信息{}", studentName);

        // 查询学生列表
        List<Student> studentList = iStudentService.lambdaQuery().eq(Student::getName, studentName).list();

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
    public Resultx<String> save(@RequestBody Live live){
        log.info("添加学生数据参数{}", live);
        boolean result = iLiveService.save(live);
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
    public Resultx delete (@PathVariable String id){
        log.info("根据id删除单个数据{}",id);
        boolean result = iLiveService.removeById(id);
        if (result){
            return Resultx.success("删除成功");
        }else {
            return Resultx.error("删除失败");
        }
    }


    /***
     * 修改数据
     * @param id
     * @param live
     * @return
     */

    @PutMapping("/{id}")
    public Resultx update(@PathVariable String id,@RequestBody Live live){
        live.setId(id);
        log.info("修改宿学生信息{}",live);
        boolean result = iLiveService.updateById(live);
        if (result){
            return Resultx.success("修改成功");
        }else {
            return Resultx.error("修改失败");
        }
    }

}
