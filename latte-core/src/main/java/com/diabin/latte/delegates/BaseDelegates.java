package com.diabin.latte.delegates;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.yokeyword.fragmentation_swipeback.SwipeBackFragment;

/**
 * Created by Administrator on 2017/8/1.
 * 定义为抽象类：限制实例化对象
 */

public abstract class BaseDelegates extends SwipeBackFragment{

    @SuppressWarnings("SpellCheckingInspection")
    private Unbinder mUnbinder = null;

    //让子类传入布局
    public abstract Object setLayout();
    //强制子类实现的方法
    public abstract void onBindView( @Nullable Bundle savedInstanceState,
                                     View rootView);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = null;
        if (setLayout() instanceof Integer){
            rootView = inflater.inflate((Integer) setLayout(),container,false);
        }else if (setLayout() instanceof  View){
            rootView = (View) setLayout();
        }
        if (rootView!=null){
            //绑定视图
            mUnbinder = ButterKnife.bind(this,rootView);
            onBindView(savedInstanceState,rootView);

        }
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mUnbinder!=null){
            //解除绑定
            mUnbinder.unbind();
        }
    }
}
