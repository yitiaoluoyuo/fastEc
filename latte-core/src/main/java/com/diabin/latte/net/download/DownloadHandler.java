package com.diabin.latte.net.download;

import android.os.AsyncTask;

import com.diabin.latte.net.CallBack.IError;
import com.diabin.latte.net.CallBack.IFailure;
import com.diabin.latte.net.CallBack.IRequest;
import com.diabin.latte.net.CallBack.ISuccess;
import com.diabin.latte.net.RestCreator;

import java.util.WeakHashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by xiaoxu on 2017/8/4.
 */

public class DownloadHandler {

    private final String URL;
    private static final WeakHashMap<String, Object> PARAMS = RestCreator.getParams();
    private final ISuccess SUCCESS;
    private final IFailure FAILURE;
    private final IError ERROR;
    private final IRequest REQUEST;

    //DOWNLOAD
    private final String DOWNLOAD_DIR;
    private final String EXTENSION;
    private final String NAME;

    public DownloadHandler(
            String URL,
            IRequest request,
            String download_dir,
            String extension,
            String name,
            ISuccess success,
            IFailure failure,
            IError error) {
        this.URL = URL;
        this.SUCCESS = success;
        this.FAILURE = failure;
        this.ERROR = error;
        this.REQUEST = request;
        this.DOWNLOAD_DIR = download_dir;
        this.EXTENSION = extension;
        this.NAME = name;
    }

    public final void handleDownload(){
        if (REQUEST != null){
            REQUEST.onRequestStart();
        }
        RestCreator.getRestService()
                .download(URL,PARAMS)
                .enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){

                    final ResponseBody responseBody = response.body();

                    final SaveFileTask saveFileTask = new SaveFileTask(REQUEST,SUCCESS,FAILURE,ERROR);
                    saveFileTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,DOWNLOAD_DIR,EXTENSION,responseBody,NAME);

                    //这里一定要进行判断，否则文件下载不全。
                    if (saveFileTask.isCancelled()){
                        if (REQUEST != null){
                            REQUEST.onRequestEnd();
                        }
                    }else {
                        if (ERROR != null){
                            ERROR.onError(response.code(),response.message());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (FAILURE != null){
                    FAILURE.onFailure();
                }

            }
        });

    }
}
