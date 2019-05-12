package com.fk.mycollection.View;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;


import com.fk.mycollection.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 */
public class VersionPromptFragment extends DialogFragment {
    private static final String ARG_PARAM1 = "title";
    private static final String ARG_PARAM2 = "content";

    // TODO: Rename and change types of parameters
    private String title;
    private String content;

    @BindView(R. id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_info)
    TextView tvInfo;

    public VersionPromptFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment VersionPromptFragment.
     */
    public static VersionPromptFragment newInstance(String title, String content) {
        VersionPromptFragment fragment = new VersionPromptFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, title);
        args.putString(ARG_PARAM2, content);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString(ARG_PARAM1);
            content = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_version_prompt, container, false);
        ButterKnife.bind(this, v);
        tvTitle.setText(title);
        tvInfo.setText(content);
        return v;
    }

    @OnClick({R.id.btn_cancle, R.id.btn_confirm})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancle:
                dismiss();
                break;
            case R.id.btn_confirm:
                ((IDoConfirm) getActivity()).doConfirm();
                dismiss();
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            DisplayMetrics dm = new DisplayMetrics();
            //设置弹框的占屏宽
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            dialog.getWindow().setLayout((int) (dm.widthPixels * 0.8), (int) (dm.heightPixels * 0.6));
        }
    }

    public interface IDoConfirm {
        void doConfirm();
    }
}
