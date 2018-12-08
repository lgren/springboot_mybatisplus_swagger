package com.lgren.springboot_mybatisplus_swagger.common;

/**
 * TODO
 * @author Lgren
 * @create 2018-11-27 11:30
 **/
public enum CommResultEnum {
    SUCCESS("1","SUCCESS"),
    FAILURE("0","请求失败"),
    PARAM_ERROR("-1","参数错误"),
    SYSTEM_BUSY("-2","系统繁忙"),
    SYSTEM_ERROR("-3","系统错误"),
    CFG_ERROR("-4","配置文件错误"),
    FILE_UPLOAD_FAILD("-5","上传失败"),
    UPDATE_CACHE_ERROR("-6","更新缓存失败"),
    ILLEGAL_OPERATION("-7","操作不合法");

    private String errorCode;
    private String msg;

    private CommResultEnum(String errorCode, String msg) {
        this.errorCode = errorCode;
        this.msg = msg;
    }
}
