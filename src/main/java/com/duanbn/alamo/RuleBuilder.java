package com.duanbn.alamo;

/**
 * 验证规则构造器.
 * 通过此构造其创建一个验证规则.
 *
 * @author duanbn.
 * @since 1.0.0
 */
public class RuleBuilder {

    /**
     * 创建一个验证规则.
     *
     * @return Rule 验证规则.
     */
    public static Rule build() {
        return new Rule();
    }

}
