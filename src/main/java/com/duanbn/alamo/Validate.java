package com.duanbn.alamo;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.duanbn.alamo.annotation.CheckObject;
import com.duanbn.alamo.annotation.CheckPOJO;
import com.duanbn.alamo.annotation.CustomCheck;
import com.duanbn.alamo.exception.CheckErrorException;
import com.duanbn.alamo.exception.CheckFailureException;
import com.duanbn.alamo.exception.CustomErrorException;
import com.duanbn.alamo.exception.DefineRuleException;
import com.duanbn.alamo.exception.NullException;
import com.duanbn.alamo.validator.CustomValidator;
import com.duanbn.alamo.validator.IAnnotationValidator;
import com.duanbn.alamo.validator.IValidator;
import com.duanbn.alamo.validator.NoParamValidator;
import com.duanbn.alamo.validator.ParamValidator;
import com.duanbn.alamo.validator.impl.NullValidator;

/**
 * 校验工具类. 校验工具类对外提供的门面类，通过此类触发校验动作. 使用方法如下:<br/>
 * 
 * @author duanbn.
 * @since 1.0.0
 */
public class Validate {

	public static final int MAX_CHECKLEVEL = 3;

	/**
	 * 被标注属性缓存.
	 */
	private static final Map<Class<?>, Field[]> fieldCache = new ConcurrentHashMap<Class<?>, Field[]>();
	private static final Map<String, Annotation[]> annoCache = new ConcurrentHashMap<String, Annotation[]>();
	private static final Map<String, Class<? extends Annotation>> annoClassCache = new ConcurrentHashMap<String, Class<? extends Annotation>>();

	private static final Map<String, CustomValidator<?>> customCache;

	private static final Object[] primitives = new Object[] { Boolean.TYPE, Boolean.class, Byte.TYPE, Byte.class,
			Short.TYPE, Short.class, Integer.TYPE, Integer.class, Long.TYPE, Long.class, Float.TYPE, Float.class,
			Double.TYPE, Double.class, String.class, Character.TYPE, Character.class };
	private static final Set<Object> primitiveType;

	static {
		primitiveType = new HashSet<Object>(Arrays.asList(primitives));
		customCache = new HashMap<String, CustomValidator<?>>();
	}

	private static boolean isPrimitive(Object value) {
		return primitiveType.contains(value.getClass());
	}

	/**
	 * 对普通的对象进行校验. 被校验的对象是被校验注解标注的. 通过对对象的内部属性进行注解来定义校验规则. 当校验失败时抛出异常.
	 * 
	 * @param pojo
	 *            被校验的对象.
	 * 
	 * @throws CheckErrorExceptionBak
	 *             校验失败, 被校验的对象不符合定义的校验规则, 此错误因为业务逻辑引起.
	 * @throws DefineRuleException
	 *             校验失败, 校验规则定义错误, 此错误是因为校验代码引起.
	 * 
	 * @see com.kaola.validation.validator
	 */
	public static void check(Object pojo, String... ignoreFields) {
		StringBuilder errorInfo = new StringBuilder();
		_check(pojo, 0, errorInfo, null, ignoreFields);
		if (StringUtils.isNotBlank(errorInfo.toString())) {
			throw new CheckFailureException(errorInfo.toString().trim());
		}
	}

