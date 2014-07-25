package com.duanbn.alamo.validator.impl;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;

import com.duanbn.alamo.StringUtils;
import com.duanbn.alamo.ValidateUtil;
import com.duanbn.alamo.exception.DefineRuleException;
import com.duanbn.alamo.exception.EnumErrorException;
import com.duanbn.alamo.validator.ParamValidator;

/**
 * 枚举校验器.
 * 校验一个变量的值是否属于某一个枚举的值. 此枚举值必须是数字或者是字符串.
 *
 * @author duanbn
 * @since 1.0.0
 */
public class EnumValidator extends ParamValidator<Object> {

    private static final NumberFormat numberFormat = NumberFormat.getInstance();

    @Override
    public void check(Object value, List<Object> params, String cname, String message) {
        if (params.size() == 1 && params.get(0).equals("")) {
            return;
        }

        if (value instanceof Number) {
            Number n = null;
            for (Object param : params) {
                try {
                    n = numberFormat.parse(String.valueOf(param));
                } catch (ParseException e) {
                    throw _ENUM_EXCEPTION(value, params, cname, message);
                }
                if (ValidateUtil.compare((Number) value, n) == 0) {
                    return;
                }
            }
            throw _ENUM_EXCEPTION(value, params, cname, message);
        } else if (value instanceof String) {
            for (Object param : params) {
                if (((String) value).equals(String.valueOf(param))) {
                    return;
                }
            }
            throw _ENUM_EXCEPTION(value, params, cname, message);
        } else {
            throw new DefineRuleException("不支持出数字和字符之外的枚举");
        }
    }

    private EnumErrorException _ENUM_EXCEPTION(Object value, List<Object> enums, String cname, String message) {
        EnumErrorException e = null;
        if (StringUtils.isNotBlank(message)) {
            e = new EnumErrorException(message);
        } else {
            e = new EnumErrorException(cname + " 不符合指定的枚举值 " + enums);
        }
        e.setValue(value);
        e.setEnums(enums);
        return e;
    }

}
