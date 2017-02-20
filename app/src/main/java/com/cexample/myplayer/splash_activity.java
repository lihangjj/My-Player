package com.cexample.myplayer;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class splash_activity extends AppCompatActivity {
    private boolean flg = false;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        }
        setContentView(R.layout.activity_splash_activity);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startMainActivity();
            }
        }, 2000);

    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        startMainActivity();
        return super.onTouchEvent(event);
    }

    private void startMainActivity() {
        if (flg == false) {
            flg = true;
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }


}
