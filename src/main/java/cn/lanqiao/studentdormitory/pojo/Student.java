package cn.lanqiao.studentdormitory.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student {

  private String id;
  private Integer createTime;
  private Integer updateTime;
  private String valid;
  private long version;
  private String name;
  private String password;
  private String sex;
  private String sn;

  }
