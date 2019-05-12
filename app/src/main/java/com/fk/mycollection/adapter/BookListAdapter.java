package com.fk.mycollection.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.fk.mycollection.R;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.ViewHolder>{

    public List<Map<String, String>> dataList = null;
    Context context;
    IOnItemClickListener itemClickListener;

    public BookListAdapter(Context context) {
        this.context = context;
    }

    public void notifyDataSetChanged(List<Map<String, String>> dataList) {
        if (dataList == null) {
            this.dataList = new LinkedList<>();
        } else {
            if (this.dataList == null) {
                this.dataList = new LinkedList<>();
            }
            this.dataList.addAll(dataList);
        }
        super.notifyDataSetChanged();
    }


    public void setItemClickListener(IOnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface IOnItemClickListener {
        void onItemClick(View v, Map<String, String> data);
    }

    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout
                .list_item_book, viewGroup, false);

        ViewHolder vh = new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(view, (Map) view.getTag());
                }
            }
        });
        return vh;
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Map<String, String> data = dataList.get(position);
        viewHolder.itemView.setTag(data);

    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        int count = 0;
        if (dataList != null) {
            count = dataList.size();
        }
        return count;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.image)
        ImageView image;
        @BindView(R.id.tv_auth)
        TextView tvAuth;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
