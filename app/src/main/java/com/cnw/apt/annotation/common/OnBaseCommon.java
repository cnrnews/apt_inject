package com.cnw.apt.annotation.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author lihl
 * @Date 2022/3/1 7:35
 * @Email 1601796593@qq.com
 */
@Target(ElementType.ANNOTATION_TYPE) // 作用在注解之上
@Retention(RetentionPolicy.RUNTIME) // 运行时
public @interface OnBaseCommon {

    // 事件三要素 订阅方式 setOnClickListener,setOnLongClickListener
    String setListener();

    // 事件三要素2 事件源对象 View.OnClickListener,View.OnLongClickListener
    Class setObjectListener();

    // 事件三要素3 具体执行的方法 onClick(View v),onLongClick(View v)
    String callbackMethod();

}
