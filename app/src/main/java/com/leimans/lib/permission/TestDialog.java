package com.leimans.lib.permission;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.leimans.permission.bean.AopDialogImpl;
import com.leimans.permission.bean.AopDialogListener;

/**
 * @Author ：AJin
 * @Date : 3/10/21
 * @Description :
 */
public class TestDialog extends Dialog implements AopDialogImpl {

    public AopDialogListener listener;

    private Context context;

    private TextView tv_no,tv_yes;

    public TestDialog(Context context) {
        super(context);
        this.context=context;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        super.onCreate(savedInstanceState);
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.dialog_layout, null);
        tv_no=(TextView) view.findViewById(R.id.tv_no);
        tv_yes=(TextView) view.findViewById(R.id.tv_yes);

        tv_no.setOnClickListener(new View.OnClickListener() {//取消的点击事件

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dismiss();//退出提示框
            }
        });
        tv_yes.setOnClickListener(new View.OnClickListener() {//确定的点击事件

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                //这里一定要调用listener.sure();否则内部无响应
                listener.sure();

                dismiss();//退出提示框
            }
        });
        setContentView(view);
    }
    @Override
    public void setDialogListener(AopDialogListener listener) {
        this.listener = listener;
    }
}