	/**
	 * 对普通的对象进行校验. 被校验的对象是被校验注解标注的. 通过对对象的内部属性进行注解来定义校验规则.
	 * 当校验失败时执行ICheckFailHandler接口的方法.
	 * 
	 * @param pojo
	 *            被校验的对象.
	 * @param handler
	 *            校验失败处理器.
	 * 
	 * @throws CheckErrorExceptionBak
	 *             校验失败, 被校验的对象不符合定义的校验规则, 此错误因为业务逻辑引起.
	 * @throws DefineRuleException
	 *             校验失败, 校验规则定义错误, 此错误是因为校验代码引起.
	 * 
	 * @see com.kaola.validation.validator
	 */
	public static void check(Object pojo, ICheckFailHandler handler, String... ignoreFields) {
		StringBuilder errorInfo = new StringBuilder();
		_check(pojo, 0, errorInfo, handler, ignoreFields);
		if (StringUtils.isNotBlank(errorInfo.toString())) {
			throw new CheckFailureException(errorInfo.toString().trim());
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static void _check(Object pojo, int level, StringBuilder errorInfo, ICheckFailHandler handler,
			String... ignoreFields) {
		if (level > MAX_CHECKLEVEL) {
			throw new DefineRuleException("校验深度超过指定阈值.");
		}

		Class pojoClass = pojo.getClass();
		CheckPOJO checkPOJO = (CheckPOJO) pojoClass.getAnnotation(CheckPOJO.class);
		boolean isCheck = checkPOJO == null ? true : checkPOJO.value();
		if (!isCheck) {
			return;
		}
		String mode = checkPOJO == null ? CheckPOJO.STEP : checkPOJO.mode();
		if (!mode.equals(CheckPOJO.ALL) && !mode.equals(CheckPOJO.STEP)) {
			throw new DefineRuleException("@CheckPOJO的mode属性只能是STEP | ALL");
		}

		// get fields
		Field[] fields = fieldCache.get(pojoClass);
		if (fields == null) {
			fields = pojoClass.getDeclaredFields();
			fieldCache.put(pojoClass, fields);
		}

		Object fvalue = null;
		Annotation[] annos = null;
		Annotation anno = null;
		Class<? extends Annotation> annoClass = null;
		StringBuilder cacheKey = new StringBuilder();
		for (Field field : fields) {
			// 运行时忽略字段校验
			if (StringUtils.find(ignoreFields, field.getName()) > -1) {
				continue;
			}

			if (Modifier.isFinal(field.getModifiers())) {
				continue;
			}
			if (!field.isAccessible()) {
				field.setAccessible(true);
			}

			cacheKey.append(pojoClass.getName()).append(".").append(field.getName());
			try {
				fvalue = field.get(pojo);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}

			try {
				annos = getAnnos(cacheKey.toString(), field);

				if (annos != null && annos.length > 0) {
					if (annos.length > 1) {
						throw new DefineRuleException("不支持多注解校验");
					}
					anno = annos[0];
					annoClass = annoClassCache.get(cacheKey.toString());
					if (annoClass == null) {
						annoClass = anno.annotationType();
						annoClassCache.put(cacheKey.toString(), annoClass);
					}

					// custom check
					if (annoClass == CustomCheck.class) {
						_checkCustom(fvalue, anno);
						continue;
					}

					// check null
					if (fvalue == null) {
						if (ValidateUtil.allowNull(anno)) {
							continue;
						} else if (annoClass == CheckObject.class) {
							if (StringUtils.isNotBlank(((CheckObject) anno).message())) {
								throw new NullException(((CheckObject) anno).message());
							} else if (StringUtils.isNotBlank(((CheckObject) anno).cname())) {
								throw new NullException(((CheckObject) anno).cname() + " 不能为Null");
							} else {
								throw new NullException(field.getName() + " 不能为Null");
							}
						} else {
							IAnnotationValidator validator = ValidatorLoader.findValidatorByAnno(annoClass);
							validator.check(fvalue, cacheKey.toString(), anno);
						}
					}

					// check
					if (isPrimitive(fvalue)) {
						IAnnotationValidator validator = ValidatorLoader.findValidatorByAnno(annoClass);
						validator.check(fvalue, cacheKey.toString(), anno);
					} else if (annoClass == CheckObject.class) {
						level++;
						_check(fvalue, level, errorInfo, handler);
						level--;
					} else {
						throw new DefineRuleException("不被支持的变量值类型 " + field.getType());
					}
				}
			} catch (CheckFailureException e) {
				if (handler != null) {
					try {
						handler.handle(pojo, field.getName(), fvalue, e);
					} catch (CheckFailureException cee) {
						if (mode.equals(CheckPOJO.STEP)) {
							throw cee;
						} else if (mode.equals(CheckPOJO.ALL)) {
							errorInfo.append("\n").append(cee.getMessage());
						}
					}
				} else {
					if (mode.equals(CheckPOJO.STEP)) {
						throw e;
					} else if (mode.equals(CheckPOJO.ALL)) {
						errorInfo.append("\n").append(e.getMessage());
					}
				}
			} finally {
				cacheKey.setLength(0);
			}
		}

	}

	private static Annotation[] getAnnos(String cacheKey, Field field) {
		Annotation[] annos = annoCache.get(cacheKey);
		if (annos == null) {
			annos = field.getDeclaredAnnotations();

			// 过滤掉不是Check的注解
			List<Annotation> annoList = new ArrayList<Annotation>();
			for (Annotation anno : annos) {
				if (ValidatorLoader.isCheckAnno(anno)) {
					annoList.add(anno);
				}
			}

			annos = annoList.toArray(new Annotation[annoList.size()]);
			annoCache.put(cacheKey, annos);
		}
		return annos;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static void _checkCustom(Object fvalue, Annotation anno) {
		CustomCheck cc = (CustomCheck) anno;
		String validatorClass = cc.classname();
		CustomValidator cusValidator = customCache.get(validatorClass);
		// get from cache.
		if (cusValidator == null) {
			try {
				cusValidator = (CustomValidator) Class.forName(validatorClass).newInstance();
			} catch (Exception e) {
				throw new DefineRuleException("找不到用户自定义校验器:" + validatorClass);
			}
			customCache.put(validatorClass, cusValidator);
		}
		try {
			cusValidator.check(fvalue, cc.cname(), cc.message());
		} catch (CheckErrorException e) {
			throw new CustomErrorException(e.getMessage());
		}
	}

	/**
	 * 对单值进行校验. 当校验规则允许为空值时，被校验值恰好也是空值则返回校验成功. 不进行其它的校验.
	 * 如果值是字符串则空值被视为空值,当校验失败时会抛出异常.
	 * 
	 * @param value
	 *            被校验的值.
	 * @param rule
	 *            校验规则.
	 * 
	 * @throws CheckErrorExceptionBak
	 *             校验失败, 被校验的对象不符合定义的校验规则, 此错误因为业务逻辑引起.
	 * @throws DefineRuleException
	 *             校验失败, 校验规则定义错误, 此错误是因为校验代码引起.
	 * 
	 * @see Rule
	 * @see RuleBuilder
	 */
	public static void check(Object value, Rule rule) {
		check(value, rule.getName(), rule);
	}

	/**
	 * 对单值进行校验. 当校验规则允许为空值时，被校验值恰好也是空值则返回校验成功. 不进行其它的校验.
	 * 如果值是字符串则空值被视为空值,当校验失败时会抛出异常.
	 * 
	 * @param value
	 *            被校验的值.
	 * @param cname
	 *            被校验值的中文名.
	 * @param rule
	 *            校验规则.
	 * 
	 * @throws CheckErrorExceptionBak
	 *             校验失败, 被校验的对象不符合定义的校验规则, 此错误因为业务逻辑引起.
	 * @throws DefineRuleException
	 *             校验失败, 校验规则定义错误, 此错误是因为校验代码引起.
	 * 
	 * @see Rule
	 * @see RuleBuilder
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void check(Object value, String cname, Rule rule) {
		String fcname = String.valueOf(value);
		if (StringUtils.isNotBlank(rule.getName())) {
			fcname = rule.getName();
		}
		if (StringUtils.isNotBlank(cname)) {
			fcname = cname;
		}
		Map<Class<? extends IValidator>, List<Object>> meta = rule.getMeta();

		// 对空值做特殊处理.
		if (value == null || (value instanceof String && StringUtils.isBlank((String) value))) {
			if (meta.containsKey(NullValidator.class)) {
				ParamValidator nullValidator = (ParamValidator) ValidatorLoader.findValidator(NullValidator.class);
				nullValidator.check(value, meta.get(NullValidator.class), fcname, rule.getErrorMessage());
			}
			return;
		}

		IValidator validator = null;
		for (Map.Entry<Class<? extends IValidator>, List<Object>> entry : meta.entrySet()) {
			validator = ValidatorLoader.findValidator(entry.getKey());
			if (validator instanceof NoParamValidator) {
				((NoParamValidator) validator).check(value, fcname, rule.getErrorMessage());
			} else if (validator instanceof ParamValidator) {
				((ParamValidator) validator).check(value, entry.getValue(), fcname, rule.getErrorMessage());
			} else {
				throw new DefineRuleException("Rule中包含无法识别的校验器 class=" + entry.getKey());
			}
		}
	}

	/**
	 * 用户自定义校验.
	 * 
	 * @param value
	 *            被验证的值.
	 * @param validator
	 *            用户自定义校验器.
	 * 
	 * @see CustomValidator 自定义校验器
	 */
	@SuppressWarnings("rawtypes")
	public static void check(Object value, CustomValidator validator) {
		check(value, validator, null, null);
	}

	/**
	 * 用户自定义校验.
	 * 
	 * @param value
	 *            被验证的值.
	 * @param validator
	 *            用户自定义校验器.
	 * @param cname
	 *            被校验变量的中文名
	 * 
	 * @see CustomValidator 自定义校验器
	 */
	@SuppressWarnings("rawtypes")
	public static void check(Object value, CustomValidator validator, String cname) {
		check(value, validator, cname, null);
	}

	/**
	 * 用户自定义校验.
	 * 
	 * @param value
	 *            被验证的值.
	 * @param validator
	 *            用户自定义校验器.
	 * @param cname
	 *            被校验变量的中文名
	 * @param message
	 *            自定义错误提示信息
	 * 
	 * @see CustomValidator 自定义校验器
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void check(Object value, CustomValidator validator, String cname, String message) {
		if (validator == null) {
			throw new IllegalArgumentException("校验器参数不能为null");
		}

		try {
			validator.check(value, cname, message);
		} catch (CheckErrorException e) {
			throw new CustomErrorException(e.getMessage());
		}
	}

}
