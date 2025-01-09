package cn.lanqiao.studentdormitory.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("admin")
public class Admin {
    private String id;
    private String name;
    private String password;
    private Integer state;
    private int type;
}
