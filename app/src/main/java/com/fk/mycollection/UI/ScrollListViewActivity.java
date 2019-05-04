package com.fk.mycollection.UI;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.fk.mycollection.R;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ScrollListViewActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_list_view);
        ListView listView=(ListView)findViewById(R.id.listview);
        SimpleAdapter adapter=new SimpleAdapter(this,getDate(),android.R.layout.simple_list_item_1,new String[]{"name"},new int[]{android.R.id.text1});
        listView.setAdapter(adapter);
    }

    private List<Map<String,String>> getDate(){
        List<Map<String,String>> list=new LinkedList<>();
        for (int i = 0; i < 20; i++) {
            Map<String,String> map=new HashMap<>();
            map.put("name",""+i);
            list.add(map);
        }
        return list;
    }
}
