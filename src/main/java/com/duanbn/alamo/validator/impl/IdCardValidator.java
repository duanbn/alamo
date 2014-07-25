package com.duanbn.alamo.validator.impl;

import com.duanbn.alamo.Rule;
import com.duanbn.alamo.RuleBuilder;
import com.duanbn.alamo.StringUtils;
import com.duanbn.alamo.Validate;
import com.duanbn.alamo.annotation.CheckIdCard;
import com.duanbn.alamo.exception.DefineRuleException;
import com.duanbn.alamo.exception.TypeErrorException;
import com.duanbn.alamo.validator.AbstractStringValidator;
import com.duanbn.alamo.validator.IAnnotationValidator;

/**
 * 身份证校验器. 校验变量值是否符合身份证格式.
 * 
 * @author duanbn
 * @since 1.0.0
 */
public class IdCardValidator extends AbstractStringValidator implements IAnnotationValidator<CheckIdCard> {

    @Override
    public void checkContent(String value, String cname, String message) {
        if (!value.matches("^(\\d{6})(\\d{4})(\\d{2})(\\d{2})(\\d{3})([0-9]|X)$")) {
            if (StringUtils.isNotBlank(message)) {
                throw new TypeErrorException(message);
            } else {
                throw new TypeErrorException(cname + "不是有效的身份证号码");
            }
        }
    }

    public void check(Object value, String cacheKey, CheckIdCard anno) {
        if (anno.isNull()) {
            if (!(value instanceof String)) {
                throw new DefineRuleException(anno.cname() + " 必须是字符串");
            }
            if (StringUtils.isBlank((String) value)) {
                return;
            }
        }

        // ValidateUtil.checkAnnotation(value, anno);
        Rule r = ruleCache.get(cacheKey);
        if (r == null) {
            r = RuleBuilder.build();
            r.setName(anno.cname()).isNull(anno.isNull()).setEnum(anno.value()).setErrorMessage(anno.message());
            ruleCache.put(cacheKey, r);
        }
        Validate.check(value, r);
        check(value, anno.cname(), anno.message());
    }

}
