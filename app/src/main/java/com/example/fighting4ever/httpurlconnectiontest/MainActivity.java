package com.example.fighting4ever.httpurlconnectiontest;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.get_data)
    Button mGetData;
    @Bind(R.id.text_view)
    TextView mTextView;
    @Bind(R.id.listview)
    ListView mListView;
    private static final int GET_DATA = 101;
    private static final int READ_TIMEOUT = 30000;
    private static final int CONNECT_TIMEOUT = 30000;
    private static final String APPKEY = "ec8c99b699ebe53a563bc4b2a8fa6a71";
    private Handler getData = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == GET_DATA){
                mTextView.setText(String.valueOf(msg.obj));
                Gson gson = new Gson();
                DataModel dm = gson.fromJson((String) msg.obj, DataModel.class);
                mData.add("resultcode: " + dm.getResultcode());
                mData.add("reason: " + dm.getReason());
                mData.add("result:" + dm.getResult());
                mData.add("error_code:" + dm.getError_code());
                adapter.notifyDataSetChanged();
            }
        }
    };
    private List<String> mData;
    private ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mGetData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });
        mData = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, mData);
        mListView.setAdapter(adapter);
    }
    private void getData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                Map params = new HashMap();
                params.put("cityname", "上海");
                params.put("key", APPKEY);
                params.put("dtype","json");
                try {
//                    String strUrl = "http://op.juhe.cn/onebox/weather/query?" + urlencode(params);
                    String strUrl = "http://op.juhe.cn/onebox/weather/query";
                    URL url = new URL(strUrl);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setReadTimeout(READ_TIMEOUT);
                    connection.setConnectTimeout(CONNECT_TIMEOUT);
                    connection.setInstanceFollowRedirects(false);
                    connection.connect();

                    InputStream in = connection.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(in));
                    StringBuilder builder = new StringBuilder();
                    String line = "";
                    while ((line = br.readLine()) != null){
                        builder.append(line);
                    }

                    Message msg = new Message();
                    msg.what = GET_DATA;
                    msg.obj = builder.toString();
                    getData.sendMessage(msg);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    if (connection != null){
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    public static String urlencode(Map<String, String> data){
        StringBuilder builder = new StringBuilder();
        for (Map.Entry i : data.entrySet()){
            try {
                builder.append(i.getKey()).append("=").append(URLEncoder.encode(i.getValue() +"", "UTF-8")).append("&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return builder.toString();
    }
}
