package cn.lanqiao.studentdormitory.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("admin")
public class Admin {

  private String id;
  private java.sql.Timestamp createTime;
  private java.sql.Timestamp updateTime;
  private String valid;
  private long version;
  private String name;
  private String password;
  private long state;


}
