package com.example.myapplication;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {
    private static final String API_KEY = "trnsl.1.1.20191008T131430Z.309e4260222a29a0.9b3074fc865e158e4cd4954e6f3fb7e608205a9f";

    private EditText et;
    private TextView tv;
    private String result;

    //////////////////////////////////////////////////////////
    //Находим виджеты
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.textView);
        et = findViewById(R.id.MyScreenTextOne);


        //Обработка ввода EditText
        et.setOnKeyListener(new View.OnKeyListener() {
                                @Override
                                public boolean onKey(View v, int keyCode, KeyEvent event) {
                                    if (event.getAction() == KeyEvent.ACTION_DOWN && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                                        result = et.getText().toString();
                                        //Get запрос c параметрами
                                        OkHttpClient client = new OkHttpClient.Builder().build();
                                        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://translate.yandex.net/api/v1.5/tr.json/translate").newBuilder();
                                        urlBuilder.addQueryParameter("key", API_KEY);
                                        urlBuilder.addQueryParameter("lang", result.matches("[A-Za-z]") ? "en-ru" : "ru-en");
                                        urlBuilder.addQueryParameter("text", result);
                                        String url = urlBuilder.build().toString();


                                        Request request = new Request.Builder()
                                                .url(url)
                                                .build();
                                        // Обработка myResponse и вывод в TextView
                                        client.newCall(request).enqueue(new Callback() {
                                            @Override
                                            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                                call.cancel();
                                            }

                                            @Override
                                            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                                final String myResponse = response.body().string();

                                                MainActivity.this.runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        try {
                                                            JSONObject json = new JSONObject(myResponse);
                                                            JSONArray jArray = json.getJSONArray("text");
                                                            tv.setText(jArray.getString(0));
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                });
                                            }
                                        });
                                        return true;
                                    }

                                    return false;
                                }

                            }
        );


    }

}
