package com.fk.mycollection.UI;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.fk.mycollection.R;
import com.fk.mycollection.bean.BaseBean;
import com.fk.mycollection.bean.UserBean;
import com.fk.mycollection.webservice.HttpManager;
import com.fk.mycollection.webservice.WebAPI;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetrofitTestActivity extends AppCompatActivity {

    WebAPI client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit_test);
        client = HttpManager.getInstance().createService();
        sendRequest();
    }

    private void sendRequest() {
        Map<String, String> params = new HashMap<>();
        params.put("userName", "jim");
        params.put("password", "123456");
        Call<BaseBean<UserBean>> call = client.login(params);
        call.enqueue(new Callback<BaseBean<UserBean>>() {
            @Override
            public void onResponse(Call<BaseBean<UserBean>> call, Response<BaseBean<UserBean>> response) {
                BaseBean<UserBean> result = response.body();
                if (result.getCode() == 200) {
                    UserBean user = result.getData();
                } else {
                    Toast.makeText(RetrofitTestActivity.this, result.getError(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseBean<UserBean>> call, Throwable t) {

            }
        });
    }

}
