package com.duanbn.alamo.validator.impl;

import com.duanbn.alamo.Rule;
import com.duanbn.alamo.RuleBuilder;
import com.duanbn.alamo.StringUtils;
import com.duanbn.alamo.Validate;
import com.duanbn.alamo.annotation.CheckZip;
import com.duanbn.alamo.exception.DefineRuleException;
import com.duanbn.alamo.exception.TypeErrorException;
import com.duanbn.alamo.validator.AbstractStringValidator;
import com.duanbn.alamo.validator.IAnnotationValidator;

/**
 * 邮政编码校验器. 校验变量值是否是一个邮政编码.
 * 
 * @author duanbn
 * @since 1.0.0
 */
public class ZipValidator extends AbstractStringValidator implements IAnnotationValidator<CheckZip> {

    @Override
    public void checkContent(String value, String cname, String message) {
        if (!value.matches("[1-9]\\d{5}(?!\\d)")) {
            if (StringUtils.isNotBlank(message)) {
                throw new TypeErrorException(message);
            } else {
                throw new TypeErrorException(cname + "不是有效的邮政编码");
            }
        }
    }

    public void check(Object value, String cacheKey, CheckZip anno) {
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
