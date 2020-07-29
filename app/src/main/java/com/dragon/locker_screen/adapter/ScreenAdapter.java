package com.dragon.locker_screen.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dragon.locker_screen.R;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Maker on 2020/7/28.
 * Description:
 */
public class ScreenAdapter extends RecyclerView.Adapter<ScreenAdapter.ScreenViewHold> {
    private Context mContext;
    private List<String> mUrls;
    GSYVideoOptionBuilder gsyVideoOptionBuilder;

    public final static String TAG = "RecyclerViewList";
//    private ExoPlayer mPlayer;

//    public ScreenAdapter(Context mContext, List<String> mUrls,ExoPlayer mPlayer) {
//        this.mContext = mContext;
//        this.mUrls = mUrls;
//        this.mPlayer = mPlayer;
//    }
    public ScreenAdapter(Context mContext, List<String> mUrls) {
        this.mContext = mContext;
        this.mUrls = mUrls;
        gsyVideoOptionBuilder = new GSYVideoOptionBuilder();
    }

    @NonNull
    @Override
    public ScreenAdapter.ScreenViewHold  onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_screen,parent,false);
        return new ScreenViewHold(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScreenAdapter.ScreenViewHold holder, int position) {
        String url = mUrls.get(position);


//        String title;
//        if (position % 2 == 0) {
////            url = "https://res.exexm.com/cw_145225549855002";
//            title = "这是title";
//        } else {
////            url = "http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4";
//            title = "哦？Title？";
//        }


        Map<String, String> header = new HashMap<>();
        header.put("ee", "33");

        //防止错位，离开释放
        //gsyVideoPlayer.initUIState();
        gsyVideoOptionBuilder
                .setIsTouchWiget(false)
                //.setThumbImageView(imageView)
                .setUrl(url)
                .setVideoTitle("title")
                .setCacheWithPlay(false)
                .setRotateViewAuto(true)
                .setLockLand(true)
                .setPlayTag(TAG)
                .setMapHeadData(header)
                .setShowFullAnimation(true)
                .setNeedLockFull(true)
                .setPlayPosition(position)
                .setVideoAllCallBack(new GSYSampleCallBack() {
                    @Override
                    public void onPrepared(String url, Object... objects) {
                        super.onPrepared(url, objects);
                        if (!holder.mPlayerView.isIfCurrentIsFullscreen()) {
                            //静音
                            GSYVideoManager.instance().setNeedMute(true);
                        }

                    }

                    @Override
                    public void onQuitFullscreen(String url, Object... objects) {
                        super.onQuitFullscreen(url, objects);
                        //全屏不静音
                        GSYVideoManager.instance().setNeedMute(true);
                    }

                    @Override
                    public void onEnterFullscreen(String url, Object... objects) {
                        super.onEnterFullscreen(url, objects);
                        GSYVideoManager.instance().setNeedMute(false);
                        holder.mPlayerView.getCurrentPlayer().getTitleTextView().setText((String)objects[0]);
                    }
                }).build(holder.mPlayerView);


        //增加title
        holder.mPlayerView.getTitleTextView().setVisibility(View.GONE);

        //设置返回键
        holder.mPlayerView.getBackButton().setVisibility(View.GONE);

        //设置全屏按键功能
        holder.mPlayerView.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resolveFullBtn(holder.mPlayerView);
            }
        });
    }


    @Override
    public int getItemCount() {
        return mUrls.size();
    }




    class ScreenViewHold extends RecyclerView.ViewHolder{
        StandardGSYVideoPlayer mPlayerView;
        public ScreenViewHold(@NonNull View itemView) {
            super(itemView);
            mPlayerView = itemView.findViewById(R.id.video_item_player);
        }
    }

    /**
     * 全屏幕按键处理
     */
    private void resolveFullBtn(final StandardGSYVideoPlayer standardGSYVideoPlayer) {
        standardGSYVideoPlayer.startWindowFullscreen(mContext, true, true);
    }

}
