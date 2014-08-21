package com.duanbn.alamo.validator.noparam.impl;

import com.duanbn.alamo.Rule;
import com.duanbn.alamo.RuleBuilder;
import com.duanbn.alamo.StringUtils;
import com.duanbn.alamo.Validate;
import com.duanbn.alamo.annotation.CheckMobile;
import com.duanbn.alamo.exception.CheckFailureException;
import com.duanbn.alamo.exception.TypeErrorException;
import com.duanbn.alamo.validator.AbstractStringValidator;
import com.duanbn.alamo.validator.IAnnotationValidator;

/**
 * 手机号码校验器. 校验字符串格式是否符合手机号码格式.
 * 
 * @author duanbn
 * @since 1.0.0
 */
public class MobileValidator extends AbstractStringValidator implements IAnnotationValidator<CheckMobile> {

	@Override
	public void checkContent(String value, String cname, String message) {
		if (!value.matches("^((\\(\\d{2,3}\\))|(\\d{3}\\-))?((1[345]\\d{9})|(18\\d{9}))$")) {
			if (StringUtils.isNotBlank(message)) {
				throw new CheckFailureException(message);
			} else {
				throw new CheckFailureException(cname + "不是有效的号码");
			}
		}
	}

	public void check(Object value, String cacheKey, CheckMobile anno) {
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
