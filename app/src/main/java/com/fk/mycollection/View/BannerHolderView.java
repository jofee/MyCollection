package com.fk.mycollection.View;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.convenientbanner.holder.Holder;
import com.fk.mycollection.R;

import java.util.Map;

/**
 * Created by Administrator on 2017/11/28.
 */

public class BannerHolderView implements Holder<Map<String,String>> {
    View v;
    @Override
    public View createView(Context context) {
         v= LayoutInflater.from(context).inflate(R.layout.layout_banner,null);
        return v;
    }

    @Override
    public void UpdateUI(Context context,int position, Map<String,String> data) {
        TextView name=(TextView)v.findViewById(R.id.tv_name);

        name.setText("hhhhh"+position);
    }
}
