package com.lgren.springboot_mybatisplus_swagger.common;

import java.io.Serializable;

public class CommResult<T> implements Serializable{
	
	private static final long serialVersionUID = -1L;
	// 结果
	private boolean success;
	//错误编号
	private String errorCode;
	//错误详情
	private String errorMsg;
	// 方法返回结果
	private T data;

	public CommResult() {}
	
	public boolean isSuccess() {
		return success;
	}
	public CommResult<T> setSuccess(boolean success) {
		this.success = success;
        return this;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public CommResult<T> setErrorCode(String errorCode) {
		this.errorCode = errorCode;
        return this;
    }
	public String getErrorMsg() {
		return errorMsg;
	}
	public CommResult<T> setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
        return this;
    }
	public T getData() {
		return data;
	}

	public CommResult<T> setData(T data) {
		this.data = data;
        return this;
    }
	/**
	 * 构建错误对象
	 * @param errorCode 错误编号
	 * @param errorMsg 错误详情
     */
	public static <T> CommResult<T> newFailure(String errorCode, String errorMsg) {
		CommResult<T> result=new CommResult<>();
		result.setSuccess(false);
		result.setErrorCode(errorCode);
		result.setErrorMsg(errorMsg);
		return result;
	}

	/**
	 * 构建成功对象
	 * @param data 返回数据
	 */
	public static <T> CommResult<T> newSuccess(T data) {
		CommResult<T> result=new CommResult<>();
		result.setData(data);
		result.setSuccess(true);
		result.setErrorCode("200");
		result.setErrorMsg("操作成功");
		return result;
	}

    @Override
    public String toString() {
        return "{" +
                "\"success\":" + success +
                ",\"errorCode\":\"" + errorCode + '\"' +
                ",\"errorMsg\":\"" + errorMsg + '\"' +
                ",\"data\":" + data +
                "}";
    }
}
