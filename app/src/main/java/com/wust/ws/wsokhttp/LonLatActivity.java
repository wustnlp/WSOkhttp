package com.wust.ws.wsokhttp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.wust.ws.util.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LonLatActivity extends AppCompatActivity {

    private EditText mLon;
    private EditText mLat;
    private OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lon_lat);
        mLon = (EditText) findViewById(R.id.editText);
        mLat = (EditText) findViewById(R.id.editText2);
        client = new OkHttpClient();
    }

    public void select(View view){
        String url = Config.API.Host;
        //loadWithFrom(url);
        loadWithJson(url);
    }

    private void loadWithJson(String url){
        JSONObject json = new JSONObject();
        try {
            json.put("a","苏州市");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String jsonParams = json.toString();
        RequestBody body = RequestBody.create(MediaType.parse("application/json;charset=utf-8"),jsonParams);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mLon.setText("查询失败");
                mLat.setText("查询失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    showInfo(response.body().string());
                    response.close();
                }
            }
        });
    }

    private void loadWithFrom(String url) {
        RequestBody body = new FormBody.Builder().add("a","苏州市").build();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mLon.setText("查询失败");
                mLat.setText("查询失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    showInfo(response.body().string());
                    response.close();
                }
            }
        });
    }

    private void showInfo(final String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject json = new JSONObject(str);
                    String province = json.optString("lon");
                    String catName = json.optString("lat");
                    mLon.setText(province);
                    mLat.setText(catName);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
