package com.duanbn.alamo.validator.noparam.impl;

import java.text.NumberFormat;
import java.text.ParseException;

import com.duanbn.alamo.Rule;
import com.duanbn.alamo.RuleBuilder;
import com.duanbn.alamo.StringUtils;
import com.duanbn.alamo.Validate;
import com.duanbn.alamo.annotation.CheckStringNumber;
import com.duanbn.alamo.exception.CheckFailureException;
import com.duanbn.alamo.exception.TypeErrorException;
import com.duanbn.alamo.validator.AbstractStringValidator;
import com.duanbn.alamo.validator.IAnnotationValidator;

/**
 * 字符串内容是数字的校验器. 校验字符串的内容是不是一个数字，不是数字抛出异常.
 * 
 * @author duanbn
 * @since 1.0.0
 */
public class StringNumberValidator extends AbstractStringValidator implements IAnnotationValidator<CheckStringNumber> {

	private static final NumberFormat numberFormat = NumberFormat.getInstance();

	@Override
	public void checkContent(String value, String cname, String message) {
		if (!value.matches("^[-+]?\\d*\\.?\\d*$")) {
			if (StringUtils.isNotBlank(message)) {
				throw new CheckFailureException(message);
			} else {
				throw new CheckFailureException(cname + "不是数字");
			}
		}
	}

	public void check(Object value, String cacheKey, CheckStringNumber anno) {
		if (StringUtils.isBlank((String) value)) {
			if (anno.isNull()) {
				return;
			} else {
				throw new CheckFailureException(anno.cname() + " 不能为空值");
			}
		}
		if (!(value instanceof String)) {
			throw new TypeErrorException(anno.cname() + " 必须是字符串");
		}

		try {
			Rule r = ruleCache.get(cacheKey);
			if (r == null) {
				r = RuleBuilder.build();
				r.setName(anno.cname()).length(anno.length()).range(anno.range())
						.setEnum(anno.value()).setErrorMessage(anno.message());
				ruleCache.put(cacheKey, r);
			}
			Validate.check(numberFormat.parse((String) value), r);

		} catch (ParseException e) {
			if (StringUtils.isNotBlank(anno.message())) {
				throw new TypeErrorException(anno.message());
			} else {
				throw new TypeErrorException(anno.cname() + "不是数字");
			}
		}
		check(value, anno.cname(), anno.message());
	}

}
