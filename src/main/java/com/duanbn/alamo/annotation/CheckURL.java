package com.duanbn.alamo.annotation;

import java.lang.annotation.*;

/**
 * 校验属性是否是URL.
 *
 * @author duanbn
 * @since 1.0.0
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface CheckURL {

    /**
     * 中文名称.
     */
    String cname() default "";

    /**
     * 是否允许为空.
     * 默认为true, 也就是可以为null.
     */
    boolean isNull() default true;

    /**
     * URL的长度.
     * 长度表达式. (1,*), (1,2), (*,2), [1,*], [1,2], [*,2] 小括号代表开区间
     * 大括号代表闭区间, 星号代表无界限.
     */
    String length() default "";

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
