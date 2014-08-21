package com.duanbn.alamo.validator.noparam.impl;

import com.duanbn.alamo.Rule;
import com.duanbn.alamo.RuleBuilder;
import com.duanbn.alamo.StringUtils;
import com.duanbn.alamo.Validate;
import com.duanbn.alamo.annotation.CheckString;
import com.duanbn.alamo.exception.CheckFailureException;
import com.duanbn.alamo.validator.AbstractStringValidator;
import com.duanbn.alamo.validator.IAnnotationValidator;

/**
 * 字符串校验器.
 * 无参校验器, 判断变量值是否是一个字符串.
 *
 * @author duanbn
 * @since 1.0.0
 */
public class StringValidator extends AbstractStringValidator implements IAnnotationValidator<CheckString> {

    @Override
    public void checkContent(String value, String cname, String message) {
        // 不需要实现.
    }

    public void check(Object value, String cacheKey, CheckString anno) {
        if (anno.isNull()) {
            if (!(value instanceof String)) {
                throw new CheckFailureException(anno.cname() + " 必须是字符串");
            }
            if (StringUtils.isBlank((String) value)) {
                return;
            }
        }

        // ValidateUtil.checkAnnotation(value, anno);
        Rule r = ruleCache.get(cacheKey); 
        if (r == null) { 
            r = RuleBuilder.build();
            r.setName(anno.cname()).isNull(anno.isNull()).length(anno.length()).setEnum(anno.value()).setErrorMessage(anno.message());
            ruleCache.put(cacheKey, r);
        }
        Validate.check(value, r);
        check(value, anno.cname(), anno.message());
    }

}
