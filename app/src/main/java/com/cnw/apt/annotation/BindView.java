package com.cnw.apt.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author lihl
 * @Date 2022/2/28 17:33
 * @Email 1601796593@qq.com
 */
@Target(ElementType.FIELD) // 作用在类上
@Retention(RetentionPolicy.RUNTIME) // 运行时
public @interface BindView {
    int value() default -1;
}
