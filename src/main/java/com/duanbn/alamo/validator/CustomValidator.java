package com.duanbn.alamo.validator;

import com.duanbn.alamo.exception.CheckErrorException;

/**
 * 用户自定义校验器接口.
 * 当内部校验器不符合校验需求, 用户可以定义自己的校验器.
 *
 * @author duanbn
 * @since 1.0.0
 */
public abstract class CustomValidator<E> implements IValidator {

    /**
     * 用户可以自己实现此方法完成校验.
     *
     * @param value 被校验的变量值.
     * @param cname 被校验的变量中文名称.
     * @param message 自定义错误提示信息，如果不存在则为null
     *
     * @throws CheckErrorExceptionBak 校验失败.
     */
    public abstract void check(E value, String cname, String message) throws CheckErrorException;

}
