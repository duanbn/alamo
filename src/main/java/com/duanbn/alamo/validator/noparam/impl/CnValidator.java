package com.duanbn.alamo.validator.noparam.impl;

import com.duanbn.alamo.Rule;
import com.duanbn.alamo.RuleBuilder;
import com.duanbn.alamo.StringUtils;
import com.duanbn.alamo.Validate;
import com.duanbn.alamo.annotation.CheckCn;
import com.duanbn.alamo.exception.CheckFailureException;
import com.duanbn.alamo.exception.TypeErrorException;
import com.duanbn.alamo.validator.AbstractStringValidator;
import com.duanbn.alamo.validator.IAnnotationValidator;

/**
 * 中文校验器. 校验变量值是否是中文.
 * 
 * @author duanbn
 * @since 1.0.0
 */
public class CnValidator extends AbstractStringValidator implements IAnnotationValidator<CheckCn> {

    @Override
    public void checkContent(String value, String cname, String message) {
        if (!isChinese(value)) {
            if (StringUtils.isNotBlank(message)) {
                throw new CheckFailureException(message);
            } else {
                throw new CheckFailureException(cname + "不是中文");
            }
        }
    }

    public void check(Object value, String cacheKey, CheckCn anno) {
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

    private boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
            return true;
        }
        return false;
    }

    private boolean isChinese(String value) {
        char[] ch = value.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (isChinese(c)) {
                return true;
            }
        }
        return false;
    }

}
