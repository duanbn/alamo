package com.duanbn.alamo.validator.noparam.impl;

import com.duanbn.alamo.Rule;
import com.duanbn.alamo.RuleBuilder;
import com.duanbn.alamo.StringUtils;
import com.duanbn.alamo.Validate;
import com.duanbn.alamo.annotation.CheckNumber;
import com.duanbn.alamo.exception.CheckFailureException;
import com.duanbn.alamo.validator.IAnnotationValidator;
import com.duanbn.alamo.validator.noparam.NoParamValidator;

/**
 * 数字类型校验器.
 * 属于无参校验器, 校验变量值是否是一个数值类型.
 *
 * @author duanbn
 * @since 1.0.0
 */
public class NumberValidator extends NoParamValidator implements IAnnotationValidator<CheckNumber> {

    @Override
    public void check(Object value, String cname, String message) {
        if (!(value instanceof Number)) {
            if (StringUtils.isNotBlank(message)) {
                throw new CheckFailureException(message);
            } else {
                throw new CheckFailureException(cname + "不是数字");
            }
        }
    }

    public void check(Object value, String cacheKey, CheckNumber anno) {
        // ValidateUtil.checkAnnotation(value, anno);
        Rule r = ruleCache.get(cacheKey); 
        if (r == null) { 
            r = RuleBuilder.build();
            r.setName(anno.cname()).isNull(anno.isNull()).length(anno.length()).range(anno.range()).setEnum(anno.value()).setErrorMessage(anno.message());
            ruleCache.put(cacheKey, r);
        }
        Validate.check(value, r);
        check(value, anno.cname(), anno.message());
    }

}
