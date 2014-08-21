package com.duanbn.alamo;

import com.duanbn.alamo.exception.CheckFailureException;
import com.duanbn.alamo.exception.custom.CustomErrorException;

/**
 * 校验失败处理器.
 * 当使用注解进行校验时，如果某一个属性校验失败则会调用此接口的实现.
 *
 * @author duanbn
 * @since 1.0.1
 */
public interface ICheckFailHandler {

    /**
     * 校验失败时调用.
     *
     * @param obj 被校验的对象.
     * @param filedName 校验失败的属性名.
     * @param value 校验失败的属性的值.
     * @param e 校验失败时抛出的异常
     *
     * @throws CustomErrorException 自定义异常
     */
    public void handle(Object obj, String fieldName, Object value, CheckFailureException e) throws CheckFailureException;

}
