package com.example.qqsliding.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.qqsliding.R;
import com.example.qqsliding.adapter.ItemAdapter;
import com.example.qqsliding.adapter.LeftItemAdapter;
import com.example.qqsliding.widget.DragLayout;
import com.nineoldandroids.view.ViewHelper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DragLayout dl;
    private ListView lv;
    private ListView listView;
    private List<String> mLists = new ArrayList<>();

    private ImageView head;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dl = (DragLayout) findViewById(R.id.dl);
        head = (ImageView) findViewById(R.id.head);



        final ItemAdapter adapter = initListView();

        initListener(adapter);


       /* dl.setDragStateListener(new DragLayout.OnDragStatusListener() {
            @Override
            public void onClose() {
                Log.d("MainActivity", "关闭");
            }

            @Override
            public void onOpen() {
                Log.d("MainActivity", "打开");
            }

            @Override
            public void onDraging(float percent) {
                Log.d("MainActivity", "拖拽");
            }
        });*/
    }

    private void initListener(final ItemAdapter adapter) {
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    //在滑动的时候关闭拖拽出来的item
                    adapter.closeAllLayout();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

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

    @NonNull
    private ItemAdapter initListView() {
        lv = (ListView) findViewById(R.id.lv);
        lv.setAdapter(new LeftItemAdapter(this));
        // Toast.makeText(this, "你好", 1).show();

        initData();
        listView = (ListView) findViewById(R.id.listView);
        final ItemAdapter adapter = new ItemAdapter(this,mLists);

        //在这里把adapter传递了，为了使当我们的item拖拽出来的时候，我们要禁止左滑这个item的时候防止我的左侧view的出现
        dl.setAdapterInterface(adapter);
        listView.setAdapter(adapter);
        return adapter;
    }

    private void initData() {
        for (int i = 0; i < 20; i++) {
            mLists.add("item" + i);
        }

    }
}
