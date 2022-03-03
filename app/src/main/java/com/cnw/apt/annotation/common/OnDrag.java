package com.cnw.apt.annotation.common;

import android.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author lihl
 * @Date 2022/3/1 7:33
 * @Email 1601796593@qq.com
 */
@Target(ElementType.METHOD) // 作用在类上
@Retention(RetentionPolicy.RUNTIME) // 运行
@OnBaseCommon(
        setListener = "setOnDragListener",
        setObjectListener = View.OnDragListener.class,
        callbackMethod = "onDrag"
)
public @interface OnDrag {
    int value();
}
