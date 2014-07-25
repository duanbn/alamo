package com.duanbn.alamo.exception;

/**
 * 运行时异常.
 * 校验变量值类型生错误时抛出此异常.
 *
 * @author duanbn
 * @since 1.0.0
 */
public class TypeErrorException extends CheckFailureException {

    public TypeErrorException() {
    }

    public TypeErrorException(String message) {
        super(message);
    }

}
