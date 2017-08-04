package com.diabin.latte.utils;

import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.diabin.latte.app.Latte;

/**
 * Created by xiaoxu on 2017/8/2.
 *
 */

public class DimenUtil {

    public static int  getScreenWidth(){
        final Resources resources = Latte.getApplication().getResources();
        final DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.widthPixels;
    }

    public static int  getScreenHeight(){
        final Resources resources = Latte.getApplication().getResources();
        final DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.heightPixels;
    }
}
