package com.duanbn.alamo.annotation;

import java.lang.annotation.*;

/**
 * 用户自定义校验.
 *
 * @author duanbn
 * @since 1.0.0
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface CustomCheck {

    /**
     * 属性的中文名.
     */
    String cname() default "";

    /**
     * 自定义校验器的class fullpath
     */
    String classname();

    String message() default "";

}
