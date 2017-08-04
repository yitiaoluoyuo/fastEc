package com.diabin.latte.ui;

import android.content.Context;
import android.support.v7.app.AppCompatDialog;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.diabin.latte.R;
import com.diabin.latte.utils.DimenUtil;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

/**
 * Created by xiaoxu on 2017/8/2.
 */

public class LatteLoader {

    //设置与屏幕相比的比例，以适合多尺寸屏幕的机型
    private static final int LOADER_SIZE_SCALE = 8;
    private static final int LOADER_OFFSET_SCALE = 10;
    //把每个loader加入集合，在不需要loader的时候，遍历集合然后统一关闭。
    private static final ArrayList<AppCompatDialog> LOADERS = new ArrayList<>();
    //设置默认的加载动画样式
    private static final String DEFAULT_LOADER = LoaderStyle.BallClipRotatePulseIndicator.name();

    public static void showLoading(Context context,Enum<LoaderStyle> type){
        showLoading(context,type.name());
    }

    public static void showLoading(Context context, String type) {
        final AppCompatDialog dialog = new AppCompatDialog(context, R.style.dialog);
        final AVLoadingIndicatorView avLoadingIndicatorView = LoaderCreator.create(type, context);
        dialog.setContentView(avLoadingIndicatorView);

        int deviceWidth = DimenUtil.getScreenWidth();
        int deviceHeight = DimenUtil.getScreenHeight();

        final Window dialogWindow = dialog.getWindow();
        if (dialogWindow == null) {
            WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();
            //设置dialog的宽高为屏幕宽高的比例缩小8倍
            layoutParams.width = deviceWidth / LOADER_SIZE_SCALE;
            layoutParams.height = deviceHeight / LOADER_SIZE_SCALE;
            //设置偏移量
            layoutParams.height = layoutParams.height + deviceHeight / LOADER_OFFSET_SCALE;
            layoutParams.gravity = Gravity.CENTER;
        }
        //
        LOADERS.add(dialog);
        dialog.show();
    }

    //dialog这个类如果传入的是ApplicationContext在向WebView或者其他View上边显示会报错。
    //在哪个类就传入当前类的Context（Fragment需要getContext）
    public static void showLoading(Context context) {
        showLoading(context, DEFAULT_LOADER);
    }

    public static void stopLoading() {
        for (AppCompatDialog dialog : LOADERS) {
            if (dialog != null) {
                if (dialog.isShowing()) {
                    //有回调
                    dialog.cancel();
                    //无回调
                    //dialog.dismiss();
                }
            }
        }
    }

}
