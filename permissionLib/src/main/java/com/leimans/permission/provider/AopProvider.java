package com.leimans.permission.provider;

import android.app.Application;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.leimans.permission.util.AopPermissionUtil;

/**
 * @Author ：FengLi
 * @Date : 2021-03-02
 * @Description : 使用ContentProvider来初始化
 */
public class AopProvider extends ContentProvider {
    @Override
    public boolean onCreate() {
        AopPermissionUtil.getInstance().init((Application) getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }


    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
