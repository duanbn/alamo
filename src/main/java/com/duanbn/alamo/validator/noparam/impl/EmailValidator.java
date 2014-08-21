package com.duanbn.alamo.validator.noparam.impl;

import com.duanbn.alamo.Rule;
import com.duanbn.alamo.RuleBuilder;
import com.duanbn.alamo.StringUtils;
import com.duanbn.alamo.Validate;
import com.duanbn.alamo.annotation.CheckEmail;
import com.duanbn.alamo.exception.CheckFailureException;
import com.duanbn.alamo.exception.TypeErrorException;
import com.duanbn.alamo.validator.AbstractStringValidator;
import com.duanbn.alamo.validator.IAnnotationValidator;

/**
 * Email校验器. 校验变量值是否符合Email的格式.
 * 
 * @author duanbn
 * @since 1.0.0
 */
public class EmailValidator extends AbstractStringValidator implements IAnnotationValidator<CheckEmail> {

    @Override
    public void checkContent(String value, String cname, String message) {
        if (!value
                .matches("[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?")) {
            if (StringUtils.isNotBlank(message)) {
                throw new CheckFailureException(message);
            } else {
                throw new CheckFailureException(cname + "不是合法的Email");
            }
        }
    }

    public void check(Object value, String cacheKey, CheckEmail anno) {
        if (anno.isNull()) {
            if (!(value instanceof String)) {
                throw new TypeErrorException(anno.cname() + " 必须是字符串");
            }
            if (StringUtils.isBlank((String) value)) {
                return;
            }
        }

        // ValidateUtil.checkAnnotation(value, anno);
        Rule r = ruleCache.get(cacheKey);
        if (r == null) {
            r = RuleBuilder.build();
            r.setName(anno.cname()).isNull(anno.isNull()).length(anno.length()).setEnum(anno.value())
                    .setErrorMessage(anno.message());
            ruleCache.put(cacheKey, r);
        }
        Validate.check(value, r);
        // check email
        check(value, anno.cname(), anno.message());
    }

}
