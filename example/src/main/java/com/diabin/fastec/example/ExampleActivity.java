package com.diabin.fastec.example;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.diabin.latte.activities.ProxyActivity;
import com.diabin.latte.delegates.LatteDelegate;
import com.diabin.latte.net.CallBack.IError;
import com.diabin.latte.net.CallBack.IFailure;
import com.diabin.latte.net.CallBack.ISuccess;
import com.diabin.latte.net.RestClient;

public class ExampleActivity extends ProxyActivity {


    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delegate_example);
        testRestClient();
    }

    public void testRestClient() {
        RestClient.builder()
                .url("http://news.baidu.com/")
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        Toast.makeText(getApplication(),response,Toast.LENGTH_LONG).show();
                    }
                })
                .failure(new IFailure() {
                    @Override
                    public void onFailure() {

                    }
                })
                .error(new IError() {
                    @Override
                    public void onError(int code, String msg) {

                    }
                })
                .builder().get();

    }

    @Override
    public LatteDelegate setRootDelegate() {
        return new ExampleDelegate();
    }
}
