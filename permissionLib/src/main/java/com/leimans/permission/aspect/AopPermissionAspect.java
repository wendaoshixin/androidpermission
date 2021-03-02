package com.leimans.permission.aspect;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.leimans.permission.annotation.AopPermission;
import com.leimans.permission.bean.AopPermissionResult;
import com.leimans.permission.callback.PermissionRequestCallback;
import com.leimans.permission.fragment.AopPermissionFragment;
import com.leimans.permission.fragment.AopPermissionSupportFragment;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author ：FengLi
 * @Date : 2021-03-02
 * @Description : 使用Aspect编译器，处理权限注解逻辑
 */
@Aspect
public class AopPermissionAspect {
    private static final String TAG_FRAGMENT_SUPPORT = "AopPermissionSupportFragment";
    private static final String TAG_FRAGMENT = "AopPermissionFragment";

    @Pointcut("execution(@com.leimans.permission.annotation.AopPermission * *(..))")
    public void checkPermission() {

    }

    @Around("checkPermission()")
    public void check(final ProceedingJoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        final Object[] args = point.getArgs();
        final Activity activity = getTopActivity(point);
        if (activity == null) {
            return;
        }
        AopPermission annotation = signature.getMethod().getAnnotation(AopPermission.class);
        final String[] permissions = annotation.value();
        if (permissions == null || permissions.length == 0) {
            proceed(args, point, new String[]{}, new String[]{}, new String[]{});
            return;
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            proceed(args, point, new String[]{}, new String[]{}, permissions);
            return;
        }
        //权限过滤，只申请被拒绝了的
        final List<String> deniedPermissions = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                deniedPermissions.add(permission);
            }
        }
        if (deniedPermissions.size() == 0) {
            proceed(args, point, new String[]{}, new String[]{}, permissions);
            return;
        }
        if (activity instanceof FragmentActivity) {
            supportPermissions(activity, args, point, deniedPermissions, permissions);
        } else {
            permissions(activity, args, point, deniedPermissions, permissions);
        }
    }


    private void supportPermissions(final Activity activity, final Object[] args, final ProceedingJoinPoint point,
                                    List<String> deniedPermissions, final String[] allPermissions) {
        FragmentManager fm = ((FragmentActivity) activity).getSupportFragmentManager();
        AopPermissionSupportFragment aopPermissionSupportFragment = new AopPermissionSupportFragment();
        aopPermissionSupportFragment.setPermissionRequestCallback(new PermissionRequestCallback() {
            @Override
            public void onGranted() {
                proceed(args, point, new String[]{}, new String[]{}, allPermissions);
            }

            @Override
            public void onDenied(String[] grantedPermissions, String[] deniedPermissions, String[] dontAskAgainPermissions) {
                String[] gs = getGrantedPermissions(deniedPermissions, dontAskAgainPermissions, allPermissions);
                proceed(args, point, deniedPermissions, dontAskAgainPermissions, gs);
            }
        });
        fm.beginTransaction().add(aopPermissionSupportFragment, TAG_FRAGMENT_SUPPORT).commitAllowingStateLoss();
        fm.executePendingTransactions();
        aopPermissionSupportFragment.requestPermissions(deniedPermissions.toArray(new String[deniedPermissions.size()]), 65535);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void permissions(final Activity activity, final Object[] args, final ProceedingJoinPoint point,
                             List<String> deniedPermissions, final String[] allPermissions) {
        android.app.FragmentManager fm = activity.getFragmentManager();
        AopPermissionFragment aopPermissionFragment = new AopPermissionFragment();
        aopPermissionFragment.setPermissionRequestCallback(new PermissionRequestCallback() {
            @Override
            public void onGranted() {
                proceed(args, point, new String[]{}, new String[]{}, allPermissions);
            }

            @Override
            public void onDenied(String[] grantedPermissions, String[] deniedPermissions, String[] dontAskAgainPermissions) {
                String[] gs = getGrantedPermissions(deniedPermissions, dontAskAgainPermissions, allPermissions);
                proceed(args, point, deniedPermissions, dontAskAgainPermissions, gs);
            }
        });
        fm.beginTransaction().add(aopPermissionFragment, TAG_FRAGMENT).commitAllowingStateLoss();
        fm.executePendingTransactions();
        aopPermissionFragment.requestPermissions(deniedPermissions.toArray(new String[deniedPermissions.size()]), 65535);
    }

    /**
     * 在所有权限中移除被拒绝的和不在询问的就是被允许的
     */
    private String[] getGrantedPermissions(String[] deniedPermissions, String[] dontAskAgainPermissions, String[] allPermissions) {
        String[] result = Arrays.copyOf(deniedPermissions, deniedPermissions.length + dontAskAgainPermissions.length);
        System.arraycopy(dontAskAgainPermissions, 0, result, deniedPermissions.length, dontAskAgainPermissions.length);
        List<String> grantedPermissionList = new ArrayList<>();
        List<String> allDeniedPermissionList = Arrays.asList(result);
        for (String current : allPermissions) {
            if (TextUtils.isEmpty(current)) {
                continue;
            }
            if (!allDeniedPermissionList.contains(current)) {
                grantedPermissionList.add(current);
            }
        }
        return grantedPermissionList.toArray(new String[grantedPermissionList.size()]);
    }

    public void proceed(Object[] args, ProceedingJoinPoint point,
                        String[] deniedPermissions, String[] dontAskAgainPermissions, String[] grantedPermissions) {
        try {
            AopPermissionResult aopPermissionResult = null;
            if (args != null && args.length != 0) {
                for (Object arg : args) {
                    if (arg instanceof AopPermissionResult) {
                        aopPermissionResult = (AopPermissionResult) arg;
                        break;
                    }
                }
            }
            boolean allGranted = deniedPermissions.length == 0 && dontAskAgainPermissions.length == 0;//是否全部允许
            if (aopPermissionResult != null) {
                aopPermissionResult.setAllGranted(allGranted);
                aopPermissionResult.setGrantedPermissions(grantedPermissions);
                aopPermissionResult.setDeniedPermissions(deniedPermissions);
                aopPermissionResult.setDontAskAgainPermissions(dontAskAgainPermissions);
                point.proceed(args);
            } else {
                //这种情况只有在全部权限都被允许的情况下才会回调，只要有一个不被允许，强制跳转设置界面
                if (allGranted) {
                    point.proceed(args);
                } else {
                    Activity topActivity = getTopActivity(point);
                    if (topActivity != null) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.parse("package:" + topActivity.getPackageName()));
                        topActivity.startActivity(intent);
                    }
                }
            }
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    public Activity getTopActivity(final ProceedingJoinPoint point) {
        Object curObject = point.getThis();
        Activity activity = null;
        Log.e("fengli", "curObject=" + curObject.getClass().getName());
        if (curObject instanceof Activity) {
            activity = (Activity) curObject;
        } else if (curObject instanceof Fragment) {
            activity = ((Fragment) curObject).getActivity();
        } else if (curObject instanceof android.app.Fragment) {
            activity = ((android.app.Fragment) curObject).getActivity();
        }
        return activity;
    }
}
