package com.duanbn.alamo;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import com.duanbn.alamo.annotation.CheckCn;
import com.duanbn.alamo.annotation.CheckEmail;
import com.duanbn.alamo.annotation.CheckEn;
import com.duanbn.alamo.annotation.CheckIP;
import com.duanbn.alamo.annotation.CheckIdCard;
import com.duanbn.alamo.annotation.CheckIsNull;
import com.duanbn.alamo.annotation.CheckMobile;
import com.duanbn.alamo.annotation.CheckNumber;
import com.duanbn.alamo.annotation.CheckObject;
import com.duanbn.alamo.annotation.CheckString;
import com.duanbn.alamo.annotation.CheckStringNumber;
import com.duanbn.alamo.annotation.CheckTelphone;
import com.duanbn.alamo.annotation.CheckURL;
import com.duanbn.alamo.annotation.CheckZip;
import com.duanbn.alamo.exception.DefineRuleException;
import com.duanbn.alamo.validator.IAnnotationValidator;
import com.duanbn.alamo.validator.IValidator;
import com.duanbn.alamo.validator.noparam.impl.CnValidator;
import com.duanbn.alamo.validator.noparam.impl.EmailValidator;
import com.duanbn.alamo.validator.noparam.impl.EnValidator;
import com.duanbn.alamo.validator.noparam.impl.IPValidator;
import com.duanbn.alamo.validator.noparam.impl.IdCardValidator;
import com.duanbn.alamo.validator.noparam.impl.MobileValidator;
import com.duanbn.alamo.validator.noparam.impl.NumberValidator;
import com.duanbn.alamo.validator.noparam.impl.StringNumberValidator;
import com.duanbn.alamo.validator.noparam.impl.StringValidator;
import com.duanbn.alamo.validator.noparam.impl.TelphoneValidator;
import com.duanbn.alamo.validator.noparam.impl.URLValidator;
import com.duanbn.alamo.validator.noparam.impl.ZipValidator;
import com.duanbn.alamo.validator.param.impl.EnumValidator;
import com.duanbn.alamo.validator.param.impl.LengthValidator;
import com.duanbn.alamo.validator.param.impl.NullValidator;
import com.duanbn.alamo.validator.param.impl.RangeValidator;
import com.duanbn.alamo.validator.param.impl.RegexValidator;

/**
 * 校验器的加载器. 负责将所有已经定义的校验器加载到内存. 相当与一个容器类.
 * 
 * @author duanbn.
 * @since 1.0.0
 */
public class ValidatorLoader {

	private static final Map<Class<? extends IValidator>, IValidator> validators;
	private static final Map<Class<? extends Annotation>, IAnnotationValidator> annoValidators;

	static {
		validators = new HashMap<Class<? extends IValidator>, IValidator>();
		annoValidators = new HashMap<Class<? extends Annotation>, IAnnotationValidator>();

		load();
	}

	public static void load() {
		// no param
		validators.put(CnValidator.class, new CnValidator());
		annoValidators.put(CheckCn.class, new CnValidator());

		validators.put(EmailValidator.class, new EmailValidator());
		annoValidators.put(CheckEmail.class, new EmailValidator());

		validators.put(EnValidator.class, new EnValidator());
		annoValidators.put(CheckEn.class, new EnValidator());

		validators.put(IdCardValidator.class, new IdCardValidator());
		annoValidators.put(CheckIdCard.class, new IdCardValidator());

		validators.put(IPValidator.class, new IPValidator());
		annoValidators.put(CheckIP.class, new IPValidator());

		validators.put(MobileValidator.class, new MobileValidator());
		annoValidators.put(CheckMobile.class, new MobileValidator());

		validators.put(TelphoneValidator.class, new TelphoneValidator());
		annoValidators.put(CheckTelphone.class, new TelphoneValidator());

		validators.put(NumberValidator.class, new NumberValidator());
		annoValidators.put(CheckNumber.class, new NumberValidator());

		validators.put(StringValidator.class, new StringValidator());
		annoValidators.put(CheckString.class, new StringValidator());

		validators.put(StringNumberValidator.class, new StringNumberValidator());
		annoValidators.put(CheckStringNumber.class, new StringNumberValidator());

		validators.put(URLValidator.class, new URLValidator());
		annoValidators.put(CheckURL.class, new URLValidator());

		validators.put(ZipValidator.class, new ZipValidator());
		annoValidators.put(CheckZip.class, new ZipValidator());

		// param
		validators.put(EnumValidator.class, new EnumValidator());

		validators.put(LengthValidator.class, new LengthValidator());

		validators.put(RangeValidator.class, new RangeValidator());

		validators.put(RegexValidator.class, new RegexValidator());

		validators.put(NullValidator.class, new NullValidator());
		annoValidators.put(CheckIsNull.class, new NullValidator());
	}

	public static boolean isCheckAnno(Annotation anno) {
		if (anno.annotationType() == CheckObject.class) {
			return true;
		}

		return annoValidators.containsKey(anno.annotationType());
	}

	public static <T extends IValidator> T findValidator(Class<T> validatorClass) {
		IValidator validator = validators.get(validatorClass);
		if (validator == null) {
			throw new DefineRuleException("无法识别的校验器 " + validatorClass);
		}
		return (T) validator;
	}

	public static IAnnotationValidator findValidatorByAnno(Class<? extends Annotation> annoClass) {
		IAnnotationValidator validator = annoValidators.get(annoClass);
		if (validator == null) {
			throw new DefineRuleException("无法识别的校验注解 " + annoClass);
		}
		return validator;
	}

}
