package com.example.qqsliding.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qqsliding.utils.ItemDataUtils;
import com.example.qqsliding.R;
import com.example.qqsliding.bean.ItemBean;

import java.util.List;

/**
 * Created by 若兰 on 2016/1/22.
 * 一个懂得了编程乐趣的小白，希望自己
 * 能够在这个道路上走的很远，也希望自己学习到的
 * 知识可以帮助更多的人,分享就是学习的一种乐趣
 * QQ:1069584784
 * csdn:http://blog.csdn.net/wuyinlei
 */

public class LeftItemAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<ItemBean> mItemBeans;

    public LeftItemAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        mItemBeans = ItemDataUtils.getItemBeans();
    }

    @Override
    public int getCount() {
        return mItemBeans != null ? mItemBeans.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return mItemBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        if (convertView == null){
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_left_layout,null);
            holder.item_img = (ImageView) convertView.findViewById(R.id.item_img);
            holder.item_tv = (TextView) convertView.findViewById(R.id.item_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ItemBean itemBean = mItemBeans.get(position);

        holder.item_tv.setText(itemBean.getTitle());
        holder.item_img.setImageResource(itemBean.getImg());

        return convertView;
    }

    class ViewHolder {
        ImageView item_img;
        TextView item_tv;
    }
}
