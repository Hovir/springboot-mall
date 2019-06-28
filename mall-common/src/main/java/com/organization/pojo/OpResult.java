package com.organization.pojo;

import java.io.Serializable;

public class OpResult<E> implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final int OP_SUCCESS = 1;
    public static final int OP_FAILED = 2;
    private int status; // 1 成功 2 失败
    private String errorCode; // 错误业务码
    private String message; // 提示信息
    private E dataValue; // 数据值

    public OpResult() {

    }

    public OpResult(int status, String message) {
    	this.status=status;
    	this.message=message;
    }

    public OpResult(int status, String message, E dataValue) {
	this.status = status;
	this.message = message;
	this.dataValue = dataValue;
    }

    public int getStatus() {
	return status;
    }

    public void setStatus(int status) {
	this.status = status;
    }

    public String getMessage() {
	return message;
    }

    public void setMessage(String message) {
	this.message = message;
    }

    public Object getDataValue() {
	return dataValue;
    }

    public void setDataValue(E dataValue) {
	this.dataValue = dataValue;
    }

    public static final class OpMsg {
	public static final String SAVE_SUCCESS = "保存成功";
	public static final String SAVE_FAIL = "保存失败";
	public static final String MODIFY_SUCCESS = "修改成功";
	public static final String MODIFY_FAIL = "修改失败";
	public static final String DELETE_SUCCESS = "删除成功";
	public static final String DELETE_FAIL = "删除失败";
	public static final String OP_SUCCESS = "操作成功";
	public static final String OP_FAIL = "操作失败";
    }

    public static enum ErrorCode {

	Err_DB_001("数据库查询异常"), Err_BID_001("查询数据不存在");

	private String errorMsg;

	private ErrorCode(String errorMsg) {
	    this.errorMsg = errorMsg;
	}

	public String getErrorMsg() {
	    return errorMsg;
	}

    }

    public String getErrorCode() {
	return errorCode;
    }

    public void setErrorCode(String errorCode) {
	this.errorCode = errorCode;
    }

    /**
     * 获取成功的返回值
     *
     * @param dataValue
     * @return
     */
    public static OpResult<Object> getSuccessResult(Object dataValue) {
	return new OpResult<Object>(OpResult.OP_SUCCESS, "操作成功", dataValue);
    }

    /**
     * 获取失败的返回值
     *
     * @param errorMessage
     * @return
     */
    public static OpResult<Object> getFailedResult(String errorMessage) {
	return new OpResult<Object>(OpResult.OP_FAILED, errorMessage);
    }

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "OpResult [status=" + status + ", errorCode=" + errorCode + ", message=" + message + ", dataValue=" + dataValue + "]";
	}
    
    

}
