package com.duanbn.alamo.validator.param.impl;

import java.util.List;

import com.duanbn.alamo.StringUtils;
import com.duanbn.alamo.exception.CheckFailureException;
import com.duanbn.alamo.exception.TypeErrorException;
import com.duanbn.alamo.validator.param.ParamValidator;

/**
 * 正则表达式校验器.
 * 属于有参校验器, 参数为正则表达式.
 *
 * @author duanbn
 * @since 1.0.0
 */
public class RegexValidator extends ParamValidator<String> {

    @Override
    public void check(Object value, List<String> params, String cname, String message) {
        if (!(value instanceof String)) {
            throw new TypeErrorException("正则表达式只支持字符串校验");
        }

        for (String regex : params) {
            if (!((String) value).matches(regex)) {
                if (StringUtils.isNotBlank(message)) {
                    throw new CheckFailureException(message);
                } else {
                    throw new CheckFailureException(cname + "不符合正则表达式规则");
                }
            }
        }
        
    }

}
