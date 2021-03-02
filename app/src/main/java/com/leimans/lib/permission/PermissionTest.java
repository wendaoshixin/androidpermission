package com.leimans.lib.permission;

import android.Manifest;
import android.content.Context;
import android.widget.Toast;

import com.leimans.permission.annotation.AopPermission;

/**
 * @Author ：FengLi
 * @Date : 2021-03-02
 * @Description : 非主Actvity，非Fragment测试验证OK
 */
public class PermissionTest {

    @AopPermission({Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA})
    public  static void test123(Context context) {
        Toast.makeText(context, "测试权限", Toast.LENGTH_LONG).show();
    }
}
