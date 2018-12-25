package com.fcang.spider.hotel.core;

/**
 * @Description: 响应编码
 * @author hewenfeng
 * @date 2017年12月22日 上午9:27:01
 * @version V1.0
 */
public class BaseResponseStatus {
	
	public static final  int SUCCESS = BaseResponseStatusEnum.SUCCESS.status;
	
	public static final  int FAILED = BaseResponseStatusEnum.FAILED.status;
	
	public static final  int PERMISSION_ERROR = BaseResponseStatusEnum.PERMISSION_ERROR.status;
	
	public static final  int OPERATE_REPEAT = BaseResponseStatusEnum.OPERATE_REPEAT.status;
	
	public static final  int EXCEPTION = BaseResponseStatusEnum.EXCEPTION.status;
	
	public static final  int INVALID_PARAMETER = BaseResponseStatusEnum.INVALID_PARAMETER.status;
	

	public enum BaseResponseStatusEnum {
		/**SUCCESS(100000,"成功")*/
		SUCCESS(100000,"成功"),
		/**FAILED(100001,"失败") **/
		FAILED(100001,"失败"),
		/**OPERATE_REPEAT(100002,"重复操作"), **/
		OPERATE_REPEAT(100002,"重复操作"),
		/**PERMISSION_ERROR(100003,"权限错误"),**/
		PERMISSION_ERROR(100003,"权限错误"),
		
		/** 900001,"非法参数或者参数为空" **/
		INVALID_PARAMETER(900001, "非法参数或者参数为空"),

		/** 900000,"系统异常" */
		EXCEPTION(900000, "系统异常");

		private String msg;
		private int status;

		private BaseResponseStatusEnum(int status, String msg) {
			this.status = status;
			this.msg = msg;
		}

		public String getMsg() {
			return this.msg;
		}

		public int getStatus() {
			return this.status;
		}

	}
}
