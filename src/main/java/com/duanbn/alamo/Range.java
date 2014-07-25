package com.duanbn.alamo;

import com.duanbn.alamo.exception.DefineRuleException;

/**
 * 范围.
 *
 * @author duanbn
 * @since 1.0.0
 */
public class Range {

    private Opt opt;
    private Number value;

    public Range(Opt opt, Number value) {
        this.opt = opt;
        this.value = value;
    }

    public boolean in(Number n) {
        switch (opt) {
            case EQ:
                if (ValidateUtil.compare(n, value) == 0) {
                    return true;
                } else {
                    return false;
                }
            case GT:
                if (ValidateUtil.compare(n, value) > 0) {
                    return true;
                } else {
                    return false;
                }
            case GTE:
                if (ValidateUtil.compare(n, value) >= 0) {
                    return true;
                } else {
                    return false;
                }
            case LT:
                if (ValidateUtil.compare(n, value) < 0) {
                    return true;
                } else {
                    return false;
                }
            case LTE:
                if (ValidateUtil.compare(n, value) <= 0) {
                    return true;
                } else {
                    return false;
                }
        }

        throw new DefineRuleException("非法的opt类型");
    }

    public String toString() {
        StringBuilder info = new StringBuilder("?");
        switch (opt) {
            case EQ:
                info.append("=");
                break;
            case GT:
                info.append(">");
                break;
            case GTE:
                info.append(">=");
                break;
            case LT:
                info.append("<");
                break;
            case LTE:
                info.append("<=");
                break;
        }
        info.append(value);
        return info.toString();
    }

    public Opt getOpt() {
        return opt;
    }
    
    public void setOpt(Opt opt) {
        this.opt = opt;
    }
    
    public Number getValue() {
        return value;
    }
    
    public void setValue(Number value) {
        this.value = value;
    }
}
