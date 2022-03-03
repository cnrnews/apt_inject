package com.cnw.apt;

import android.util.Log;
import android.view.View;

import com.cnw.apt.annotation.BindView;
import com.cnw.apt.annotation.Click;
import com.cnw.apt.annotation.ContentView;
import com.cnw.apt.annotation.common.OnBaseCommon;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @Author lihl
 * @Date 2022/2/28 17:21
 * @Email 1601796593@qq.com
 */
public class InjectTool {

    private static final String TAG = InjectTool.class.getSimpleName();


    public static void inject(Object object) {

        injectContentView(object);

        injectBindView(object);

        injectOnClick(object);

        injecEvent(object);
    }

    /**
     * 兼容性处理Android 点击和长按事件
     *
     * @param object
     */
    private static void injecEvent(Object object) {

        // 获取MainActivity 所有方法
        Class<?> mMainActivityClass = object.getClass();
        Method[] declaredMethods = mMainActivityClass.getDeclaredMethods();

        // 第一层 for 获取方法的所有注解
        for (Method declaredMethod : declaredMethods) {
            declaredMethod.setAccessible(true);

            Annotation[] annotations = declaredMethod.getAnnotations();

            for (Annotation annotation : annotations) {
                if (annotation == null) continue;

                // 第二层 for 查询注解中使用到 OnBaseCommon 注解的地方
                Class<? extends Annotation> annotationType = annotation.annotationType();
                OnBaseCommon onBaseCommon = annotationType.getAnnotation(OnBaseCommon.class);

                if (onBaseCommon != null) {

                    // 依次取出 OnBaseCommon 的值
                    String setListener = onBaseCommon.setListener();
                    Class setObjectListener = onBaseCommon.setObjectListener();
                    String callbackMethod = onBaseCommon.callbackMethod();


                    try {
                        // 通过注解的值，findViewById 获取具体的 View
                        Method viewMethod = annotationType.getDeclaredMethod("value");
                        viewMethod.setAccessible(true);
                        int viewId = (int) viewMethod.invoke(annotation);


                        Method findViewByIdMethod = mMainActivityClass.getMethod("findViewById", int.class);
                        Object viewObj = findViewByIdMethod.invoke(object, viewId);


                        Method mViewMethod = viewObj.getClass().getMethod(setListener, setObjectListener);
                        // 通过动态代理设置 View 对应注解的方法
                        Object proxyInstance = Proxy.newProxyInstance(
                                setObjectListener.getClassLoader(),
                                new Class[]{setObjectListener},
                                new InvocationHandler() {
                                    @Override
                                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                                        return declaredMethod.invoke(object, null);
                                    }
                                }
                        );
                        mViewMethod.invoke(viewObj, proxyInstance);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    }
    /**
     * 将使用到@Click注解的方法动态注入到 MainActivity
     * viewId 对应的事件触发时，执行 MainActivity 对应的注解方法
     *
     * @param object
     */
    private static void injectOnClick(Object object) {

        // 获取MainActivity所有方法
        Class<?> mMainActivityClass = object.getClass();
        Method[] declaredMethods = mMainActivityClass.getDeclaredMethods();

        // 获取使用到@Click注解的方法
        for (Method declaredMethod : declaredMethods) {

            declaredMethod.setAccessible(true);

            Click annotation = declaredMethod.getAnnotation(Click.class);
            if (null != annotation) {
                //析注解@Click的value,通过findViewById转换为View
                int viewId = annotation.value();

                Method findViewByIdMethod = null;
                try {
                    findViewByIdMethod = mMainActivityClass.getMethod("findViewById", int.class);


                    Object viewObj = findViewByIdMethod.invoke(object, viewId);
                    View targetView = (View) viewObj;

                    // 设置view 点击事件,执行MainActivity对应的注解方法
                    targetView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                declaredMethod.invoke(object);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * 将注解 BindView 的控件注入到 object
     *
     * @param object Activity 或者 Fragment 实例对象
     */
    private static void injectBindView(Object object) {

        // 得到类的所有字段
        Field[] declaredFields = object.getClass().getDeclaredFields();
        if (declaredFields != null) {
            for (Field field : declaredFields) {

                field.setAccessible(true);
                // 查询所有使用了注解 BindView 的字段
                BindView annotation = field.getAnnotation(BindView.class);
                if (annotation != null) {
                    try {
                        // 获取注解值，调用object 的 findViewById 方法
                        int viewId = annotation.value();
                        Method findViewByIdMethod = object.getClass().getMethod("findViewById", int.class);
                        findViewByIdMethod.setAccessible(true);

                        // 得到具体的 view
                        Object resultView = findViewByIdMethod.invoke(object, viewId);

                        // 最后将 resultView 注入到 object
                        // field :      com.cnw.apt.MainActivity.textView
                        // object:      com.cnw.apt.MainActivity
                        // resultView:  android.widget.TextView
                        // =>  MainActivity.textView  = resultView
                        field.set(object, resultView);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 将注解 ContentView 的 layoutId 注入到 object 的 setContentView()
     *
     * @param object Activity 或者 Fragment 实例对象
     */
    private static void injectContentView(Object object) {
        Class<?> mActivityClass = object.getClass();
        ContentView annotation = mActivityClass.getAnnotation(ContentView.class);
        if (null == annotation) {
            Log.e(TAG, " annotation is null");
            return;
        }

        // 获取注解的值
        int layoutId = annotation.value();

        try {
            // 反射获取Activity 或者 Fragment setContentView 方法
            Method setContentViewMethod = mActivityClass.getMethod("setContentView", int.class);
            setContentViewMethod.setAccessible(true);
            // 执行 setContentView 方法
            setContentViewMethod.invoke(object, layoutId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
