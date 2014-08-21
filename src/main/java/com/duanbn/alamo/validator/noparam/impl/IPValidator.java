package com.duanbn.alamo.validator.noparam.impl;

import com.duanbn.alamo.Rule;
import com.duanbn.alamo.RuleBuilder;
import com.duanbn.alamo.StringUtils;
import com.duanbn.alamo.Validate;
import com.duanbn.alamo.annotation.CheckIP;
import com.duanbn.alamo.exception.CheckFailureException;
import com.duanbn.alamo.exception.TypeErrorException;
import com.duanbn.alamo.validator.AbstractStringValidator;
import com.duanbn.alamo.validator.IAnnotationValidator;

/**
 * IP校验器.
 * 校验变量值是否符合IP格式.
 *
 * @author duanbn
 * @since 1.0.0
 */
public class IPValidator extends AbstractStringValidator implements IAnnotationValidator<CheckIP> {

    @Override
    public void checkContent(String value, String cname, String message) {
        if (!value.matches("^(0|[1-9]\\d?|[0-1]\\d{2}|2[0-4]\\d|25[0-5]).(0|[1-9]\\d?|[0-1]\\d{2}|2[0-4]\\d|25[0-5]).(0|[1-9]\\d?|[0-1]\\d{2}|2[0-4]\\d|25[0-5]).(0|[1-9]\\d?|[0-1]\\d{2}|2[0-4]\\d|25[0-5])$")) {
            if (StringUtils.isNotBlank(message)) {
                throw new CheckFailureException(message);
            } else {
                throw new CheckFailureException(cname + "不是有效的IP地址");
            }
        }
    }

    public void check(Object value, String cacheKey, CheckIP anno) {
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
            r.setName(anno.cname()).isNull(anno.isNull()).setEnum(anno.value()).setErrorMessage(anno.message());
            ruleCache.put(cacheKey, r);
        }
        Validate.check(value, r);
        check(value, anno.cname(), anno.message());
    }

}
