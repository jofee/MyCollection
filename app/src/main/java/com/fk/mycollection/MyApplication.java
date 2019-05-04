package com.fk.mycollection;

import android.app.Application;

import io.realm.Realm;

/**
 * Created by Administrator on 2017/12/14.
 */

public class MyApplication extends Application {

    public static MyApplication sApp;

    @Override
    public void onCreate() {
        super.onCreate();
        sApp=this;
        Realm.init(this);
    }
}
