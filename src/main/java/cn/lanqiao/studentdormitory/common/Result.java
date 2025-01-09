package cn.lanqiao.studentdormitory.common;

import cn.lanqiao.studentdormitory.enums.ResponseCodeEnum;
import lombok.Data;

@Data
public class Result {
    private String code;
    private String message;
    private Object data;

    public static Result success(String code, String message, Object data) {
        Result result = new Result();
        result.setCode(code);
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    public static Result error(ResponseCodeEnum codeEnum) {
        Result result = new Result();
        result.setCode(codeEnum.getCode());
        result.setMessage(codeEnum.getInfo());
        return result;
    }

    public static Result error(String code, String message) {
        Result result = new Result();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }
} 