package com.fk.mycollection.UI;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.fk.mycollection.R;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SimpleDateSelectActivity extends AppCompatActivity {

    @BindView(R.id.tv_date)
    TextView mTvDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_date_select);
        ButterKnife.bind(this);
        mTvDate.setText("asdfsd");
    }

    @OnClick({R.id.tv_date})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_date:
                selectDate(mTvDate);
                break;
        }
    }

    private void selectDate(final TextView dateView) {
        Calendar c = Calendar.getInstance();
        // TODO Auto-generated method stub
        new DatePickerDialog(this, R.style.AppTheme,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        monthOfYear += 1;// 月份从0开始！

                        String str = year
                                + "-"
                                + (monthOfYear < 10 ? "0" + monthOfYear
                                : monthOfYear)
                                + "-"
                                + (dayOfMonth < 10 ? "0" + dayOfMonth
                                : dayOfMonth);

                        dateView.setText(str);// 悬挂到期时间

                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH)).show();
    }
}
