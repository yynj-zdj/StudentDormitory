package cn.lanqiao.studentdormitory;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@SpringBootApplication
@MapperScan("cn.lanqiao.studentdormitory.mapper")
public class StudentDormitoryApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudentDormitoryApplication.class, args);
    }

}
