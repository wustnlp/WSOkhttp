package com.wust.ws.wsokhttp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FileDownloadActivity extends AppCompatActivity {

    public static final String filePath = "http://dynamic-image.yesky.com/740x-/uploadImages/2016/348/35/63X007801E84.jpg";
    public static final String fileName = "63X007801E84.jpg";

    private ProgressBar mProgressBar;
    private OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_download);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        client = new OkHttpClient();
        requestPermission();
    }

    public void download(View view){
        Request request = new Request.Builder()
                .url(filePath)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("FileDownloadActivity","访问服务器出错！");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                writeFile(response);
                response.close();
            }
        });
    }


    public static final int EXTERNAL_STORAGE_CODE = 10;

    public void requestPermission(){
        //判断当前的activity是否已经获取了该权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            //如果申请的权限曾经被用户拒绝过，这些需要对用户做出解释
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                Toast.makeText(this,"请给定权限",Toast.LENGTH_SHORT).show();
            }else{
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},EXTERNAL_STORAGE_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case EXTERNAL_STORAGE_CODE:
            {
                //如果请求被拒绝，那么通常grantResults数组为空
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //申请成功，进行相应的操作
                    Toast.makeText(FileDownloadActivity.this,"已授权成功",Toast.LENGTH_SHORT).show();
                }else{
                    //申请失败，可以继续向用户解释
                }
            }
        }
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1){
                int progress = msg.arg1;
                mProgressBar.setProgress(progress);
            }
        }
    };

    private void writeFile(Response response) {
        InputStream is;
        FileOutputStream fos = null;
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        Log.d("writeFile:",path);
        File file = new File(path,fileName);
        is = response.body().byteStream();
        long count = response.body().contentLength();
        long sum = 0;
        try {
            fos = new FileOutputStream(file);
            int len;
            byte[] bytes = new byte[1024];
            while ((len = is.read(bytes))!= -1) {
                fos.write(bytes);
                sum += len;
                int progress = (int) (sum *1.0f / count * 100);
                Message msg = mHandler.obtainMessage(1);
                msg.arg1 = progress;
                mHandler.sendMessage(msg);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if (is !=null){
                    is.close();
                }
                if (fos != null){
                    fos.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }

    }
}
