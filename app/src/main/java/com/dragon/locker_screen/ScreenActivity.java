package com.dragon.locker_screen;

import android.app.Activity;
import android.app.KeyguardManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.WindowManager;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dragon.locker_screen.adapter.ScreenAdapter;
import com.shuyu.gsyvideoplayer.GSYVideoManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maker on 2020/7/28.
 * Description:
 */
public class ScreenActivity extends Activity {
    private PowerManager.WakeLock mWakeLock;
    private KeyguardManager mKeyguardManager;
    private KeyguardManager.KeyguardLock mKeyguardLock;
    private RecyclerView mRvScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED //锁屏显示
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD //解锁
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON //保持屏幕不息屏
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);//点亮屏幕
        setContentView(R.layout.activity_screen);
        //下面就是根据自己的跟你需求来写，跟写一个Activity一样的
        //拿到传过来的数据

        List<String> urls = new ArrayList<>();
        urls.add("http://www.w3school.com.cn/example/html5/mov_bbb.mp4");
        urls.add("http://www.w3school.com.cn/example/html5/mov_bbb.mp4");
        urls.add("http://www.w3school.com.cn/example/html5/mov_bbb.mp4");
        urls.add("http://www.w3school.com.cn/example/html5/mov_bbb.mp4");
        urls.add("http://www.w3school.com.cn/example/html5/mov_bbb.mp4");
        urls.add("http://www.w3school.com.cn/example/html5/mov_bbb.mp4");
        urls.add("http://www.w3school.com.cn/example/html5/mov_bbb.mp4");
        urls.add("http://www.w3school.com.cn/example/html5/mov_bbb.mp4");
        urls.add("http://www.w3school.com.cn/example/html5/mov_bbb.mp4");
        urls.add("http://www.w3school.com.cn/example/html5/mov_bbb.mp4");

        mRvScreen = findViewById(R.id.rv_screen);
        ScreenAdapter recyclerNormalAdapter = new ScreenAdapter(this, urls);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        mRvScreen.setLayoutManager(linearLayoutManager);
        mRvScreen.setAdapter(recyclerNormalAdapter);


        mRvScreen.addOnScrollListener(new RecyclerView.OnScrollListener() {

            int firstVisibleItem, lastVisibleItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                //大于0说明有播放
                if (GSYVideoManager.instance().getPlayPosition() >= 0) {
                    //当前播放的位置
                    int position = GSYVideoManager.instance().getPlayPosition();
                    //对应的播放列表TAG
                    if (GSYVideoManager.instance().getPlayTag().equals(ScreenAdapter.TAG)
                            && (position < firstVisibleItem || position > lastVisibleItem)) {

                        //是否全屏
                        if (!GSYVideoManager.isFullState(ScreenActivity.this)) {
                            GSYVideoManager.releaseAllVideos();
                            recyclerNormalAdapter.notifyItemChanged(position);
                        }
                    }
                }
            }
        });


    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.e("onResume:", "onResume");
        GSYVideoManager.onResume(false);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GSYVideoManager.releaseAllVideos();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("onPause:", "onPause");
        GSYVideoManager.onPause();
//        mPlayer.stop();
    }

    @Override
    protected void onStop() {
        super.onStop();
//        mPlayer.release();
    }

    @Override
    public void onBackPressed() {
        if (GSYVideoManager.backFromWindowFull(this)) {
            return;
        }
        super.onBackPressed();
    }

}
