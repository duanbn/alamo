package com.duanbn.alamo.validator;

import java.lang.annotation.Annotation;

/**
 * 对注解进行校验接口.
 * 处理注解类型的校验.
 *
 * @author duanbn
 * @since 1.0.0
 */
public interface IAnnotationValidator<T extends Annotation> {

    void check(Object value, String cacheKey, T annotation);

}
