package com.dragon.locker_screen;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;

import io.flutter.Log;
import io.flutter.embedding.android.FlutterActivity;

public class MainActivity extends FlutterActivity {
    private Context mContext;
    static String TAG = "TAG";

    BroadcastReceiver mMasterResetReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            try {
                Log.e("Output:", "接收到消息");
                String action = intent.getAction();
                disableSystemLockScreen(context);
                Log.e(TAG, "Intent.ACTION_SCREEN_OFF");
                Intent lockscreenIntent = new Intent();
                lockscreenIntent.setAction("com.android.lockscreen");
                lockscreenIntent.setPackage("com.dragon.locker_screen");
                lockscreenIntent.putExtra("INTENT_ACTION", action);
                lockscreenIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                lockscreenIntent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);

                mContext.startActivity(lockscreenIntent);

//				startScreenActivity();


                //finish();
            } catch (Exception e) {
                Log.e("Output:", e.toString());
            }
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);


        registerReceiver(mMasterResetReceiver, filter);

        ActivityCompat.checkSelfPermission(this,Manifest.permission.INTERNET);
        ActivityCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE);
        ActivityCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mMasterResetReceiver);
    }


    @SuppressLint("MissingPermission")
    public static void disableSystemLockScreen(Context context) {
        // 下面代码会出现某些系统home键启动后失效的问题
        try {
            KeyguardManager keyGuardService = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
            KeyguardManager.KeyguardLock keyGuardLock = keyGuardService.newKeyguardLock("");
            keyGuardLock.disableKeyguard();
        } catch (Exception e) {
            Log.e(TAG, "disableSystemLockScreen exception, cause: " + e.getCause()
                    + ", message: " + e.getMessage());
        }
    }


}
