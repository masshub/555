package com.dragon.locker_screen;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dragon.locker_screen.adapter.ScreenAdapter;
import com.dragon.locker_screen.channel.BasicMessageChannelPlugin;
import com.dragon.locker_screen.utils.DateUtil;
import com.dragon.locker_screen.utils.PermissionUtils;
import com.dragon.locker_screen.utils.PowerUtil;
import com.shuyu.gsyvideoplayer.GSYVideoManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.plugin.common.BasicMessageChannel;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.StandardMessageCodec;
import io.flutter.plugins.GeneratedPluginRegistrant;


/**
 * Created by Maker on 2020/7/28.
 * Description:
 */
public class ScreenActivity extends FlutterActivity {
    private PowerManager.WakeLock mWakeLock;
    private KeyguardManager mKeyguardManager;
    private KeyguardManager.KeyguardLock mKeyguardLock;
    private RecyclerView mRvScreen;
    private TextView mTvTime;
    private TextView mTvBatteryLevel;
    private TextView mTvDate;
    private ImageView mIvBattery;
    private ImageView mIvPhone;
    private ImageView mIvLock;
    private ImageView mIvCamera;
    private ImageView mIvPermission;
    private Calendar calendar = GregorianCalendar.getInstance();
    private SimpleDateFormat weekFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
    private SimpleDateFormat monthFormat = new SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault());
    protected UIChangingReceiver mUIChangingReceiver;

    private  BasicMessageChannel<Object> channel;
    private List<String> urls = new ArrayList<>();
    private ScreenAdapter mAdapter;



    public void registerLockerReceiver() {
        if (mUIChangingReceiver != null) {
            return;
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        filter.addAction(Intent.ACTION_TIME_TICK);
        filter.addAction(Intent.ACTION_POWER_CONNECTED);
        filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_USER_PRESENT);

        mUIChangingReceiver = new UIChangingReceiver();
        registerReceiver(mUIChangingReceiver, filter);
    }

    public void unregisterLockerReceiver() {
        if (mUIChangingReceiver == null) {
            return;
        }
        unregisterReceiver(mUIChangingReceiver);
        mUIChangingReceiver = null;
    }

    protected void onActionReceived(String action) {
        if (!TextUtils.isEmpty(action)) {
            if (action.equals(Intent.ACTION_BATTERY_CHANGED)) {
                updateBatteryUI();
            } else if (action.equals(Intent.ACTION_TIME_TICK)) {
                updateTimeUI();
            } else if (action.equals(Intent.ACTION_POWER_CONNECTED)) {
//                mChargeContainer.setVisibility(View.VISIBLE);
                updateBatteryUI();
            } else if (action.equals(Intent.ACTION_POWER_DISCONNECTED)) {
//                mChargeContainer.setVisibility(View.GONE);
                updateBatteryUI();
            } else if (action.equals(Intent.ACTION_SCREEN_OFF)) {
            }
        }
    }

    @Override
    public void configureFlutterEngine(@NonNull FlutterEngine flutterEngine) {
        super.configureFlutterEngine(flutterEngine);
        GeneratedPluginRegistrant.registerWith(flutterEngine);
        channel = new BasicMessageChannel<Object>(flutterEngine.getDartExecutor().getBinaryMessenger(), "BasicMessageChannelPlugin", StandardMessageCodec.INSTANCE);
        channel.setMessageHandler(new BasicMessageChannel.MessageHandler<Object>() {
            @Override
            public void onMessage(@Nullable Object message, @NonNull BasicMessageChannel.Reply<Object> reply) {
                Map<Object,Object> data = (Map<Object, Object>) message;
                Log.d("ChannelLog",data.get("data").toString());
                reply.reply("android收到flutter的消息");
                urls.addAll((Collection<? extends String>) data.get("data"));
                mAdapter.notifyDataSetChanged();

            }
        });
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED //锁屏显示
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD //解锁
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON //保持屏幕不息屏
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_FULLSCREEN);//点亮屏幕
        setContentView(R.layout.activity_screen);
        registerLockerReceiver();
        //下面就是根据自己的跟你需求来写，跟写一个Activity一样的
        //拿到传过来的数据
        initView();

        initData();

    }




    private void initData() {
        urls.add("http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4");
        urls.add("http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4");
        urls.add("http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4");
        urls.add("http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4");
        urls.add("http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4");
        urls.add("http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4");
        urls.add("http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4");
        urls.add("http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4");
        urls.add("http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4");
        urls.add("http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4");

        mAdapter = new ScreenAdapter(this, urls);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        mRvScreen.setLayoutManager(linearLayoutManager);
        mRvScreen.setAdapter(mAdapter);


        mRvScreen.addOnScrollListener(new RecyclerView.OnScrollListener() {

            int firstVisibleItem, lastVisibleItem,firstCompletelyVisibleItem,lastCompletelyVisibleItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                firstCompletelyVisibleItem = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                lastCompletelyVisibleItem = linearLayoutManager.findLastCompletelyVisibleItemPosition();
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
                            mAdapter.notifyItemChanged(position);
                        }
                    }
                }
                if(lastVisibleItem % 5 == 0){
                    if(channel != null){
                        channel.send("android向flutter请求10条数据");
                        Log.d("ChannelLog","android向flutter请求10条数据");
                    }

                }
            }
        });
    }

    private void initView() {
        mRvScreen = findViewById(R.id.rv_screen);
        mTvTime = findViewById(R.id.tv_time);
        mTvDate = findViewById(R.id.tv_date);
        mIvBattery = findViewById(R.id.iv_battery);
        mIvBattery.setRotation(90);
        mIvPhone = findViewById(R.id.iv_phone);
        mIvLock = findViewById(R.id.iv_lock);
        mIvCamera = findViewById(R.id.iv_camera);
        mTvBatteryLevel = findViewById(R.id.tv_battery_level);
        mIvPermission = findViewById(R.id.iv_permission);

        mIvPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(Intent.ACTION_CALL_BUTTON);//跳转到拨号界面
                startActivity(intent);

            }
        });

        mIvCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivity(intent);

            }
        });

        mIvPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(ScreenActivity.this)
                        .setTitle("确定关闭锁屏保护")
                        .setMessage("关闭后将失去个性化内容推送服务")
                        .setPositiveButton("算了", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("去关闭", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    PermissionUtils.toPermissionSetting(ScreenActivity.this);
                                } catch (NoSuchFieldException e) {
                                    e.printStackTrace();
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                alertDialog.show();


            }
        });





        mIvLock.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                finish();
                return false;
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
        unregisterLockerReceiver();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("onPause:", "onPause");
        GSYVideoManager.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        if (GSYVideoManager.backFromWindowFull(this)) {
            return;
        }
