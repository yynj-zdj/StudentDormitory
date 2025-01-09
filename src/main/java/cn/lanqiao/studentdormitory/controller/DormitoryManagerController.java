package cn.lanqiao.studentdormitory.controller;


import cn.lanqiao.studentdormitory.pojo.Dormitory;
import cn.lanqiao.studentdormitory.pojo.DormitoryManager;
import cn.lanqiao.studentdormitory.pojo.Student;
import cn.lanqiao.studentdormitory.result.Result;
import cn.lanqiao.studentdormitory.service.IDormitoryManagerService;
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
 * @since 2025-01-06
 */
@RestController
@RequestMapping("/dormitoryManager")
@Slf4j
public class DormitoryManagerController {
@Autowired
private IDormitoryManagerService iDormitoryManagerService;

    @GetMapping
    public Result<List<DormitoryManager>> select() {
        log.info("获取所有宿管信息");
        List<DormitoryManager> list = iDormitoryManagerService.list();
        return Result.success(list);
    }


    @GetMapping("/search/findBySn")
    public Result<List<DormitoryManager>> selectBySn(@RequestParam("sn") String dormitoryManagerSn) {
        log.info("按sn查询宿管信息{}sn", dormitoryManagerSn);

        // 查询学生列表
        List<DormitoryManager> DormitoryList = iDormitoryManagerService.lambdaQuery().eq(DormitoryManager::getSn, dormitoryManagerSn).list();

        // 检查列表是否为空
        if (DormitoryList.isEmpty()) {
            return Result.error("未找到宿舍sn");
        }

        return Result.success(DormitoryList); // 返回整个列表
    }


    @GetMapping("/search/findByName")
    public Result<List<DormitoryManager>> selectByName(@RequestParam("name") String dormitoryManagerName) {
        log.info("按姓名查询宿管信息{}", dormitoryManagerName);

        // 查询学生列表
        List<DormitoryManager> studentList = iDormitoryManagerService.lambdaQuery().eq(DormitoryManager::getName, dormitoryManagerName).list();

        // 检查列表是否为空
        if (studentList.isEmpty()) {
            return Result.error("未找到该学生");
        }

        return Result.success(studentList); // 返回整个列表
    }


    @PostMapping
    public Result<String> save(@RequestBody DormitoryManager dormitoryManager){
        log.info("添加宿管信息{}", dormitoryManager);
        boolean result = iDormitoryManagerService.save(dormitoryManager);
        if (result){
            return Result.success("添加成功");
        }else {
            return Result.error("添加失败");
        }

    }


    @PutMapping("/{id}")
    public Result update(@PathVariable String id,@RequestBody DormitoryManager dormitoryManager){
        dormitoryManager.setId(id);
        log.info("修改宿管信息{}",dormitoryManager);
        boolean result = iDormitoryManagerService.updateById(dormitoryManager);
        if (result){
            return Result.success("修改成功");
        }else {
            return Result.error("修改失败");
        }
    }


    @DeleteMapping("/{id}")
    public Result delete (@PathVariable String id){
        log.info("根据id删除单个数据{}",id);
        boolean result = iDormitoryManagerService.removeById(id);
        if (result){
            return Result.success("删除成功");
        }else {
            return Result.error("删除失败");
        }
    }


}
