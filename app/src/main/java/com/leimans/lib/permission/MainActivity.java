package com.leimans.lib.permission;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.leimans.permission.annotation.AopPermission;
import com.leimans.permission.bean.AopPermissionResult;

import java.util.Arrays;


/**
 * @Author ：FengLi
 * @Date : 2021-03-02
 * @Description : 测试主界面
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.call("1008611");
            }
        });
        findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AopPermissionResult aopPermissionResult = new AopPermissionResult();
                MainActivity.this.multiple("arg1", aopPermissionResult, "arg2");
            }
        });
        findViewById(R.id.btn3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.startActivity(new Intent(MainActivity.this, TestFragmentActivity.class));
            }
        });
        findViewById(R.id.btn4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionTest.test123(MainActivity.this);
            }
        });

    }

    @AopPermission({Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA})
    private void multiple(String arg1, AopPermissionResult aopPermissionResult, String arg2) {
        if (aopPermissionResult.isAllGranted()) {
            Log.i(TAG, "参数=" + arg1 + "---" + arg2);
        } else {
            String[] deniedPermissions = aopPermissionResult.getDeniedPermissions();
            String[] grantedPermissions = aopPermissionResult.getGrantedPermissions();
            String[] dontAskAgainPermissions = aopPermissionResult.getDontAskAgainPermissions();
            Log.i(TAG, "不再询问的权限" + Arrays.toString(dontAskAgainPermissions) + "");
            Log.i(TAG, "允许的权限" + Arrays.toString(grantedPermissions) + "");
            Log.i(TAG, "被拒绝权限" + Arrays.toString(deniedPermissions) + "");
        }
    }

    @AopPermission(Manifest.permission.CALL_PHONE)
    private void call(String phone) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phone));
        startActivity(intent);
    }
}
