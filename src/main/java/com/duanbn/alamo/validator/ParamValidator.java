package com.duanbn.alamo.validator;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.duanbn.alamo.Rule;

/**
 * 带参校验器.
 * 校验器对变量值和指定的值进行比较.
 * 例如枚举校验，范围校验，变量长度校验等.
 *
 * @author duanbn
 * @since 1.0.0
 */
public abstract class ParamValidator<T> implements IValidator {

    /**
     * 注解校验缓存
     */
    protected static final Map<String, Rule> ruleCache = new ConcurrentHashMap<String, Rule>();

    public abstract void check(Object value, List<T> params, String cname, String message);

}
