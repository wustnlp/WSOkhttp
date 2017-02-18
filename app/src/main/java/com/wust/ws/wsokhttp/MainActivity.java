package com.wust.ws.wsokhttp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //get方法
    public void getRequest(View view){

        OkHttpClient okclient = new OkHttpClient();
        String url = "https://www.baidu.com";
        Request request =new Request.Builder().url(url).build();
        //enqueue是一个异步方法，execute()是一个同步方法
        okclient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("Failed:","visited failed!");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("Success:",response.body().string());
            }
        });

    }
}
