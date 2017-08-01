package com.diabin.latte.net;

import com.diabin.latte.app.ConfigType;
import com.diabin.latte.app.Latte;

import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by Administrator on 2017/8/1.
 * 5
 */

class RestCreator {

    //全局化params不用担心PARAMS为空，不用判空。惰性加载比较严谨。
    private static final class ParamsHolder{
        static final WeakHashMap<String,Object> PARAMS= new WeakHashMap<>();
    }

    static WeakHashMap<String,Object> getParams(){
        return ParamsHolder.PARAMS;
    }

    public static  RestService getRestService(){
        return RestServiceHolder.REST_SERVICE;
    }

    private final static class RetrofitHolder{
        private static final String BASE_URL = (String) Latte.getConfigurations().get(ConfigType.API_HOST.name());
        private static final Retrofit RETROFIT_CLIENT = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(OkHttpHolder.OK_HTTP_CLIENT)
                .addConverterFactory(ScalarsConverterFactory.create())//转换器，转换为String类型
                .build();
    }

    private static final class OkHttpHolder{
        private static final int TIME_OUT = 60;
        private static final OkHttpClient OK_HTTP_CLIENT = new OkHttpClient.Builder()
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .build();
    }

    private static final class RestServiceHolder{
        //由RETROFIT_CLIENT 创建 REST_SERVICE
        private static final RestService REST_SERVICE =
                RetrofitHolder.RETROFIT_CLIENT.create(RestService.class);
    }
}
