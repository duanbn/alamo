package com.duanbn.alamo.validator;

import java.util.*;
import java.util.concurrent.*;

import com.duanbn.alamo.Rule;

/**
 * 无参校验器.
 * 此校验器会对变量值本身进行校验且没有任何参数设置.
 * 例如被校验器是否是某种类型，或是某种固定格式.
 *
 * @author duanbn
 * @since 1.0.0
 */
public abstract class NoParamValidator implements IValidator {

    /**
     * 注解校验缓存
     */
    protected static final Map<String, Rule> ruleCache = new ConcurrentHashMap<String, Rule>();

    public abstract void check(Object value, String cname, String message);

}
