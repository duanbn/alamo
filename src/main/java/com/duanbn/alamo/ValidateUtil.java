package com.duanbn.alamo;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.duanbn.alamo.exception.DefineRuleException;
import com.duanbn.alamo.validator.impl.EnumValidator;
import com.duanbn.alamo.validator.impl.LengthValidator;
import com.duanbn.alamo.validator.impl.NullValidator;
import com.duanbn.alamo.validator.impl.RangeValidator;

public class ValidateUtil {

	private static final NumberFormat numberFormat = NumberFormat.getInstance();

	private static final String LENGTH_MSG = "区间表达式格式错误, 表达式只能是数字或者(10,20), (10,20], [10,20], (*,20)";
	private static final Pattern p = Pattern.compile("[-+]?\\d+\\.?\\d*|\\*");

	/**
	 * 解析区间范围表达式. 区间范围表达式"()"表示开区间, "[]"表示闭区间,"*"表示任意正整数. 表达式中不允许有任何空格.
	 * 例:(10,20), (10,20], [10,20], (*,20)
	 */
	public static List<Range> parseRange(String lengthEl, String cname) {
		if (StringUtils.isBlank(lengthEl)) {
			return null;
		}
		lengthEl = StringUtils.removeBlank(lengthEl);

		List<Range> range = new ArrayList<Range>(2);
		try {
			if (lengthEl.matches("^[-+]?\\d*\\.?\\d*$")) {
				range.add(new Range(Opt.EQ, numberFormat.parse(lengthEl)));
			} else if (lengthEl.matches("^((\\(|\\[)(([-+]?\\d+\\.?\\d*|\\*),([-+]?\\d+\\.?\\d*|\\*))(\\)|\\]))$")) {
				Matcher m = p.matcher(lengthEl);
				m.find();
				String low = m.group();
				m.find();
				String high = m.group();
				if (!low.equals("*")) {
					if (Character.toString(lengthEl.charAt(0)).trim().equals("(")) {
						range.add(new Range(Opt.GT, numberFormat.parse(low)));
					} else {
						range.add(new Range(Opt.GTE, numberFormat.parse(low)));
					}
				}
				if (!high.equals("*")) {
					if (Character.toString(lengthEl.charAt(4)).trim().equals(")")) {
						range.add(new Range(Opt.LT, numberFormat.parse(high)));
					} else {
						range.add(new Range(Opt.LTE, numberFormat.parse(high)));
					}
				}

			} else {
				throw new DefineRuleException(cname + " " + LENGTH_MSG);
			}
		} catch (ParseException e) {
			throw new DefineRuleException(cname + " " + LENGTH_MSG);
		}

		return range;
	}

	/**
	 * 解析枚举表达式. 以逗号分割的字符串.
	 */
	public static List<Object> parseEnum(String enumEl) {
		Object[] enums = enumEl.split(",");
		return Arrays.asList(enums);
	}

	/**
	 * 一个注解是否可以为null. 如果注解中没有isNull属性则默认认为是可以为null的.
	 */
	public static boolean allowNull(Annotation anno) {
		if (anno == null) {
			throw new IllegalArgumentException("参数不能为null");
		}

		try {
			Method m = anno.getClass().getDeclaredMethod("isNull");
			return (Boolean) m.invoke(anno);
		} catch (Exception e) {
			return true;
		}
	}

