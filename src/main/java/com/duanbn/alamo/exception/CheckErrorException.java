package com.duanbn.alamo.exception;

/**
 * 用于用户自定义校验使用.
 * 
 * @author duanbn
 * 
 */
public class CheckErrorException extends Exception {

	public CheckErrorException() {

	}

	public CheckErrorException(String msg) {
		super(msg);
	}

}
