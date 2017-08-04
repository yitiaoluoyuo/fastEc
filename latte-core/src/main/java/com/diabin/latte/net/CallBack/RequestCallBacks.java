package com.diabin.latte.net.CallBack;

import android.os.Handler;

import com.diabin.latte.ui.LatteLoader;
import com.diabin.latte.ui.LoaderStyle;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2017/8/1.
 */

public class RequestCallBacks implements Callback<String> {

    private final ISuccess SUCCESS;
    private final IFailure FAILURE;
    private final IError ERROR;
    private final IRequest REQUEST;
    private final LoaderStyle LOADER_STYLE;

    //声明static类型的Handler避免内存泄露
    private static final Handler HANDLER = new Handler();

    public RequestCallBacks(ISuccess success,
                            IFailure failure,
                            IError error,
                            IRequest request,LoaderStyle style) {
        this.SUCCESS = success;
        this.FAILURE = failure;
        this.ERROR = error;
        this.REQUEST = request;
        this.LOADER_STYLE = style;
    }

    @Override
    public void onResponse(Call<String> call, Response<String> response) {
        if (response.isSuccessful()){
            if (call.isExecuted()){
                if (SUCCESS!=null){
                    SUCCESS.onSuccess(response.body());
                }

            }
        }else {
            if (ERROR!=null){
                ERROR.onError(response.code(),response.message());
            }
        }

        stopLoading();
    }

    @Override
    public void onFailure(Call<String> call, Throwable t) {
        if (FAILURE!=null){
            FAILURE.onFailure();
        }
        if (REQUEST!=null){
            REQUEST.onRequestEnd();
        }
        stopLoading();
    }

    private void stopLoading(){
        if (LOADER_STYLE !=null){
            HANDLER.postDelayed(new Runnable() {
                @Override
                public void run() {
                    LatteLoader.stopLoading();
                }
            },1000);
        }
    }
}
