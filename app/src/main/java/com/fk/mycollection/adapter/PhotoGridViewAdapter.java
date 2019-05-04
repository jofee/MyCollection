package com.fk.mycollection.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.fk.mycollection.R;
import com.fk.mycollection.utill.CommonTools;

import java.util.LinkedList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PhotoGridViewAdapter extends RecyclerView.Adapter<PhotoGridViewAdapter.ViewHolder> {

    Context context;
    LinkedList<String> photoList;
    IOnAddClickListener addListener;
    IOnPhotoClickListener photoListener;
    IOnDeleteClickListener deleteListener;
    int width;
    int height;
    int limitCount = 2;
    boolean showAddView = true;

    public PhotoGridViewAdapter(Context context,
                                IOnAddClickListener addListener,
                                IOnPhotoClickListener photoListener,
                                IOnDeleteClickListener deleteListener) {
        this.context = context;
        this.addListener = addListener;
        this.photoListener = photoListener;
        this.deleteListener = deleteListener;
        int screenWidth = CommonTools.getScreenWidth(context);
        int screenHeight = CommonTools.getScreenHeight(context);
        width = (screenWidth - 60) / 4;
        height = width;
        photoList = new LinkedList<>();
        photoList.add(null);
    }

    public void setData(LinkedList<String> photoList) {
        this.photoList = photoList;
    }

    public interface IOnPhotoClickListener {
        void onPhotoClickListener(View v, int position);
    }

    public interface IOnAddClickListener {
        void onAddClickListener(View v, int position);
    }

    public interface IOnDeleteClickListener {
        void onDeleteClickListener(View v, int position);
    }

    public void addItem(String content, int position) {
        photoList.add(position, content);
        notifyItemInserted(position); //Attention!
        if (photoList.size() - 1 == limitCount) {
            photoList.remove(photoList.size() - 1);
            showAddView = false;
        }
        notifyItemRemoved(photoList.size() - 1); //Attention!
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        photoList.remove(position);
        notifyItemRemoved(position);//Attention!
        if (!showAddView) {
            photoList.add(null);
            showAddView = true;
        }
        notifyItemInserted(photoList.size()); //Attention!
        notifyDataSetChanged();
    }


    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.list_item_grid_pic, viewGroup, false);
        view.setLayoutParams(new FrameLayout.LayoutParams(width, height));
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        if (position == photoList.size() - 1 && showAddView) {
            // 添加图片按钮
            Glide.with(context).load(R.drawable.btn_add_photo).fitCenter()
                    .into(viewHolder.mIvPhoto);
            viewHolder.mIbDelete.setVisibility(View.GONE);
            viewHolder.mIvPhoto.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (addListener != null) {
                        addListener.onAddClickListener(v, position);
                    }
                }
            });
        } else {
            //
            Glide.with(context).load(photoList.get(position)).centerCrop()
                    .placeholder(R.mipmap.ic_launcher).into(viewHolder.mIvPhoto);
            viewHolder.mIbDelete.setVisibility(View.VISIBLE);
            viewHolder.mIvPhoto.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (photoListener != null) {
                        photoListener.onPhotoClickListener(v, position);
                    }
                }
            });
            viewHolder.mIbDelete.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (deleteListener != null) {
                        deleteListener.onDeleteClickListener(v, position);
                    }

                }
            });
        }
    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        return photoList.size();
    }


    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_photo)
        public ImageView mIvPhoto;
        @BindView(R.id.ib_delete)
        public ImageButton mIbDelete;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
//            AutoUtils.autoSize(view);
        }
    }

}
