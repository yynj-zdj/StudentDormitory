package cn.lanqiao.studentdormitory.controller;

import cn.lanqiao.studentdormitory.dto.BuildingDto;
import cn.lanqiao.studentdormitory.pojo.Building;
import cn.lanqiao.studentdormitory.pojo.DormitoryManager;
import cn.lanqiao.studentdormitory.result.Resultx;
import cn.lanqiao.studentdormitory.service.IBuildingService;
import cn.lanqiao.studentdormitory.service.IDormitoryManagerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/building")
@Slf4j
public class BuildingController {
    @Autowired
    private IBuildingService iBuildingService;

    @Autowired
    private IDormitoryManagerService iDormitoryManagerService;
    /***
     * 获取楼宇列表
     */
    @GetMapping
    public Resultx<List<BuildingDto>> selectBuilding() {
        log.info("获取楼宇列表");
        List<Building> buildingList = iBuildingService.list();
        List<DormitoryManager> dormitoryManagers = iDormitoryManagerService.list();

        // 创建一个 Map 来快速查找 DormitoryManager
        Map<String, DormitoryManager> dormitoryManagerMap = new HashMap<>();
        for (DormitoryManager dormitoryManager : dormitoryManagers) {
            dormitoryManagerMap.put(dormitoryManager.getId(), dormitoryManager);
        }

        List<BuildingDto> buildingDtoList = new ArrayList<>();
        for (Building building : buildingList) {
            BuildingDto buildingDto = new BuildingDto();
            buildingDto.setId(Integer.valueOf(building.getId())); // 添加这一行
            buildingDto.setLocation(building.getLocation());
            buildingDto.setName(building.getName());

            // 获取对应的 DormitoryManager
            DormitoryManager dormitoryManager = dormitoryManagerMap.get(building.getDormitoryManagerId());
            if (dormitoryManager != null) {
                buildingDto.setDormitoryManager(dormitoryManager);
            }

            buildingDtoList.add(buildingDto);
        }

        return Resultx.success(buildingDtoList);
    }

    /***
     * 回显宿管信息
     * @return
     */
    @GetMapping("/dormitoryManager")
    public Resultx<List<DormitoryManager>> selectDormitoryManager() {
        log.info("回显宿管信息");
        List<DormitoryManager> list = iDormitoryManagerService.list();
        return Resultx.success(list);
    }
    /****
     * 根据宿舍name查询楼宇
     * @param BuildingName
     * @return
     */
    @GetMapping("/search/findByName")
    public Resultx<List<BuildingDto>> selectByName(@RequestParam("name") String BuildingName) {
        log.info("查询楼宇{} name", BuildingName);

        // 查询楼宇列表
        List<Building> buildingList = iBuildingService.lambdaQuery().eq(Building::getName, BuildingName).list();

        // 检查列表是否为空
        if (buildingList.isEmpty()) {
            return Resultx.error("未找到楼宇 name");
        }

        List<BuildingDto> buildingDtoList = new ArrayList<>();
        for (Building building : buildingList) {
            BuildingDto buildingDto = new BuildingDto();
            buildingDto.setLocation(building.getLocation());
            buildingDto.setName(building.getName());

            // 获取 dormitoryManagerId
            String dormitoryManagerId = building.getDormitoryManagerId();

            // 查询对应的 dormitoryManager
            DormitoryManager dormitoryManager = iDormitoryManagerService.lambdaQuery().eq(DormitoryManager::getId, dormitoryManagerId).one();

            buildingDto.setDormitoryManager(dormitoryManager);
            buildingDtoList.add(buildingDto);
        }

        return Resultx.success(buildingDtoList);
    }
    /****
     * 添加
     * @param building
     * @return
     */
    @PostMapping
    public Resultx<String> save(@RequestBody Building building){
        log.info("添加楼宇参数{}",building);
        boolean result = iBuildingService.save(building);
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
        boolean result = iBuildingService.removeById(id);
        if (result){
            return Resultx.success("删除成功");
        }else {
            return Resultx.error("删除失败");
        }
    }
    /****
     * 修改
     * @param building
     * @return
     */
    @PutMapping("/{id}")
    public Resultx update(@PathVariable String id,@RequestBody Building building){
        building.setId(id);
        log.info("修改楼宇信息{}",building);
        boolean result = iBuildingService.updateById(building);
        if (result){
            return Resultx.success("修改成功");
        }else {
            return Resultx.error("修改失败");
        }
    }
}
