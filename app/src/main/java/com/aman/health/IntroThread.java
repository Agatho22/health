package com.aman.health;

import android.os.Handler;
import android.os.Message;

public class IntroThread extends Thread {

    private Handler handler;

    public IntroThread(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void run() {
        super.run();
        Message msg = new Message();
        try {
            Thread.sleep(1000);
            msg.what = 1;
            handler.sendEmptyMessage(msg.what);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}