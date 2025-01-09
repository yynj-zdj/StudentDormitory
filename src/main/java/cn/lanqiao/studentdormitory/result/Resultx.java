package cn.lanqiao.studentdormitory.result;

import lombok.Data;

import java.io.Serializable;

/**
 * 后端统一返回结果
 * @param <T>
 */
@Data
public class Resultx<T> implements Serializable {

    private Integer code; //编码：1成功，0和其它数字为失败
    private String msg; //错误信息
    private T data; //数据


    public static <T> Resultx<T> success() {
        Resultx<T> result = new Resultx<T>();
        result.code = 1;
        return result;
    }

    public static <T> Resultx<T> success(T object) {
        Resultx<T> result = new Resultx<T>();
        result.data = object;
        result.code = 1;
        return result;
    }

    public static <T> Resultx<T> error(String msg) {
        Resultx result = new Resultx();
        result.msg = msg;
        result.code = 0;
        return result;
    }

}
