package com.duanbn.alamo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.duanbn.alamo.exception.DefineRuleException;
import com.duanbn.alamo.validator.IValidator;
import com.duanbn.alamo.validator.noparam.NoParamValidator;
import com.duanbn.alamo.validator.param.impl.EnumValidator;
import com.duanbn.alamo.validator.param.impl.LengthValidator;
import com.duanbn.alamo.validator.param.impl.NullValidator;
import com.duanbn.alamo.validator.param.impl.RangeValidator;
import com.duanbn.alamo.validator.param.impl.RegexValidator;

/**
 * 验证规则.
 * 定义一个验证规则, 一个验证规则表示一组校验动作. 
 *
 * @author duanbn.
 * @since 1.0.0
 */
public class Rule {

    /**
     * 校验器容器.
     */
    private Map<Class<? extends IValidator>, List<Object>> meta;

    /**
     * 变量值的别名.
     */
    private String name;

    /**
     * 自定义错误信息.
     */
    private String errorMessage;

    /**
     * 包范围的构造方法.
     * 限制调用者任意创建对象
     */
    Rule() {
        meta = new LinkedHashMap<Class<? extends IValidator>, List<Object>>();
    }

    /**
     * 清除已经设置的校验条件.
     */
    public void clean() {
        meta.clear();
        errorMessage = null;
    }

    public Map<Class<? extends IValidator>, List<Object>> getMeta() {
        return this.meta;
    }

    public Rule addValidator(Class<? extends NoParamValidator> validator) {
        meta.put(validator, null);
        return this;
    }
    
    public Rule isNull(boolean isNull) {
        meta.put(NullValidator.class, Arrays.asList(new Object[] {isNull}));
        return this;
    }

    public Rule setEnum(String values) {
        meta.put(EnumValidator.class, ValidateUtil.parseEnum(values));
        return this;
    }

    public Rule addEnum(Object... values) {
        meta.put(EnumValidator.class, Arrays.asList(values));
        return this;
    }

    public Rule range(String rangeEl) {
        if (StringUtils.isBlank(rangeEl)) {
            return this;
        }

        List<Object> params = new ArrayList<Object>();
        for (Range range : ValidateUtil.parseRange(rangeEl, this.name)) {
            params.add(range);
        }
        meta.put(RangeValidator.class, params);
        return this;
    }

    public Rule range(Number value, Opt opt) {
        if (value == null) {
            throw new IllegalArgumentException("范围值不能为空");
        }
        if (opt == null) {
            throw new IllegalArgumentException("opt不能为空");
        }

        List<Object> param = meta.get(RangeValidator.class);
        if (param == null) {
            param = new ArrayList<Object>();
            meta.put(RangeValidator.class, param);
        }
        param.add(new Range(opt, value));
        return this;
    }

    public Rule range(Number value1, Opt opt1, Number value2, Opt opt2) {
        List<Object> param = meta.get(RangeValidator.class);
        if (param == null) {
            param = new ArrayList<Object>();
            meta.put(RangeValidator.class, param);
        }
        param.add(new Range(opt1, value1));
        param.add(new Range(opt2, value2));
        return this;
    }

    public Rule regex(String regex) {
        if (meta.get(RegexValidator.class) != null) {
            throw new DefineRuleException("一个校验规则中不允许出现两个正则表达式规则");
        }
        meta.put(RegexValidator.class, Arrays.asList(new Object[] {regex}));
        return this;
    }

    public Rule length(String lengthEl) {
        if (StringUtils.isBlank(lengthEl)) {
            return this;
        }

        List<Object> params = new ArrayList<Object>();
        for (Range range : ValidateUtil.parseRange(lengthEl, this.name)) {
            params.add(range);
        }
        meta.put(LengthValidator.class, params);
        return this;
    }

    public Rule length(int length, Opt opt) {
        List<Object> param = meta.get(LengthValidator.class);
        if (param == null) {
            param = new ArrayList<Object>();
            meta.put(LengthValidator.class, param);
        }
        param.add(new Range(opt, length));
        return this;
    }

    public Rule length(int length1, Opt opt1, int length2, Opt opt2) {
        List<Object> param = meta.get(LengthValidator.class);
        if (param == null) {
            param = new ArrayList<Object>();
            meta.put(LengthValidator.class, param);
        }
        param.add(new Range(opt1, length1));
        param.add(new Range(opt2, length2));
        return this;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
    
    public Rule setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        return this;
    }
    
    
    public String getName() {
        return name;
    }
    
    public Rule setName(String name) {
        this.name = name;
        return this;
    }
}
