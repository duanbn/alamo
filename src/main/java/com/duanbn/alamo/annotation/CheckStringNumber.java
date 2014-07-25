package com.duanbn.alamo.annotation;

import java.lang.annotation.*;

/**
 * 校验属性是否是一个字符串数字.
 *
 * @author duanbn
 * @since 1.0.0
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface CheckStringNumber {

    /**
     * 属性的中文标识.
     */
    String cname() default "";

    /**
     * 是否允许为空.
     * 默认为true, 也就是可以为null.
     */
    boolean isNull() default true;

    /**
     * 该数字的范围.
     * 长度表达式. (1,*), (1,2), (*,2), [1,*], [1,2], [*,2] 小括号代表开区间
     * 中括号代表闭区间, 星号代表无界限.
     */
    String range() default "";

    /**
     * 数字的位数.
     * 如果是浮点型的数字，则表示整数和小数位.
     * 长度表达式. (1,*), (1,2), (*,2), [1,*], [1,2], [*,2] 小括号代表开区间
     * 中括号代表闭区间, 星号代表无界限.
     */
    String length() default "";

    /**
     * 枚举值.
     * 只能是数字类型, 使用逗号分割.
     */
    String value() default "";

    /**
     * 错误提示信息.
     */
    String message() default "";

}
