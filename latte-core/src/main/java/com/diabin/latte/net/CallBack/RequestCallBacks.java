package com.diabin.latte.net.CallBack;

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

    public RequestCallBacks(ISuccess success,
                            IFailure failure,
                            IError error,
                            IRequest request) {
        this.SUCCESS = success;
        this.FAILURE = failure;
        this.ERROR = error;
        this.REQUEST = request;
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
    }

    @Override
    public void onFailure(Call<String> call, Throwable t) {
        if (FAILURE!=null){
            FAILURE.onFailure();
        }
        if (REQUEST!=null){
            REQUEST.onRequestEnd();
        }
    }
}
