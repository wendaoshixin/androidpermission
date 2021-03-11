package com.leimans.permission.bean;

import android.app.Dialog;
import android.view.View;

import androidx.fragment.app.DialogFragment;

/**
 * @Author ：AJin
 * @Date : 3/6/21
 * @Description :
 * Dialog的参数设置，用于设置文本等
 */
public class DialogParams {
    private String tip = "App需要使用权限";
    private String sureTip = "确定";
    private String cancelTip = "取消";
    private int theme;

    public void setTip(String tip) {
        this.tip = tip;
    }

    public void setSureTip(String sureTip) {
        this.sureTip = sureTip;
    }

    public void setCancelTip(String cancelTip) {
        this.cancelTip = cancelTip;
    }

    public void setTheme(int theme) {
        this.theme = theme;
    }

    public String getTip() {
        return tip;
    }

    public String getSureTip() {
        return sureTip;
    }

    public String getCancelTip() {
        return cancelTip;
    }

    public int getTheme() {
        return theme;
    }
}
