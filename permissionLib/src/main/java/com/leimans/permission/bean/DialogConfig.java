package com.leimans.permission.bean;

import android.app.Dialog;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

/**
 * @Author ：AJin
 * @Date : 3/6/21
 * @Description :
 * Dialog定制化参数返回
 * 定制化的Dialog必须实现 {@link AopDialogImpl}
 * 否则会抛出异常
 * 如果传递的是{@link DialogParams} 则使用系统的dialog进行提示
 */
public class DialogConfig {

    /**
     * 使用自定义dialog
     */
    private Dialog dialog;

    /**
     * 使用自定义dialogFragment
     */
    private DialogFragment dialogFragment;

    public DialogConfig(@Nullable Dialog dialog) {
        this.dialog = dialog;
    }

    public DialogConfig(@Nullable DialogFragment dialogFragment) {
        this.dialogFragment = dialogFragment;
    }

    public Dialog getDialog() {
        return dialog;
    }

    public DialogFragment getDialogFragment() {
        return dialogFragment;
    }

}
