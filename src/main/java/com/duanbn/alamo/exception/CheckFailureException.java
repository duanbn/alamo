package com.duanbn.alamo.exception;

/**
 * 校验错误异常基类.
 *
 * @author duanbn
 * @since 1.0.0
 */
public class CheckFailureException extends RuntimeException {
	
    public CheckFailureException() {
    }

    public CheckFailureException(String message) {
        super(message);
    }

}
