package com.leimans.permission.bean;

/**
 * @Author ：AJin
 * @Date : 3/6/21
 * @Description :
 * AopPermission的dialog规则
 * 必须在确定和取消按钮调用对应方法
 */
public interface AopDialogImpl {

    void setDialogListener(AopDialogListener listener);
}
