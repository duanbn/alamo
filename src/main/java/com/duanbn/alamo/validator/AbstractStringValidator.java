package com.duanbn.alamo.validator;

import com.duanbn.alamo.StringUtils;
import com.duanbn.alamo.exception.TypeErrorException;
import com.duanbn.alamo.validator.noparam.NoParamValidator;

/**
 * 字符串类型基础校验器.
 * 所有内容是字符串类型的校验器应该继承自此类.
 *
 * @author duanbn
 * @since 1.0.0
 */
public abstract class AbstractStringValidator extends NoParamValidator {

    @Override
    public void check(Object value, String cname, String message) {
        if (!(value instanceof String)) {
            if (StringUtils.isNotBlank(message)) {
                throw new TypeErrorException(message);
            } else {
                throw new TypeErrorException(cname + "不是一个字符串");
            }
        }

        checkContent((String) value, cname, message);
    }

    public abstract void checkContent(String value, String cname, String message);

}
