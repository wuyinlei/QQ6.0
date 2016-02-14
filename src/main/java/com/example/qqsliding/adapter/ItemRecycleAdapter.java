package com.example.qqsliding.adapter;

import android.content.Context;
import android.graphics.PointF;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.qqsliding.R;
import com.example.qqsliding.widget.SwipeLayout;
import com.example.qqsliding.widget.ViscosityListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 若兰 on 2016/2/2.
 * 一个懂得了编程乐趣的小白，希望自己
 * 能够在这个道路上走的很远，也希望自己学习到的
 * 知识可以帮助更多的人,分享就是学习的一种乐趣
 * QQ:1069584784
 * csdn:http://blog.csdn.net/wuyinlei
 */

public class ItemRecycleAdapter extends RecyclerView.Adapter<ItemRecycleAdapter.MyViewHolder> {


    private Context mContext;
    private LayoutInflater mInflater;

    private List<Integer> removePoint = new ArrayList<>();

    private List<SwipeLayout> openItems;
    private List<String> mDatas;

    boolean isOpen = false;

    public ItemRecycleAdapter(Context context, List<String> lists) {
        mContext = context;
        openItems = new ArrayList<>();
        mDatas = lists;
    }

    public int getOpenItems() {
        return openItems.size();
    }


    /**
     * 关闭所有的layout
     */
    public void closeAllLayout() {
        if (openItems.size() == 0)
            return;
        for (SwipeLayout l : openItems) {
            l.close();
        }
        openItems.clear();
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mInflater = LayoutInflater.from(parent.getContext());
        View view = mInflater.inflate(R.layout.item_layout, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        SwipeLayout swipeLayout = (SwipeLayout) holder.itemView;
        swipeLayout.setSwipeLayoutListener(new SwipeLayout.OnSwipeLayoutListener() {
            @Override
            public void onClose(SwipeLayout mSwipeLayout) {
                openItems.remove(mSwipeLayout);

            }

            @Override
            public void onOpen(SwipeLayout mSwipeLayout) {
                openItems.add(mSwipeLayout);
                isOpen = true;
            }

            @Override
            public void onDraging(SwipeLayout mSwipeLayout) {

            }

            @Override
            public void onStartClose(SwipeLayout mSwipeLayout) {

            }

            @Override
            public void onStartOpen(SwipeLayout mSwipeLayout) {

                closeAllLayout();
                openItems.add(mSwipeLayout);
            }
        });

        holder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatas.remove(position);
                notifyDataSetChanged();
            }
        });
        holder.tvName.setText(mDatas.get(position));
        //SwipeLayout swipeLayout = (SwipeLayout) holder.itemView;
        /**
         * 在这里实现只能拖拽出来一个item，拖拽其他的时候，要把之前已经拖拽出的给关闭
         */

        //得到红点的布局
        TextView textView = holder.point;

        //判断是否已经移除了，如果是在移除的集合中，代表已经移除，如果没有，就没有移除
        boolean isRemoved = !removePoint.contains(position);
        //判断是否显示  如果已经移除了，就不显示，没有移除就显示
        textView.setVisibility(isRemoved ? View.VISIBLE : View.GONE);
        if (isRemoved){
            textView.setText(position + "");
            textView.setTag(position);
            if (openItems.size() == 0) {
                ViscosityListener mViscosityListener = new ViscosityListener(mContext, textView) {
                    @Override
                    public void onDisappear(PointF mDragCenter) {
                        super.onDisappear(mDragCenter);
                        removePoint.add(position);
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onReset(boolean isOutOfRange) {
                        super.onReset(isOutOfRange);
                        notifyDataSetChanged();
                    }

                };
                    textView.setOnTouchListener(mViscosityListener);

            }
        }


    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }


    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvCall, tvDelete;
        TextView tvName;
        TextView point;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvCall = (TextView) itemView.findViewById(R.id.tvCall);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvDelete = (TextView) itemView.findViewById(R.id.tvDelete);
            point = (TextView) itemView.findViewById(R.id.point);
        }
    }
}
