package com.duanbn.alamo.annotation;

import java.lang.annotation.*;

/**
 * 嵌套校验.
 * 当对象的属性是另一个被引用的对象时, 如果这个属性被此注解标注，则在校验该对象时
 * 对这个属性引用的对象进行校验.
 *
 * @author duanbn
 * @since 1.0.0
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface CheckObject {

    String cname() default "";

    /**
     * 是否允许为空.
     * 默认为true, 也就是可以为null.
     */
    boolean isNull() default true;

    String message() default "";

}
