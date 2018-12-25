package com.fcang.spider.hotel.core;



import java.io.Serializable;
import java.util.Collections;

import com.fcang.spider.hotel.core.BaseResponseStatus.BaseResponseStatusEnum;


/**
 * @author hewenfeng
 * @date 2017-12-27 17:29:38
 **/
public class BaseFullResponse<T> implements Serializable {

	private static final long serialVersionUID = 5105502275635899805L;
	
	public static final String DEFAULT_SUCCESS_STATUS=BaseResponseStatusEnum.SUCCESS.toString();
	
	public static final String DEFAULT_FAILED_STATUS=BaseResponseStatusEnum.FAILED.toString();
	
	
	private String code;
	
	private int status;
	
	/** 响应消息 */
	private String message;
	
	private T data;
	
	public BaseFullResponse() {
		super();
	}
	/**成功返回，data=null*/
	@SuppressWarnings("rawtypes")
	public static final  BaseFullResponse OK = new BaseFullResponse<>(BaseResponseStatusEnum.SUCCESS.getStatus(),
			BaseResponseStatusEnum.SUCCESS.toString(),null,null);
	
	/**成功返回，data=null*/
	@SuppressWarnings("unchecked")
	public static <T> BaseFullResponse<T> emptyData(){
		return (BaseFullResponse<T>)OK;
	}
	
	/**成功返回，data=emptyList*/
	@SuppressWarnings("rawtypes")
	private static final  BaseFullResponse EMPTY_LIST = new BaseFullResponse<>(BaseResponseStatusEnum.SUCCESS.getStatus(),
			BaseResponseStatusEnum.SUCCESS.toString(),null,Collections.emptyList());
	/**成功返回，data=emptyList*/
	@SuppressWarnings("unchecked")
	public static <T> BaseFullResponse<T> emptyList(){
		return (BaseFullResponse<T>)EMPTY_LIST;
	}
	

	
	public BaseFullResponse(String code, T data) {
		this.code = code;
		this.data = data;
	}
	
	public BaseFullResponse(int status,String code,String message, T data) {
		this.code = code;
		this.data = data;
		this.message = message;
		this.status = status;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public static <T> BaseFullResponse<T> success(T data) {
		return successByCode(DEFAULT_SUCCESS_STATUS, data);
	}
	
	public static <T> BaseFullResponse<T> successByCode(String code, T data) {
		return success(code,null,  data);
	}
	
	public static <T> BaseFullResponse<T> successByMessage(String message, T data) {
		return success(DEFAULT_SUCCESS_STATUS, message, data);
	}
	
	public static <T> BaseFullResponse<T> success(String code,String message, T data) {
		return new BaseFullResponse<>(BaseResponseStatusEnum.SUCCESS.getStatus(),code,message, data);
	}
	public static <T> BaseFullResponse<T> failed(int status,String code) {
		return new BaseFullResponse<>(status,code, null, null);
	}
	
	public static <T> BaseFullResponse<T> failed(String code) {
		return new BaseFullResponse<>(BaseResponseStatusEnum.FAILED.getStatus(),code, null, null);
	}
	public static <T> BaseFullResponse<T> failed(String code, String message) {
		return new BaseFullResponse<>(BaseResponseStatusEnum.FAILED.getStatus(),code, message, null);
	}

	public static <T> BaseFullResponse<T> failed(String code, String message, T data) {
		return new BaseFullResponse<>(BaseResponseStatusEnum.FAILED.getStatus(),code, message, data);
	}
	
	public static <T> BaseFullResponse<T> failed(int status,String code, String message, T data) {
		return new BaseFullResponse<>(status,code, message, data);
	}
	
	public static <T> BaseFullResponse<T> failed(BaseResponseStatusEnum baseResponseStatusEnum){
		return failed(baseResponseStatusEnum,null);
	}
	public static <T> BaseFullResponse<T> failed(BaseResponseStatusEnum baseResponseStatusEnum,String message){
		return new BaseFullResponse<>(baseResponseStatusEnum.getStatus(),
				baseResponseStatusEnum.toString(),
				message==null?baseResponseStatusEnum.getMsg():message,
						null);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
	public boolean isSuccess() {
		return BaseResponseStatusEnum.SUCCESS.toString().equalsIgnoreCase(getCode());
	}
	

	public boolean isException() {
		if(getStatus()!=0) {
			return BaseResponseStatusEnum.EXCEPTION.getStatus()==getStatus();
		}
		return BaseResponseStatusEnum.EXCEPTION.toString().equalsIgnoreCase(getCode());
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * @param string
	 * @param memberAmountCalculateModel
	 * @return
	 */
	public static <T> BaseFullResponse<T> failedByMessage(String message,
			T data) {
		return failed(DEFAULT_FAILED_STATUS, message,data);
	}
	@Override
	public String toString() {
		return "BaseFullResponse [code=" + code + ", status=" + status + ", message=" + message + ", data=" + data
				+ "]";
	}
	
	
}