	/**
	 * 共用的注解进行校验.
	 */
	@Deprecated
	public static void checkAnnotation(Object value, Annotation anno) {
		try {
			Method cnameMethod = anno.getClass().getDeclaredMethod("cname");
			String cname = (String) cnameMethod.invoke(anno);

			String message = null;
			try {
				Method messageMethod = anno.getClass().getDeclaredMethod("message");
				message = (String) messageMethod.invoke(anno);
			} catch (NoSuchMethodException e) {
			}

			// 空值需要首先检查
			Method nullMethod = anno.getClass().getDeclaredMethod("isNull");
			boolean isNull = (Boolean) nullMethod.invoke(anno);
			ValidatorLoader.findValidator(NullValidator.class).check(value, Arrays.asList(new Boolean[] { isNull }),
					cname, message);

			for (Method m : anno.getClass().getDeclaredMethods()) {
				if (m.getName().equals("length")) {
					String lengthEl = (String) m.invoke(anno);
					List<Range> range = ValidateUtil.parseRange(lengthEl, cname);
					if (range != null)
						ValidatorLoader.findValidator(LengthValidator.class).check(value, range, cname, message);
				} else if (m.getName().equals("value")) {
					String enumEl = (String) m.invoke(anno);
					List<Object> enums = ValidateUtil.parseEnum(enumEl);
					ValidatorLoader.findValidator(EnumValidator.class).check(value, enums, cname, message);
				} else if (m.getName().equals("range")) {
					String rangeEl = (String) m.invoke(anno);
					List<Range> range = ValidateUtil.parseRange(rangeEl, cname);
					if (range != null)
						ValidatorLoader.findValidator(RangeValidator.class).check(value, range, cname, message);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 比较两个Number类型的数字.
	 */
	public static double compare(Number n1, Number n2) {
		if (n1 instanceof Short) {
			if (n2 instanceof Short) {
				return n1.shortValue() - n2.shortValue();
			} else if (n2 instanceof Integer) {
				return n1.shortValue() - n2.intValue();
			} else if (n2 instanceof Long) {
				return n1.shortValue() - n2.longValue();
			} else if (n2 instanceof Float) {
				return n1.shortValue() - n2.floatValue();
			} else if (n2 instanceof Double) {
				return n1.shortValue() - n2.doubleValue();
			}
		} else if (n1 instanceof Integer) {
			if (n2 instanceof Short) {
				return n1.intValue() - n2.shortValue();
			} else if (n2 instanceof Integer) {
				return n1.intValue() - n2.intValue();
			} else if (n2 instanceof Long) {
				return n1.intValue() - n2.longValue();
			} else if (n2 instanceof Float) {
				return n1.intValue() - n2.floatValue();
			} else if (n2 instanceof Double) {
				return n1.intValue() - n2.doubleValue();
			}
		} else if (n1 instanceof Long) {
			if (n2 instanceof Short) {
				return n1.longValue() - n2.shortValue();
			} else if (n2 instanceof Integer) {
				return n1.longValue() - n2.intValue();
			} else if (n2 instanceof Long) {
				return n1.longValue() - n2.longValue();
			} else if (n2 instanceof Float) {
				return n1.longValue() - n2.floatValue();
			} else if (n2 instanceof Double) {
				return n1.longValue() - n2.doubleValue();
			}
		} else if (n1 instanceof Float) {
			if (n2 instanceof Short) {
				return n1.floatValue() - n2.shortValue();
			} else if (n2 instanceof Integer) {
				return n1.floatValue() - n2.intValue();
			} else if (n2 instanceof Long) {
				return n1.floatValue() - n2.longValue();
			} else if (n2 instanceof Float) {
				return n1.floatValue() - n2.floatValue();
			} else if (n2 instanceof Double) {
				return n1.floatValue() - n2.doubleValue();
			}
		} else if (n1 instanceof Double) {
			if (n2 instanceof Short) {
				return n1.doubleValue() - n2.shortValue();
			} else if (n2 instanceof Integer) {
				return n1.doubleValue() - n2.intValue();
			} else if (n2 instanceof Long) {
				return n1.doubleValue() - n2.longValue();
			} else if (n2 instanceof Float) {
				return n1.doubleValue() - n2.floatValue();
			} else if (n2 instanceof Double) {
				return n1.doubleValue() - n2.doubleValue();
			}
		}

		throw new RuntimeException("数值类型比较错误");
	}

}
