package com.duanbn.alamo.validator.noparam.impl;

import com.duanbn.alamo.Rule;
import com.duanbn.alamo.RuleBuilder;
import com.duanbn.alamo.StringUtils;
import com.duanbn.alamo.Validate;
import com.duanbn.alamo.annotation.CheckEn;
import com.duanbn.alamo.exception.CheckFailureException;
import com.duanbn.alamo.exception.TypeErrorException;
import com.duanbn.alamo.validator.AbstractStringValidator;
import com.duanbn.alamo.validator.IAnnotationValidator;

/**
 * 英文校验器. 校验变量值是否是英文.
 * 
 * @author duanbn
 * @since 1.0.0
 */
public class EnValidator extends AbstractStringValidator implements IAnnotationValidator<CheckEn> {

    @Override
    public void checkContent(String value, String cname, String message) {
        if (!value.matches("^[A-Za-z]+$")) {
            if (StringUtils.isNotBlank(message)) {
                throw new CheckFailureException(message);
            } else {
                throw new CheckFailureException(cname + "不是英文");
            }
        }
    }

    public void check(Object value, String cacheKey, CheckEn anno) {
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
        check(value, anno.cname(), anno.message());
    }

}
