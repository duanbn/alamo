package com.duanbn.alamo.exception;

import com.duanbn.alamo.*;

/**
 * 空值判断异常.
 *
 * @author duanbn
 * @since 1.0.0
 */
public class NullException extends CheckFailureException {

    public NullException() {
    }

    public NullException(String message) {
        super(message);
    }

}
