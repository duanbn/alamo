package com.duanbn.alamo.validator.impl;

import com.duanbn.alamo.Rule;
import com.duanbn.alamo.RuleBuilder;
import com.duanbn.alamo.StringUtils;
import com.duanbn.alamo.Validate;
import com.duanbn.alamo.annotation.CheckTelphone;
import com.duanbn.alamo.exception.DefineRuleException;
import com.duanbn.alamo.exception.TypeErrorException;
import com.duanbn.alamo.validator.AbstractStringValidator;
import com.duanbn.alamo.validator.IAnnotationValidator;

/**
 * 电话号码号码校验器.
 * 校验字符串格式是否符合电话号码格式. 及支持手机也支持座机.
 *
 * @author duanbn
 * @since 1.0.0
 */
public class TelphoneValidator extends AbstractStringValidator implements IAnnotationValidator<CheckTelphone> {

    @Override
    public void checkContent(String value, String cname, String message) {
        if (!value.matches("^\\d{3}-\\d{8}|\\d{4}-\\d{7,8}|(((\\d{2,3}))|(\\d{3}-))?((1[345]\\d{9})|(18\\d{9}))$")) {
            if (StringUtils.isNotBlank(message)) {
                throw new TypeErrorException(message);
            } else {
                throw new TypeErrorException(cname + "不是有效的电话号码");
            }
        }
    }

    public void check(Object value, String cacheKey, CheckTelphone anno) {
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
