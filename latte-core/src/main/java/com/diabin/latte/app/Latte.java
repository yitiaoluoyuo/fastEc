package com.diabin.latte.app;

import android.content.Context;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/7/31.
 * 统一的管理机制
 * final限制继承
 */

public final class Latte {

    public static  Configurator init(Context context){
        getConfigurations().put(
                ConfigType.APPLICATION_CONTEXT.name(),
                context.getApplicationContext());
        return Configurator.getInstance();
    }

    public static HashMap<String,Object> getConfigurations(){
        return Configurator.getInstance().getLatteConfigs();
    }

    public static Context getApplication(){
        return (Context) getConfigurations().get(ConfigType.APPLICATION_CONTEXT.name());
    }
}
