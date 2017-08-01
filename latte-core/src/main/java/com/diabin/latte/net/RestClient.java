package com.diabin.latte.net;

import com.diabin.latte.net.CallBack.IError;
import com.diabin.latte.net.CallBack.IFailure;
import com.diabin.latte.net.CallBack.IRequest;
import com.diabin.latte.net.CallBack.ISuccess;
import com.diabin.latte.net.CallBack.RequestCallBacks;

import java.util.Map;
import java.util.WeakHashMap;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by Administrator on 2017/8/1.
 */

public class RestClient {

    private final String URL;
    // private final String DOWNLOAD_DIR;
    private static final WeakHashMap<String, Object> PARAMS = RestCreator.getParams();
    //private final String NAME;
    private final ISuccess SUCCESS;
    private final IFailure FAILURE;
    private final IError ERROR;
    private final IRequest REQUEST;
    private final RequestBody BODY;

    public RestClient(String URL,
                      Map<String, Object> params,
                      ISuccess success,
                      IFailure failure,
                      IError error,
                      IRequest request,
                      RequestBody body) {
        this.URL = URL;
        PARAMS.putAll(params);
        this.SUCCESS = success;
        this.FAILURE = failure;
        this.ERROR = error;
        this.REQUEST = request;
        this.BODY = body;
    }

    public static RestClientBuilder builder() {
        return new RestClientBuilder();
    }

    private void request(HttpMethod method) {
        final RestService service = RestCreator.getRestService();
        Call<String> call = null;
        if (REQUEST != null) {
            REQUEST.onRequestStart();
        }

        switch (method) {
            case GET:
                call = service.get(URL, PARAMS);
                break;
            case POST:
                call = service.post(URL, PARAMS);
                break;
            case DELETE:
                call = service.delete(URL, PARAMS);
                break;
            case PUT:
                call = service.put(URL, PARAMS);
                break;
            default:
                break;
        }

        if (call != null) {
            call.enqueue(getRequestCallBack());
        }
    }

    private Callback<String> getRequestCallBack() {
        return new RequestCallBacks(SUCCESS, FAILURE, ERROR, REQUEST);
    }

    public void get() {
        request(HttpMethod.GET);
    }

    public void post() {
        request(HttpMethod.POST);
    }

    public void delete() {
        request(HttpMethod.DELETE);
    }

    public void put() {
        request(HttpMethod.PUT);
    }
}
