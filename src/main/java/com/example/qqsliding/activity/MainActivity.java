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

    private void initRecycleView() {
        recycleview = (RecyclerView) findViewById(R.id.recycleview);
        recycleview.setLayoutManager(new LinearLayoutManager(this));
        recycleview.setItemAnimator(new DefaultItemAnimator());
        recycleview.addItemDecoration(new DividerItemDecoration(MainActivity.this, DividerItemDecoration.VERTICAL_LIST));
    }

    private void initViews() {
        dl = (DragLayout) findViewById(R.id.dl);
        head = (ImageView) findViewById(R.id.head);
        lv = (ListView) findViewById(R.id.lv);
        lv.setAdapter(new LeftItemAdapter(this));
    }

    private void initListener() {

        final ItemRecycleAdapter adapter = new ItemRecycleAdapter(MainActivity.this, mLists);
        dl.setAdapterInterface(adapter);
        recycleview.setAdapter(adapter);

        recycleview.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    adapter.closeAllLayout();
                }
            }
        });
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
                dl.open();
            }
        });
    }


    private void initData() {
        for (int i = 0; i < 20; i++) {
            mLists.add("item" + i);
        }

    }
}
