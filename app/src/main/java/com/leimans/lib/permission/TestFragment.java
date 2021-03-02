package com.leimans.lib.permission;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.leimans.permission.annotation.AopPermission;
import com.leimans.permission.bean.AopPermissionResult;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


/**
 * @Author ：FengLi
 * @Date : 2021-03-02
 * @Description : 测试主界面
 */
public class TestFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_main, null);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestFragment.this.saveImage(new AopPermissionResult());
            }
        });
    }

    @AopPermission({Manifest.permission.WRITE_EXTERNAL_STORAGE
            , Manifest.permission.READ_EXTERNAL_STORAGE})
    private void saveImage(AopPermissionResult aopPermissionResult) {
        if (aopPermissionResult.isAllGranted()) {
            Drawable drawable = getResources().getDrawable(R.mipmap.ic_launcher);
            Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            File file = new File(Environment.getExternalStorageDirectory() + "/fengli.png");
            OutputStream fos = null;
            boolean succ = false;
            try {
                fos = new FileOutputStream(file);
                succ = bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                Toast.makeText(getActivity(), succ ? "保存成功" + file.getAbsolutePath() : "保存失败", Toast.LENGTH_LONG).show();
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
