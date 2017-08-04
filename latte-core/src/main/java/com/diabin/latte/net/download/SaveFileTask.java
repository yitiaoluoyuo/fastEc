package com.diabin.latte.net.download;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;

import com.diabin.latte.app.Latte;
import com.diabin.latte.net.CallBack.IError;
import com.diabin.latte.net.CallBack.IFailure;
import com.diabin.latte.net.CallBack.IRequest;
import com.diabin.latte.net.CallBack.ISuccess;
import com.diabin.latte.utils.FileUtil;

import java.io.File;
import java.io.InputStream;

import okhttp3.ResponseBody;

/**
 * Created by xiaoxu on 2017/8/4.
 */

public class SaveFileTask extends AsyncTask<Object,Void, File> {

    private final IRequest REQUEST;
    private final ISuccess SUCCESS;
    private final IFailure FAILURE;
    private final IError ERROR;

    public SaveFileTask(IRequest request, ISuccess success, IFailure failure, IError error) {
        this.REQUEST = request;
        this.SUCCESS = success;
        this.FAILURE = failure;
        this.ERROR = error;
    }

    @Override
    protected File doInBackground(Object... params) {
        String downloadDir = (String) params[0];
        String extension = (String) params[1];
        final ResponseBody responseBody = (ResponseBody) params[2];
        final String name = (String) params[3];
        final InputStream is = responseBody.byteStream();
        if (downloadDir==null || downloadDir.equals("")){
            downloadDir = "down_loads";
        }
        if (extension ==null || extension.equals("")){
            extension = "";
        }
        if (name == null){
            return FileUtil.writeToDisk(is,downloadDir,extension.toUpperCase(),extension);
        }else {
            return FileUtil.writeToDisk(is,downloadDir,name);
        }
    }

    @Override
    protected void onPostExecute(File file) {
        super.onPostExecute(file);
        if (SUCCESS != null){
            SUCCESS.onSuccess(file.getPath());
        }
        if (REQUEST != null){
            REQUEST.onRequestEnd();
        }
    }

    //自动安装下载下来的.apk文件
    private void autoInstallApk(File file){
        if (FileUtil.getExtension(file.getPath()).equals("apk")){
            final Intent install = new Intent();
            install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            install.setAction(Intent.ACTION_VIEW);
            install.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
            Latte.getApplication().startActivity(install);
        }

    }
}
