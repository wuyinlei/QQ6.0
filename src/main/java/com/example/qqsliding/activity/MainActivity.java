package com.example.qqsliding.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.example.qqsliding.R;
import com.example.qqsliding.adapter.LeftItemAdapter;
import com.example.qqsliding.widget.DragLayout;

public class MainActivity extends AppCompatActivity {

    private DragLayout dl;
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dl = (DragLayout) findViewById(R.id.dl);

        lv = (ListView) findViewById(R.id.lv);
        lv.setAdapter(new LeftItemAdapter(this));
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
}
