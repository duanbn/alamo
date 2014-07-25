package com.duanbn.alamo.exception;

/**
 * 运行时异常.
 * 校验发生错误时抛出此异常.
 *
 * @author duanbn
 * @since 1.0.0
 */
public class DefineRuleException extends RuntimeException {

    public DefineRuleException() {
    }

    public DefineRuleException(String message) {
        super(message);
    }

}
