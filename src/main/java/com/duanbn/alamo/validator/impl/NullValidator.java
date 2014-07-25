package com.duanbn.alamo.validator.impl;

import java.util.List;

import com.duanbn.alamo.Rule;
import com.duanbn.alamo.RuleBuilder;
import com.duanbn.alamo.StringUtils;
import com.duanbn.alamo.Validate;
import com.duanbn.alamo.annotation.CheckIsNull;
import com.duanbn.alamo.exception.NullException;
import com.duanbn.alamo.validator.IAnnotationValidator;
import com.duanbn.alamo.validator.ParamValidator;

/**
 * 校验参数值是否为null. 如果是字符串则判断是否为空串.
 * 
 * @author duanbn
 */
public class NullValidator extends ParamValidator<Boolean> implements IAnnotationValidator<CheckIsNull> {

    @Override
    public void check(Object value, List<Boolean> params, String cname, String message) {
        boolean isFail = false;
        for (Boolean param : params) {
            if (param) {
                return;
            }

            if (value instanceof String) {
                if (StringUtils.isBlank((String) value)) {
                    isFail = true;
                }
            } else {
                if (value == null) {
                    isFail = true;
                }
            }
        }

        if (isFail) {
            if (StringUtils.isNotBlank(message)) {
                throw new NullException(message);
            } else {
                throw new NullException(cname + " 不能为空值");
            }
        }
    }

    public void check(Object value, String cacheKey, CheckIsNull anno) {
        // ValidateUtil.checkAnnotation(value, anno);
        Rule r = ruleCache.get(cacheKey);
        if (r == null) {
            r = RuleBuilder.build();
            r.setName(anno.cname()).isNull(anno.isNull());
            ruleCache.put(cacheKey, r);
        }
        Validate.check(value, r);
    }

}
