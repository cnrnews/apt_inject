package com.cnw.apt.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author lihl
 * @Date 2022/3/1 6:52
 * @Email 1601796593@qq.com
 */

@Target(ElementType.METHOD) // 作用在方法上
@Retention(RetentionPolicy.RUNTIME) // 运行时
public @interface Click {
    int value() default -1;
}
