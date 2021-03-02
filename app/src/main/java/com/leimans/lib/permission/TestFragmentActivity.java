package com.leimans.lib.permission;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
/**
 * @Author ：FengLi
 * @Date : 2021-03-02
 * @Description : FragmentActivty测试
 */
public class TestFragmentActivity extends FragmentActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other);
    }
}
