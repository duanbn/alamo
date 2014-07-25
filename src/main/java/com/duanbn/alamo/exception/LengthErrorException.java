package com.duanbn.alamo.exception;

import java.util.*;

import com.duanbn.alamo.*;

/**
 * 运行时异常.
 * 校验长度发生错误时抛出此异常.
 *
 * @author duanbn
 * @since 1.0.0
 */
public class LengthErrorException extends CheckFailureException {

    private Object value;
    private List<Range> range;

    public LengthErrorException() {
    }

    public LengthErrorException(String message) {
        super(message);
    }

    public void setRange(List<Range> range) {
        this.range = range;
    }

    public List<Range> getRange() {
        return this.range;
    }
    
    public Object getValue() {
        return value;
    }
    
    public void setValue(Object value) {
        this.value = value;
    }
}
