package com.fcang.spider.hotel.core;

public class SpiderException extends Exception{

	private static final long serialVersionUID = 1477293943362783879L;
	
	private  int serial = 0;
	private  String code;
	public SpiderException() {
        super();
    }
	
	public SpiderException(String mess) {
        super(mess);
    }
	
	public SpiderException(int serial,String code, String mess) {
		super(mess);
		this.serial = serial;
	    this.code = code;
    }
	
	public SpiderException(String code, String mess) {
		super(mess);
	    this.code = code;
    }

	public int getSerial() {
		return serial;
	}

	public String getCode() {
		return code;
	}

	public void setSerial(int serial) {
		this.serial = serial;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
}
