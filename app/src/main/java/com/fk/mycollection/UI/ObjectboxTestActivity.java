package com.fk.mycollection.UI;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.fk.mycollection.ObjectBox;
import com.fk.mycollection.R;
import com.fk.mycollection.bean.UserBean;
import com.fk.mycollection.bean.UserBean_;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.objectbox.Box;

public class ObjectboxTestActivity extends AppCompatActivity {

    Box<UserBean> userBeanBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_objectbox_test);
        ButterKnife.bind(this);
        UserBean user = new UserBean();
        user.setName("jim");
        userBeanBox = ObjectBox.get().boxFor(UserBean.class);
        userBeanBox.removeAll();
        userBeanBox.put(user);
    }

    @OnClick({R.id.btn_show, R.id.btn_edit})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_show:
                List<UserBean> userBeans = userBeanBox.getAll();
                for (UserBean userBean : userBeans) {
                    Toast.makeText(this, userBean.getName(), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_edit:
                UserBean user = userBeanBox.query().equal(UserBean_.name, "jim").build().findFirst();
                user.setName("Tom");
                userBeanBox.put(user);
                break;
        }
    }
}