//        super.onBackPressed();
    }


    private void updateTimeUI() {
        mTvTime.setText(DateUtil.getHourString(this, System.currentTimeMillis()));
        mTvDate.setText(monthFormat.format(calendar.getTime()) + " " + weekFormat.format(calendar.getTime()));
    }


    private void updateBatteryUI() {
        int level = PowerUtil.getLevel(this);
        mTvBatteryLevel.setText(level + "%");
        if(PowerUtil.isCharging(this)){
            if (level < 100 && mIvBattery.getDrawable() instanceof Animatable) {
                Animatable animatable = (Animatable) mIvBattery.getDrawable();
                if (PowerUtil.isCharging(this)) {
                    animatable.start();
                } else {
                    animatable.stop();
                }
            } else {
                mIvBattery.setImageResource(R.drawable.ic_baseline_battery_charging_full_36);
            }
        } else {
            if (level <= 20) {
                mIvBattery.setImageResource(R.mipmap.ic_battery_20_white_48dp);
            } else if (level <= 50) {
                mIvBattery.setImageResource(R.mipmap.ic_battery_50_white_48dp);
            } else if (level < 60) {
                mIvBattery.setImageResource(R.mipmap.ic_battery_60_white_48dp);
            } else if (level < 80) {
                mIvBattery.setImageResource(R.mipmap.ic_battery_80_white_48dp);
            } else if (level < 90) {
                mIvBattery.setImageResource(R.mipmap.ic_battery_90_white_48dp);
            } else if(level == 100) {
                mIvBattery.setImageResource(R.mipmap.ic_battery_full_white_48dp);
            }
        }

    }


    private class UIChangingReceiver extends BroadcastReceiver {

        public UIChangingReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (!TextUtils.isEmpty(action)) {
                onActionReceived(action);
            }
        }
    }





}
