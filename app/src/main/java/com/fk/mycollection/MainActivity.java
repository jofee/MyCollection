package com.fk.mycollection;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.fk.mycollection.UI.BannerActivity;
import com.fk.mycollection.UI.BitmapGestureActivity;
import com.fk.mycollection.UI.DrawerActivity;
import com.fk.mycollection.UI.GlideProgressActivity;
import com.fk.mycollection.UI.GsonTestActivity;
import com.fk.mycollection.UI.MutiPhotoSelectActivity;
import com.fk.mycollection.UI.ObjectboxTestActivity;
import com.fk.mycollection.UI.PopupWindowActivity;
import com.fk.mycollection.UI.ProgressWebViewActivity;
import com.fk.mycollection.UI.RatingBarActivity;
import com.fk.mycollection.UI.RetrofitTestActivity;
import com.fk.mycollection.UI.ScrollListViewActivity;
import com.fk.mycollection.UI.SimpleDateSelectActivity;
import com.fk.mycollection.UI.TabActivity;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ListView mListView;
    SimpleAdapter mAdapter;
    List<Map<String, Object>> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = (ListView) findViewById(R.id.list_activities);
        dataList = getData();
        mAdapter = new SimpleAdapter(getApplication(), dataList, R.layout.list_simple_item, new String[]{"name"}, new int[]{R.id.txt_name});
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object o = dataList.get(position).get("class");
                startActivity(new Intent(MainActivity.this, (Class<?>) o));
            }
        });
        mListView.setAdapter(mAdapter);
    }

    protected List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new LinkedList<Map<String, Object>>();
        Map<String, Object> map;
//    //自定义dialog
//    map=new HashMap<String, Object>();
//    map.put("name", "自定义dialog");
//    map.put("class", CustomDialogActivity.class);
//    list.add(map);
//    //警告dialog
//    map=new HashMap<String, Object>();
//    map.put("name", "警告dialog");
//    map.put("class", AlertDialogActivity.class);
//    list.add(map);
//    //照片选择
//    map=new HashMap<String, Object>();
//    map.put("name", "照片选择");
//    map.put("class", PhotoSelectActivity.class);
//    list.add(map);
//
//    //删除过期文件
//    map=new HashMap<String, Object>();
//    map.put("name", "删除过期文件");
//    map.put("class", DeleteExpiredFilesActivity.class);
//    list.add(map);

        //swiperefredh
//        map=new HashMap<String, Object>();
//        map.put("name", "SwipeRefreshLayout");
//        map.put("class", SwipeRefreshActivity.class);
//        list.add(map);
//
//    //自定义ProgressDialog
//    map=new HashMap<String, Object>();
//    map.put("name", "自定义ProgressDialog");
//    map.put("class", ProgressDialogActivity.class);
//    list.add(map);


        //图片查看器
//        map=new HashMap<String, Object>();
//        map.put("name", "图片查看器");
//        map.put("class", PhotoViewActivity.class);
//        list.add(map);


        //图片下载进度
        map = new HashMap<>();
        map.put("name", "图片下载进度");
        map.put("class", GlideProgressActivity.class);
        list.add(map);

        //嵌套scrollview
        map = new HashMap<>();
        map.put("name", "嵌套scrollview");
        map.put("class", ScrollListViewActivity.class);
        list.add(map);

        //图片旋转
        map = new HashMap<>();
        map.put("name", "图片旋转");
        map.put("class", BitmapGestureActivity.class);
        list.add(map);

        //轮播
        map = new HashMap<>();
        map.put("name", "轮播");
        map.put("class", BannerActivity.class);
        list.add(map);

        //简单日期选择
        map = new HashMap<>();
        map.put("name", "简单日期选择");
        map.put("class", SimpleDateSelectActivity.class);
        list.add(map);

        //带进度条的webview
        map = new HashMap<>();
        map.put("name", "带进度条的webview");
        map.put("class", ProgressWebViewActivity.class);
        list.add(map);

        //popupwindow
        map = new HashMap<>();
        map.put("name", "popupwindow");
        map.put("class", PopupWindowActivity.class);
        list.add(map);

        //popupwindow
        map = new HashMap<>();
        map.put("name", "图片选择多选");
        map.put("class", MutiPhotoSelectActivity.class);
        list.add(map);

        //TabLayout
        map = new HashMap<>();
        map.put("name", "TabLayout");
        map.put("class", TabActivity.class);
        list.add(map);

        //RatingBar
        map = new HashMap<>();
        map.put("name", "评分");
        map.put("class", RatingBarActivity.class);
        list.add(map);

        //DrawerActivity
        map = new HashMap<>();
        map.put("name", "抽屉导航");
        map.put("class", DrawerActivity.class);
        list.add(map);

        //ObjectboxTestActivity
        map = new HashMap<>();
        map.put("name", "数据库");
        map.put("class", ObjectboxTestActivity.class);
        list.add(map);

        //RetrofitTestActivity
        map = new HashMap<>();
        map.put("name", "Retrofit");
        map.put("class", RetrofitTestActivity.class);
        list.add(map);

        //GsonTestActivity
        map = new HashMap<>();
        map.put("name", "Gson");
        map.put("class", GsonTestActivity.class);
        list.add(map);

        return list;
    }

}
