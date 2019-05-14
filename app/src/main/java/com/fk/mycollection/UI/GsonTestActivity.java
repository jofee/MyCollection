package com.fk.mycollection.UI;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.fk.mycollection.R;
import com.fk.mycollection.bean.BaseBean;
import com.fk.mycollection.bean.UserBean;
import com.fk.mycollection.utill.CommonTools;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GsonTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gson_test);
        jsonConvert();
    }

    private void jsonConvert(){

        Gson gson=new Gson();

        UserBean user=new UserBean();
        user.setUserId("123");
        user.setName("jim");
        String jsonUser=gson.toJson(user);

        UserBean user2= gson.fromJson(jsonUser,UserBean.class);

        List<List<Map<String,String>>> list=new ArrayList<>();

        String jsonList=gson.toJson(list);

        List<List<Map<String,String>>> list2=gson.fromJson(jsonList,new TypeToken<List<List<Map<String,String>>>>(){}.getType());

//        BaseBean<UserBean> result=new BaseBean<>();
//        result.setCode(200);
//        result.setError("");
//        result.setData(user);
//
//        String jsonResult= gson.toJson(result);

        String jsonResult= CommonTools.dataToJson(200,"",user);

        BaseBean<UserBean> response = gson.fromJson(jsonResult, new TypeToken<BaseBean<UserBean>>(){}.getType());

    }
}
