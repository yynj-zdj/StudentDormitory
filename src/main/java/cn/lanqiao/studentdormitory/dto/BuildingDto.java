package cn.lanqiao.studentdormitory.dto;

import cn.lanqiao.studentdormitory.pojo.DormitoryManager;
import lombok.Data;


@Data
public class BuildingDto {
    private Integer id;

    private String location;


    private String name;

    private DormitoryManager dormitoryManager;
}
