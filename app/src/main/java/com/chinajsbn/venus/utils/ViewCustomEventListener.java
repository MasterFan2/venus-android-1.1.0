package com.chinajsbn.venus.utils;

import com.lidroid.xutils.view.ViewFinder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 *
 */
public interface ViewCustomEventListener {
    void setEventListener(Object handler, ViewFinder finder, Annotation annotation, Method method);
}
