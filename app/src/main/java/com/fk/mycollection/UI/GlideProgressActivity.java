package com.fk.mycollection.UI;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.stream.StreamModelLoader;
import com.fk.mycollection.R;

import java.io.InputStream;

public class GlideProgressActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glide_progress);
        ImageView imageView=(ImageView)findViewById(R.id.iv_test);
//        Glide.with(this).using(ProgressModelLoader).load("http://goo.gl/gEgYUd").into(imageView);
    }

    public class ProgressModelLoader implements StreamModelLoader<String>{

        @Override
        public DataFetcher<InputStream> getResourceFetcher(String model, int width, int height) {
            return new ProgressDataFetcher();
        }
    }

    public class ProgressDataFetcher implements DataFetcher<InputStream>{

        @Override
        public InputStream loadData(Priority priority) throws Exception {
            return null;
        }

        @Override
        public void cleanup() {

        }

        @Override
        public String getId() {
            return null;
        }

        @Override
        public void cancel() {

        }
    }
}
