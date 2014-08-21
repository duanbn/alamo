package com.duanbn.alamo.validator.param.impl;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;

import com.duanbn.alamo.Range;
import com.duanbn.alamo.StringUtils;
import com.duanbn.alamo.exception.CheckFailureException;
import com.duanbn.alamo.exception.RangeErrorException;
import com.duanbn.alamo.validator.param.ParamValidator;

/**
 * 数值范围校验器. 校验一个数值的值是否在一个指定的范围中. <b>范围校验只支持连续区间的校验，例：大于2并且小于4，
 * 不支持离散区间校验例：大于4或者小于2</b>
 * 
 * @author duanbn
 * @since 1.0.0
 */
public class RangeValidator extends ParamValidator<Range> {

	private static final NumberFormat numberFormat = NumberFormat.getInstance();

	@Override
	public void check(Object value, List<Range> params, String cname, String message) {
		Number n = null;
		if (value instanceof String) {
			try {
				n = numberFormat.parse((String) value);
			} catch (ParseException e) {
				throw new CheckFailureException("范围校验的被校验值必须是数字或者数字字符串");
			} catch (NumberFormatException e) {
				throw new CheckFailureException("范围校验的被校验值必须是数字或者数字字符串");
			}
		} else if (value instanceof Number) {
			n = (Number) value;
		} else {
			throw new CheckFailureException("范围校验的被校验值必须是数字");
		}

		for (Range range : params) {
			if (range.in(n)) {
				continue;
			} else {
				throw _ENUM_EXCEPTION(value, params, cname, message);
			}
		}
	}

	private RangeErrorException _ENUM_EXCEPTION(Object value, List<Range> params, String cname, String message) {
		RangeErrorException e = null;
		if (StringUtils.isNotBlank(message)) {
			e = new RangeErrorException(message);
		} else {
			e = new RangeErrorException(cname + " 不在指定的数值范围内");
		}
		e.setValue(value);
		e.setRange(params);
		return e;
	}

}
