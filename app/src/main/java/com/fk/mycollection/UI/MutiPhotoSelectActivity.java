package com.fk.mycollection.UI;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.fk.mycollection.R;
import com.fk.mycollection.adapter.PhotoGridViewAdapter;
import com.fk.mycollection.utill.CommonTools;
import com.fk.mycollection.utill.PermissionUtil;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MutiPhotoSelectActivity extends AppCompatActivity {

    private static final int MY_PERMISSION_REQUEST_CODE = 0;
    private static final int IMAGE_REQUEST_CODE = 1;// 本地图片请求标志
    private static final int CAMERA_REQUEST_CODE = 2;// 相机请求标志

    String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission
            .WRITE_EXTERNAL_STORAGE};

    @BindView(R.id.ll_main)
    LinearLayout mLlMain;

    @BindView(R.id.gv_pic)
    protected RecyclerView mGvPic;// 图片
    private PhotoGridViewAdapter gridviewAdapter;
    RecyclerView.LayoutManager layoutManager;
    private LinkedList<String> photoPathList = new LinkedList<String>();// 图片路径(只有拍照)，可删除
    private LinkedList<String> photoUriList = new LinkedList<String>();// 图片uri(包括拍照和相册选择)
    private Uri imageUri;// 拍照uri
    private String imagePath;// 拍照路径
    private PopupWindow mPowChangeImg;// 选择图片的PopupWindow

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muti_photo_select);
        ButterKnife.bind(this);
        initGridView();
    }

    private void initGridView() {
        gridviewAdapter = new PhotoGridViewAdapter(this,
                new PhotoGridViewAdapter.IOnAddClickListener() {

                    @Override
                    public void onAddClickListener(View v, int position) {
                        if (Build.VERSION.SDK_INT >= 23) {
                            if (checkPermissionAllGranted(permissions)) {
                                showPopThImg();
                            } else {
                                ActivityCompat.requestPermissions(MutiPhotoSelectActivity.this,
                                        permissions,
                                        MY_PERMISSION_REQUEST_CODE);
                            }
                        } else {
                            showPopThImg();
                        }

                    }
                }, new PhotoGridViewAdapter.IOnPhotoClickListener() {

            @Override
            public void onPhotoClickListener(View v, int position) {
            }
        }, new PhotoGridViewAdapter.IOnDeleteClickListener() {

            @Override
            public void onDeleteClickListener(View v, int position) {
                photoUriList.remove(position);
                gridviewAdapter.removeItem(position);
            }
        });
        layoutManager=new GridLayoutManager(this,3);
        mGvPic.setLayoutManager(layoutManager);
        mGvPic.setAdapter(gridviewAdapter);
    }

    /**
     * 检查是否拥有指定的所有权限
     */
    private boolean checkPermissionAllGranted(String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager
                    .PERMISSION_GRANTED) {
                // 只要有一个权限没有被授予, 则直接返回 false
                return false;
            }
        }
        return true;
    }

    /**
     * 选择 从相机 还是相册 修改头像
     *
     * @Title: showPopupWindow void
     * @author limm
     * @since 2015-10-19 V 1.0
     */
    @SuppressWarnings("deprecation")
    private void showPopThImg() {
        /**
         * 从底部弹出的布局 layout_pop_camera为 布局文件
         */
        View view = (RelativeLayout) LayoutInflater.from(this).inflate(
                R.layout.layout_pop_camera, null);
        RelativeLayout relativeLayout = (RelativeLayout) view
                .findViewById(R.id.relative_dialog);
        Button firstButton = (Button) view.findViewById(R.id.addpic_bot_btn1);
        Button secButton = (Button) view.findViewById(R.id.addpic_bot_btn2);
        Button thirdButton = (Button) view.findViewById(R.id.addpic_bot_btn3);
        if (mPowChangeImg == null) {

            mPowChangeImg = new PopupWindow(this);
            mPowChangeImg.setBackgroundDrawable(new BitmapDrawable());

            // popupWindow.setFocusable(true); // 设置PopupWindow可获得焦点
            mPowChangeImg.setTouchable(true); // 设置PopupWindow可触摸
            mPowChangeImg.setOutsideTouchable(true); // 设置非PopupWindow区域可触摸

            mPowChangeImg.setContentView(view);

            mPowChangeImg.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            mPowChangeImg.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

            /**
             * PopupWindow 的动画样式
             */
//            mPowChangeImg.setAnimationStyle(R.style.MyDialog);
        }

        mPowChangeImg.showAtLocation(mLlMain, Gravity.BOTTOM, 0, 0);

        mPowChangeImg.update();

        // 从相册拍照
        firstButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // 打开图库
                Intent intentFromGallery = new Intent();
                intentFromGallery.setType("image/*"); // 设置文件类型
                intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intentFromGallery, IMAGE_REQUEST_CODE);
//                overridePendingTransition(R.anim.enter_righttoleft,
//                        R.anim.exit_righttoleft);
                mPowChangeImg.dismiss();
            }
        });

        // 拍照
        secButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                TakePhoto();
                mPowChangeImg.dismiss();

            }
        });

        // 取消
        thirdButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                mPowChangeImg.dismiss();
                // mAllLayout.setVisibility(View.GONE);
            }

        });
        relativeLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                mPowChangeImg.dismiss();
            }
        });
    }

    /**
     * 拍照
     */
    public void TakePhoto() {
        //
        Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 判断存储卡是否可以用，可用进行存储
        if (CommonTools.hasSdcard()) {
            intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, getImageUri());
        } else {
            Toast.makeText(this, "未找到存储卡！", Toast.LENGTH_LONG).show();
        }
        List<ResolveInfo> resInfoList = getPackageManager().queryIntentActivities(intentFromCapture, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            grantUriPermission(packageName, imageUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        startActivityForResult(intentFromCapture, CAMERA_REQUEST_CODE);
    }

    private Uri getImageUri() {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());
        String imageName = timeStamp + ".jpg";
        // imageUri = Uri.fromFile(new File(Environment
        // .getExternalStorageDirectory(), imageName));
        File rootPath = new File(Environment.getExternalStorageDirectory()
                .getPath() + "/temp");
        // File rootPath = new File(getFilesDir() + "/qdcc");
        if (!rootPath.exists()) {
            rootPath.mkdirs();
        }
        imagePath = rootPath + "/" + imageName;
//        imageUri = Uri.fromFile(new File(rootPath, imageName));
        imageUri = FileProvider.getUriForFile(this, this.getPackageName()+".fileprovider", new File
                (rootPath, imageName));
        return imageUri;
    }

    /**
     * 显示图片
     */
    private void getImageToView(Uri uri) {

        if (uri != null) {
            //
            photoUriList.add(uri.toString());
            photoPathList.add(imagePath);
            gridviewAdapter.addItem(uri.toString(),photoUriList.size()-1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case IMAGE_REQUEST_CODE:
                    getImageToView(data.getData());
                    break;
                case CAMERA_REQUEST_CODE:
                    getImageToView(imageUri);
                    break;
            }
        }
    }

    /**
     * 申请权限结果返回处理
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_PERMISSION_REQUEST_CODE) {
            if (PermissionUtil.verifyPermissions(grantResults)) {
                // 如果所有的权限都授予了, 则执行代码
                showPopThImg();
            } else {
                // 弹出对话框告诉用户需要权限的原因, 并引导用户去应用权限管理中手动打开权限按钮

            }
        }
    }


    private void picdata(){
        try {
            String date= CommonTools.getBase64(this, imageUri, imagePath, 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
