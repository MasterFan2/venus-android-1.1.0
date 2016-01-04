package com.chinajsbn.venus.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.chinajsbn.venus.ui.base.ActivityFeature;
import com.chinajsbn.venus.ui.base.FragmentFeature;
import com.chinajsbn.venus.ui.base.SystemBarTintManager;
import com.lidroid.xutils.util.DoubleKeyValueMap;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.ViewFinder;
import com.lidroid.xutils.view.ViewInjectInfo;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by MasterFan on 2015/4/24.
 * description:
 */
public class MTViewUtils {

    private static final int INVALID = -1;

    private static ConcurrentHashMap<Class<? extends Annotation>, ViewCustomEventListener> annotationType_viewCustomEventListener_map;

    /**
     * register Custom Annotation
     *
     * @param annotationType The type of custom annotation must be annotated by @EventBase.
     * @param listener
     */
    public static void registerCustomAnnotation(Class<? extends Annotation> annotationType, ViewCustomEventListener listener) {
        if (annotationType_viewCustomEventListener_map == null) {
            annotationType_viewCustomEventListener_map = new ConcurrentHashMap<>();
        }
        annotationType_viewCustomEventListener_map.put(annotationType, listener);
    }

    /**
     *
     * @param handle
     * @param container
     * @param view
     */
    public static void injectFeature(Fragment handle, LayoutInflater inflater, ViewGroup container, View view){
        FragmentFeature config = handle.getClass().getAnnotation(FragmentFeature.class);
        if(config == null) throw new IllegalArgumentException("not set resources");
        if(config.layout() <= 0) throw new RuntimeException("resources error!");
        View v = inflater.inflate(config.layout(), container, false);
    }

    /**
     *
     * @param handler
     */
    public static void injectFeature(Activity handler){

        ActivityFeature config = handler.getClass().getAnnotation(ActivityFeature.class);
        if(config == null)
            throw new IllegalArgumentException("not set resources");

        //
        // no title
        if(config.no_title()){
            handler.requestWindowFeature(Window.FEATURE_NO_TITLE);
        }

        int statusBarColor = config.statusBarColor();
        if(statusBarColor != INVALID){

            //set statusBar color
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                setTranslucentStatus(true, handler);
            }

            SystemBarTintManager tintManager = new SystemBarTintManager(handler);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(statusBarColor);
        }

        //full screen
        if(config.full_screen()){
            handler.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            handler.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        }


        //
        handler.setContentView(config.layout());
    }

    @TargetApi(19)
    private static void setTranslucentStatus(boolean on, Activity activity) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    public static void inject(View view){
        injectObject(view, new ViewFinder(view));
    }

    public static void inject(Activity activity){
        injectObject(activity, new ViewFinder(activity));
    }

    public static void inject(Object handler, View view){
        injectObject(handler, new ViewFinder(view));
    }

    public static void inject(Object handler, Activity activity){
        injectObject(handler, new ViewFinder(activity));
    }

    /**
     * @TODO inject view and events
     * @param handler
     * @param finder
     */
    public static void injectObject(Object handler, ViewFinder finder){
        //1.inject view
        Field[] fields = handler.getClass().getDeclaredFields();
        if (fields != null && fields.length > 0) {
            for (Field field : fields) {
                ViewInject viewInject = field.getAnnotation(ViewInject.class);
                if (viewInject != null) {
                    try {
                        View view = finder.findViewById(viewInject.value());
                        if (view != null) {
                            field.setAccessible(true);
                            field.set(handler, view);
                        }
                    } catch (IllegalAccessException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IllegalArgumentException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }
        }
        //2.inject event
        Method[] methods = handler.getClass().getDeclaredMethods();
        if (methods != null && methods.length > 0) {
            String eventName = OnClick.class.getCanonicalName();
            String prefix = eventName.substring(0, eventName.lastIndexOf('.'));
            DoubleKeyValueMap<ViewInjectInfo, Annotation, Method> info_annotation_method_map = new DoubleKeyValueMap<>();
            for (Method method : methods) {
                Annotation[] annotations = method.getDeclaredAnnotations();
                if (annotations != null && annotations.length > 0) {
                    for (Annotation annotation : annotations) {
                        Class<?> annType = annotation.annotationType();
                        if (annType.getCanonicalName().startsWith(prefix)) {
                            try {
                                Method valueMethod = annType.getDeclaredMethod("value");

                                Object values = valueMethod.invoke(annotation);
                                int len = Array.getLength(values);
                                for (int i = 0; i < len; i++) {
                                    ViewInjectInfo info = new ViewInjectInfo();
                                    info.value = Array.get(values, i);
                                    info_annotation_method_map.put(info, annotation, method);
                                }
                            } catch (Throwable e) {
                                LogUtils.e(e.getMessage(), e);
                            }
                        }else{
                            ViewCustomEventListener listener = annotationType_viewCustomEventListener_map.get(annType);
                            if (listener != null) {
                                listener.setEventListener(handler, finder, annotation, method);
                            }
                        }
                    }
                }
            }
            ViewCommonEventListener.setAllEventListeners(handler, finder, info_annotation_method_map);
        }
    }
}
