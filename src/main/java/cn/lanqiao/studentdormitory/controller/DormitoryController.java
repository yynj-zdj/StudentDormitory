package cn.lanqiao.studentdormitory.controller;


import cn.lanqiao.studentdormitory.pojo.Building;
import cn.lanqiao.studentdormitory.pojo.Dormitory;
import cn.lanqiao.studentdormitory.result.Resultx;
import cn.lanqiao.studentdormitory.service.IBuildingService;
import cn.lanqiao.studentdormitory.service.IDormitoryManagerService;
import cn.lanqiao.studentdormitory.service.IDormitoryService;
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
 * @since 2025-01-03
 */
@RestController
@RequestMapping("/dormitory")
@Slf4j
public class DormitoryController {
    @Autowired
    private IDormitoryService iDormitoryService;

    @Autowired
    private IBuildingService iBuildingService;
    @Autowired
    private IDormitoryManagerService iDormitoryManagerService;
    /***
     *获取宿舍列表
     */

    @GetMapping
    public Resultx<List<Dormitory>> select() {
        log.info("获取宿舍列表");
        List<Dormitory> list = iDormitoryService.list();
        return Resultx.success(list);
    }


    /***
     * 获取楼宇列表
     */

    @GetMapping("/building")
    public Resultx<List<Building>> selectBuilding() {
        log.info("获取楼宇列表");
        List<Building> list = iBuildingService.list();
        return Resultx.success(list);
    }

    /****
     * 根据宿舍sn查询宿舍
     * @param DormitorySn
     * @return
     */
    @GetMapping("/search/findBySn")
    public Resultx<List<Dormitory>> selectBySn(@RequestParam("sn") String DormitorySn) {
        log.info("查询宿舍{}sn", DormitorySn);

        // 查询学生列表
        List<Dormitory> DormitoryList = iDormitoryService.lambdaQuery().eq(Dormitory::getSn, DormitorySn).list();

        // 检查列表是否为空
        if (DormitoryList.isEmpty()) {
            return Resultx.error("未找到宿舍sn");
        }

        return Resultx.success(DormitoryList); // 返回整个列表
    }

    /****
     * 添加
     * @param dormitory
     * @return
     */
    @PostMapping
    public Resultx<String> save(@RequestBody Dormitory dormitory){
        log.info("添加宿舍参数{}", dormitory);
        boolean result = iDormitoryService.save(dormitory);
        if (result){
            return Resultx.success("添加成功");
        }else {
            return Resultx.error("添加失败");
        }

    }

    /***
     * 删除
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Resultx delete (@PathVariable Integer id){
        log.info("根据id删除单个数据{}",id);
        boolean result = iDormitoryService.removeById(id);
        if (result){
            return Resultx.success("删除成功");
        }else {
            return Resultx.error("删除失败");
        }
    }

    /****
     * 修改
     * @param dormitory
     * @return
     */
    @PutMapping("/{id}")
    public Resultx update(@PathVariable Integer id,@RequestBody Dormitory dormitory){
        dormitory.setId(id);
        log.info("修改宿舍信息{}",dormitory);
        boolean result = iDormitoryService.updateById(dormitory);
        if (result){
            return Resultx.success("修改成功");
        }else {
            return Resultx.error("修改失败");
        }
    }


}
