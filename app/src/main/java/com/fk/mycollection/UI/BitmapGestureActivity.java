package com.fk.mycollection.UI;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.fk.mycollection.R;
import com.fk.mycollection.utill.TouchImageView;

public class BitmapGestureActivity extends AppCompatActivity {

    String url="https://pic.36krcnd.com/201711/14001632/2tybodtxvb1tho25!feature";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bitmap_gesture);
        final ConstraintLayout main=(ConstraintLayout)findViewById(R.id.main);


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final Bitmap bitmap = Glide.with(getApplicationContext()).load(url).asBitmap().into(500, 500).get();
                    BitmapGestureActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            TouchImageView img = new TouchImageView(getApplicationContext(), bitmap);
                            main.addView(img,500,500);
                        }
                    });
                }catch ( Exception e){
                    e.getMessage();
                }
            }
        }).start();
    }
}
