package com.fk.mycollection.webservice;

import com.fk.mycollection.bean.BaseBean;
import com.fk.mycollection.bean.UserBean;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface WebAPI {
    // 登录接口
    @FormUrlEncoded
    @POST("user!login.action")
    Call<BaseBean<UserBean, Object>> login(@FieldMap Map<String, String> params);

}

