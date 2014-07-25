package com.duanbn.alamo.exception;

/**
 * 运行时异常.
 * 自定义校验错误.
 *
 * @author duanbn
 * @since 1.0.0
 */
public class CustomErrorException extends CheckFailureException {

    public CustomErrorException() {
    }

    public CustomErrorException(String message) {
        super(message);
    }

}
