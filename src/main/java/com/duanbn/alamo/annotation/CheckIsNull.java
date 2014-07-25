package com.duanbn.alamo.annotation;

import java.lang.annotation.*;

/**
 * 对属性进行空值校验.<br/>
 *
 * @author duanbn
 * @since 1.0.0
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface CheckIsNull {

    String cname() default "";

    /**
     * 是否允许为Null.
     */
    boolean isNull() default false;

}
