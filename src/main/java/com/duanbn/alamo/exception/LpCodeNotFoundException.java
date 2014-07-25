package com.duanbn.alamo.exception;

/**
 * 运行时异常.
 * 找不到这个业务码.
 *
 * @author duanbn
 * @since 1.0.0
 */
public class LpCodeNotFoundException extends CheckFailureException {

    public LpCodeNotFoundException() {
    }

    public LpCodeNotFoundException(String message) {
        super(message);
    }

}
