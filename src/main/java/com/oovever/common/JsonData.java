package com.oovever.common;

import lombok.Getter;
import lombok.Setter;

/**
 * JSON返回结构
 * @author OovEver
 * 2018/1/12 16:28
 */
@Getter
@Setter
public class JsonData {
//    返回结果
    private boolean ret;
//异常信息
    private String msg;
//返回给前台的数据
    private Object data;

    /**
     * 返回结果
     * @param ret 返回结果
     */
    public JsonData(boolean ret) {
        this.ret = ret;
    }

    /**
     * 返回成功
     * @param object 要返回的数据
     * @param msg json信息
     * @return 返回Json数据
     */
    public static JsonData success(Object object, String msg) {
        JsonData jsonData = new JsonData(true);
        jsonData.data = object;
        jsonData.msg = msg;
        return jsonData;
    }

    /**
     * 不传数据时候的返回
     * @param object 要返回的数据
     * @return 返回数据
     */
    public static JsonData success(Object object) {
        JsonData jsonData = new JsonData(true);
        jsonData.data = object;
        return jsonData;
    }
    public static JsonData success() {
        return new JsonData(true);
    }

    /**
     * 返回失败
     * @param msg 失败信息
     * @return 返回失败的json数据
     */
    public static JsonData fail(String msg) {
        JsonData jsonData = new JsonData(false);
        jsonData.msg = msg;
        return jsonData;
    }
}
