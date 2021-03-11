package com.leimans.permission.dialog;

import android.app.AlertDialog;
import android.content.Context;

/**
 * @Author ：AJin
 * @Date : 3/11/21
 * @Description :
 */
class DefaultDialog extends AlertDialog {
    protected DefaultDialog(Context context) {
        super(context);
    }

    protected DefaultDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    protected DefaultDialog(Context context, int themeResId) {
        super(context, themeResId);
    }


}
