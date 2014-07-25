package com.duanbn.alamo.annotation;

import java.lang.annotation.*;

/**
 * 被校验的类标注.
 * 标注一个类是否需要被校验. 以及校验失败后抛出错误的方式.
 *
 * @author duanbn
 * @since 1.0.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface CheckPOJO {

    public static final String STEP = "STEP";
    public static final String ALL = "ALL";

    /**
     * 被标注的类是否需要被校验.
     */
    boolean value() default true;

    /**
     * 抛出校验错误的方式.
     * 遇到错误就抛出或者整个类都检查完之后再抛出.
     * 可选项: STEP， ALL
     */
    String mode() default STEP;

}
