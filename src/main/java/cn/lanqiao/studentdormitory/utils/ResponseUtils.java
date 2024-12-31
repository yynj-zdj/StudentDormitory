package cn.lanqiao.studentdormitory.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: 李某人
 * @Date: 2024/09/18/9:14
 * @Description:
 * 响应工具类(用来规范响应的数据)
 */
@Data
@NoArgsConstructor
public class ResponseUtils<T> {
    private Integer code; // 状态码 200 成功 304 失败
    private String message; // 信息 响应的信息结果
    private T data;//携带的数据

    public ResponseUtils(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public ResponseUtils(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
}
