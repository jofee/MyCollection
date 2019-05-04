package com.fk.mycollection.UI;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.fk.mycollection.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PopupWindowActivity extends AppCompatActivity {

    @BindView(R.id.btn_show)
    Button mBtnShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_window);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_show) void show(){
        showPopupWindow();
    }

    protected void showPopupWindow(){
        View contentView= LayoutInflater.from(getApplication()).inflate(R.layout.popupwindow, null);
        final PopupWindow mPopup=new PopupWindow(getApplication());
        mPopup.setContentView(contentView);
        mPopup.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mPopup.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        Button btn_test=(Button)contentView.findViewById(R.id.btn_show);
        btn_test.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(getApplication(), "test", Toast.LENGTH_SHORT).show();
            }
        });

        mPopup.setFocusable(true);
        mPopup.setOutsideTouchable(true);
        mPopup.setBackgroundDrawable(new BitmapDrawable());
        //显示在button的下方
        int[] location = new int[2];
        mBtnShow.getLocationOnScreen(location);
        // 设置动画
        //mPopup.setAnimationStyle(R.style.PopupAnimationCard);

        mPopup.showAtLocation(mBtnShow, Gravity.NO_GRAVITY, location[0] + mBtnShow.getWidth(),
                location[1] + mBtnShow.getHeight());
    }

}
