package com.duanbn.alamo.annotation;

import java.lang.annotation.*;

/**
 * 校验属性是否是身份证号码.
 *
 * @author duanbn
 * @since 1.0.0
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface CheckIdCard {

    /**
     * 属性的中文名称.
     */
    String cname() default "";

    /**
     * 是否允许为空.
     * 默认为true, 也就是可以为null.
     */
    boolean isNull() default true;

    /**
     * 枚举值.
     * 使用逗号分割.
     */
    String value() default "";

    /**
     * 错误提示信息.
     */
    String message() default "";

}
