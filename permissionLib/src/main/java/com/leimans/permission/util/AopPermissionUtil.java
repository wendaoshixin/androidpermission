package com.leimans.permission.util;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author ：FengLi
 * @Date : 2021-03-02
 * @Description :工具类
 * 初始化之后，可以在任何类上的函数申请权限，使用ActivityLifecycle来拿到最上面的Activity
 */
public class AopPermissionUtil {
    private static AopPermissionUtil instance;
    private List<Activity> activities = new ArrayList<>();

    public static AopPermissionUtil getInstance() {
        if (instance == null) {
            synchronized (AopPermissionUtil.class) {
                if (instance == null) {
                    instance = new AopPermissionUtil();
                }
            }
        }
        return instance;
    }

    public Activity getTopActivity() {
        if (activities == null) {
            throw new RuntimeException("Activity是空的");
        } else {
            for (int i = activities.size() - 1; i >= 0; i++) {
                Activity activity = activities.get(i);
                if (!activity.isDestroyed()) {
                    return activity;
                }
            }
            throw new RuntimeException("Activity是空的");
        }
    }

    public void init(Application context) {
        context.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                if (!activities.contains(activity)) {
                    activities.add(activity);
                }
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                activities.remove(activity);
            }
        });
    }
}
