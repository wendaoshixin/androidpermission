package com.leimans.permission.callback;

/**
 * @Author ：FengLi
 * @Date : 2021-03-02
 * @Description : 权限回调，放到不可见发Fragment里面
 */
public interface PermissionRequestCallback {
    /**
     * 所有申请的权限都被允许
     */
    void onGranted();

    /**
     * 没有全部通过 （grantedPermissions+deniedPermissions+dontAskAgainPermissions）=一共申请的权限
     *
     * @param grantedPermissions      通过了的权限
     * @param deniedPermissions       被拒绝的权限（不包括不再被询问的）
     * @param dontAskAgainPermissions 不再询问的权限
     */
    void onDenied(String[] grantedPermissions, String[] deniedPermissions, String[] dontAskAgainPermissions);
}
