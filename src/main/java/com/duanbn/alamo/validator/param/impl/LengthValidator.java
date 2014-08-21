package com.duanbn.alamo.validator.param.impl;

import java.util.List;

import com.duanbn.alamo.Range;
import com.duanbn.alamo.StringUtils;
import com.duanbn.alamo.exception.CheckFailureException;
import com.duanbn.alamo.exception.LengthErrorException;
import com.duanbn.alamo.validator.param.ParamValidator;

/**
 * 长度校验器. 校验变量的长度, 如果变量是字符类型，则校验字符串的长度,如果变量是数值类型，则校验数值的位数.
 * <b>长度校验只支持连续区间的校验，例：大于2并且小于4， 不支持离散区间校验例：大于4或者小于2</b>
 * 
 * @author duanbn
 * @since 1.0.0
 */
public class LengthValidator extends ParamValidator<Range> {

    @Override
    public void check(Object value, List<Range> params, String cname, String message) {
        int length = -1;

        if (value instanceof String || value instanceof Integer || value instanceof Short || value instanceof Long) {
            length = String.valueOf(value).length();
        } else if (value instanceof Float || value instanceof Double) {
            length = String.valueOf(value).length() - 1;
        }

        if (length == -1) {
            throw new CheckFailureException("获取" + cname + "长度失败");
        }

        for (Range range : params) {
            if (range.in(length)) {
                continue;
            } else {
                throw _ENUM_EXCEPTION(value, params, cname, message);
            }
        }
    }

    private LengthErrorException _ENUM_EXCEPTION(Object value, List<Range> params, String cname, String message) {
        LengthErrorException e = null;
        if (StringUtils.isNotBlank(message)) {
            e = new LengthErrorException(message);
        } else {
            e = new LengthErrorException(cname + " 不符合指定的长度范围 " + params);
        }
        e.setValue(value);
        e.setRange(params);
        return e;
    }

}
