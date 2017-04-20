package com.xuzhihui.picsart.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;

/**
 * Project Name:  PicsArt
 * Package Name:  com.xuzhihui.picsart.view
 * File Name:     CustomDialog
 * Creator:       Jav-Xu
 * Create Time:   2017/4/14 18:54
 * Description:   自定义 Dialog 模板
 */

public class CustomDialog extends Dialog {

    private Window mWindow;
    private WindowManager.LayoutParams mLayoutParams;

    // 基本属性初始化
    public CustomDialog(Context context, int theme) {
        super(context, theme);
        mWindow = getWindow();
        mLayoutParams = getWindow().getAttributes();
    }

    // 详细属性赋值
    public CustomDialog(Context context, int theme, int layout, int width, int heigth, int gravity, int anim) {
        this(context, theme);
        setContentView(layout);
        mLayoutParams.width = width;
        mLayoutParams.height = heigth;
        mLayoutParams.gravity = gravity;
        mWindow.setAttributes(mLayoutParams);
        mWindow.setWindowAnimations(anim);
    }
}
