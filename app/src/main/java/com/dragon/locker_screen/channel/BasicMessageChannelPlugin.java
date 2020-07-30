package com.dragon.locker_screen.channel;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

import io.flutter.Log;
import io.flutter.embedding.android.FlutterView;
import io.flutter.plugin.common.BasicMessageChannel;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.StandardMessageCodec;
import io.flutter.plugin.common.StringCodec;

/**
 * Created by Maker on 2020/7/30.
 * Description:
 */
public class BasicMessageChannelPlugin implements BasicMessageChannel.MessageHandler<Object> {
    private Activity activity;
    private static BasicMessageChannel<Object> messageChannel;

    static BasicMessageChannelPlugin registerWith(FlutterView flutterView) {
        return new BasicMessageChannelPlugin(flutterView);
    }

    private BasicMessageChannelPlugin(FlutterView flutterView) {
        this.activity = (Activity) flutterView.getContext();
        this.messageChannel = new BasicMessageChannel<Object>((BinaryMessenger) flutterView, "BasicMessageChannelPlugin", StandardMessageCodec.INSTANCE);
        messageChannel.setMessageHandler(this);
    }

    @Override
    public void onMessage(@Nullable Object message, @NonNull BasicMessageChannel.Reply<Object> reply) {
        reply.reply(message);
        Map<Object,Object> data = (Map<Object, Object>) message;
        Log.d("android",data.get("data").toString());

    }

    public static void send(String str, BasicMessageChannel.Reply<Object> reply) {
        messageChannel.send(new HashMap<>(), reply);
    }
}
