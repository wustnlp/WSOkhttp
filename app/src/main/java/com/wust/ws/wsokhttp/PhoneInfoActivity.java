package com.wust.ws.wsokhttp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PhoneInfoActivity extends AppCompatActivity {

    private EditText mLon;
    private EditText mLat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_info);
        mLon = (EditText) findViewById(R.id.editText);
        mLat = (EditText) findViewById(R.id.editText2);
    }

    public void select(View view){
        String url = "http://gc.ditu.aliyun.com/geocoding?a=%E8%8B%8F%E5%B7%9E%E5%B8%82";
        OkHttpClient client = new OkHttpClient();
        client.newCall(new Request.Builder().get().url(url).build()).enqueue(new Callback() {
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

    private void showInfo(final String string) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject json = new JSONObject(string);
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
