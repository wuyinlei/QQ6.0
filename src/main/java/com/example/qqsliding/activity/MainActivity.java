package com.example.qqsliding.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.qqsliding.R;
import com.example.qqsliding.adapter.DividerItemDecoration;
import com.example.qqsliding.adapter.ItemRecycleAdapter;
import com.example.qqsliding.adapter.LeftItemAdapter;
import com.example.qqsliding.widget.DragLayout;
import com.nineoldandroids.view.ViewHelper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DragLayout dl;
    private ListView lv;
    private RecyclerView recycleview;
    private List<String> mLists = new ArrayList<>();

    private ImageView head;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        initRecycleView();

        initData();

        initListener();
    }

    /**
     * 设置recycleview的一些参数
     */
    private void initRecycleView() {
        recycleview.setLayoutManager(new LinearLayoutManager(this));
        recycleview.setItemAnimator(new DefaultItemAnimator());
        recycleview.addItemDecoration(new DividerItemDecoration(MainActivity.this, DividerItemDecoration.VERTICAL_LIST));
    }

    /**
     * 初始化布局控件
     */
    private void initViews() {
        dl = (DragLayout) findViewById(R.id.dl);
        head = (ImageView) findViewById(R.id.head);
        lv = (ListView) findViewById(R.id.lv);
        lv.setAdapter(new LeftItemAdapter(this));
        recycleview = (RecyclerView) findViewById(R.id.recycleview);
    }

    /**
     * 初始化listener
     */
    private void initListener() {

        final ItemRecycleAdapter adapter = new ItemRecycleAdapter(MainActivity.this, mLists);
        dl.setAdapterInterface(adapter);
        recycleview.setAdapter(adapter);
/*
        *//**
         * 监听recycleview的滑动事件，在滑动的时候要关闭打开的swipelayout
         *//*
        recycleview.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    adapter.closeAllLayout();
                }
            }
        });*/

        recycleview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    adapter.closeAllLayout();
                }
            }
        });

        /**
         * DragLayout的事件监听
         */
        dl.setDragStateListener(new DragLayout.OnDragStatusListener() {
            @Override
            public void onClose() {

            }

            @Override
            public void onOpen() {
            }

            @Override
            public void onDraging(float percent) {

                //通过拖拽实现主界面的左上的头像的隐藏
                ViewHelper.setAlpha(head, 1 - percent);
            }
        });

        head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.closeAllLayout();
                dl.open();
            }
        });
    }

    /**
     * 初始化数据
     */
    private void initData() {
        for (int i = 0; i < 20; i++) {
            mLists.add("item" + i);
        }

    }
}
