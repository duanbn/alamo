package com.duanbn.alamo.exception;

import java.util.*;

/**
 * 运行时异常.
 * 校验枚举值发生错误时抛出此异常.
 *
 * @author duanbn
 * @since 1.0.0
 */
public class EnumErrorException extends CheckFailureException {

    private Object value;
    private List<Object> enums;

    public EnumErrorException() {
    }

    public EnumErrorException(String message) {
        super(message);
    }

    public void setEnums(List<Object> enums) {
        this.enums = enums;
    }

    public List<Object> getEnums() {
        return this.enums;
    }
    
    public Object getValue() {
        return value;
    }
    
    public void setValue(Object value) {
        this.value = value;
    }
}
