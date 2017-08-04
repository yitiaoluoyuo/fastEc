package com.diabin.latte.net;

import android.content.Context;

import com.diabin.latte.net.CallBack.IError;
import com.diabin.latte.net.CallBack.IFailure;
import com.diabin.latte.net.CallBack.IRequest;
import com.diabin.latte.net.CallBack.ISuccess;
import com.diabin.latte.net.CallBack.RequestCallBacks;
import com.diabin.latte.net.download.DownloadHandler;
import com.diabin.latte.ui.LatteLoader;
import com.diabin.latte.ui.LoaderStyle;

import java.io.File;
import java.util.Map;
import java.util.WeakHashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by Administrator on 2017/8/1.
 */

public class RestClient {

    private final String URL;
    private static final WeakHashMap<String, Object> PARAMS = RestCreator.getParams();
    private final ISuccess SUCCESS;
    private final IFailure FAILURE;
    private final IError ERROR;
    private final IRequest REQUEST;
    private final RequestBody BODY;
    private final File FILE;

    private final LoaderStyle LOADER_STYLE;
    private final Context CONTEXT;

    //DOWNLOAD
    private final String DOWNLOAD_DIR;
    private final String EXTENSION;
    private final String NAME;

    public RestClient(String URL,
                      Map<String, Object> params,
                      ISuccess success,
                      IFailure failure,
                      IError error,
                      IRequest request,
                      RequestBody body,
                      File file,
                      String download_dir,
                      String extension,
                      String name,
                      Context context,
                      LoaderStyle loader_style
                     ) {
        this.URL = URL;
        PARAMS.putAll(params);
        this.SUCCESS = success;
        this.FAILURE = failure;
        this.ERROR = error;
        this.REQUEST = request;
        this.BODY = body;
        this.CONTEXT = context;
        this.LOADER_STYLE = loader_style;
        this.FILE = file;
        this.DOWNLOAD_DIR = download_dir;
        this.EXTENSION = extension;
        this.NAME = name;
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

        if (LOADER_STYLE != null) {
            LatteLoader.showLoading(CONTEXT, LOADER_STYLE);
        }

        switch (method) {
            case GET:
                call = service.get(URL, PARAMS);
                break;
            case POST:
                call = service.post(URL, PARAMS);
                break;
            case POST_RAW:
                call = service.postRaw(URL,BODY);
                break;
            case DELETE:
                call = service.delete(URL, PARAMS);
                break;
            case PUT:
                call = service.put(URL, PARAMS);
                break;
            case PUT_RAW:
                call = service.putRaw(URL, BODY);
                break;
            case UPLOAD:
                final RequestBody requestBody =
                        RequestBody.create(MediaType.parse(MultipartBody.FORM.toString()),FILE);
                final MultipartBody.Part part =
                        MultipartBody.Part.createFormData("file",FILE.getName(),requestBody);
                call = RestCreator.getRestService().upload(URL,part);
                break;
            default:
                break;
        }

        if (call != null) {
            call.enqueue(getRequestCallBack());
        }
    }

    private Callback<String> getRequestCallBack() {
        return new RequestCallBacks(
                SUCCESS,
                FAILURE,
                ERROR,
                REQUEST,
                LOADER_STYLE);
    }

    public void get() {
        request(HttpMethod.GET);
    }

    public void post() {
        if (BODY == null) {
            request(HttpMethod.POST);
        } else {
            if (!PARAMS.isEmpty()) {
                throw new RuntimeException("params must be null");
            }
            request(HttpMethod.POST_RAW);
        }
        request(HttpMethod.POST);
    }

    public void delete() {
        request(HttpMethod.DELETE);
    }

    public void put() {
        if (BODY == null) {
            request(HttpMethod.POST);
        } else {
            if (!PARAMS.isEmpty()) {
                throw new RuntimeException("params must be null");
            }
            request(HttpMethod.POST_RAW);
        }
        request(HttpMethod.PUT);
    }

    public void upload(){
        request(HttpMethod.UPLOAD);
    }

    public void download(){
       new DownloadHandler(URL,REQUEST,DOWNLOAD_DIR,EXTENSION,NAME,SUCCESS,FAILURE,ERROR)
       .handleDownload();
    }


}

