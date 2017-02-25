package com.cexample.myplayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.domain.Mediaitem;
import com.domain.YinyueItem;
import com.utils.Utils;
import java.util.ArrayList;
import java.util.Date;

public class BofangshipinActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int PROGRESS = 1;//视频进度的更新
    private static final int YINCANG_KONGZHI_MIANBAN = 2;
    private static final int SHOW_SPEED = 3;
    private VideoView videoView;
    //手势识别器
    private GestureDetector gesture;
    private Uri uri;
    private ImageView zanting;
    private ImageView fanhui;
    private ImageView quanping;
    private ImageView dianliang;
    private ImageView shang;
    private ImageView xia;
    private ImageView qiehuan;
    private RelativeLayout bofangjiemian;
    private TextView sysytem_time;
    private TextView net_speed;
    private TextView over_time;
    private TextView shipin_name;
    private TextView net_loading_speed;
    private TextView ontime;

    private ImageView shengyin;
    private LinearLayout shipinBuffer;
    private LinearLayout shipin_loading;
    private AudioManager audioManager;
    private int currentvoice;
    private int ercitvoice;
    private int maxvoice;//0~15
    private SeekBar bofangjindu;
    private SeekBar yinliang;
    int currentPosition;
    private int shipinposition;//要播的列表的位置
    private int yinyueposition;//要播的列表的位置
    YinyueItem yinyueItem;
    Mediaitem mediaItem;
    private ArrayList<Mediaitem> mediaItems;//传进来的视频列表
    private ArrayList<YinyueItem> myinyueItems;//传进来的视频列表
    private DianliangReceiver recevier;
    private int screenWidth;
    private int screenHight;
    private Utils utils = new Utils();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SHOW_SPEED:
                    String net_speedstr = utils.getNetSpeed(getApplicationContext());
                    net_speed.setText("缓冲中..." + net_speedstr);
                    net_loading_speed.setText("加载中..." + net_speedstr);
                    handler.sendEmptyMessageDelayed(SHOW_SPEED, 2000);
                    break;
                case PROGRESS:
//                    1.得到当前视频的播放进度
                    currentPosition = videoView.getCurrentPosition();
//                    2.Seekbar.setProgress(当前进度);
                    bofangjindu.setProgress(currentPosition);
                    //设置系统时间
                    sysytem_time.setText(String.format("%tR", new Date()));
                    if (isNetUri) {//只有网络视频资源才显示缓冲
                        int buffer = videoView.getBufferPercentage();//得到百分之多少的视频了0到100之间的数字
                        int secondaryProgress = buffer * bofangjindu.getMax() / 100;//占总进度的百分之多少
                        bofangjindu.setSecondaryProgress(secondaryProgress);
                    } else {
                        //本地视频没有缓冲
                        bofangjindu.setSecondaryProgress(0);
                    }
//                    3.每秒更新一次
                    ontime.setText(utils.formatterTime(currentPosition));
                    sendEmptyMessageDelayed(PROGRESS, 1000);
                    break;
                case YINCANG_KONGZHI_MIANBAN:
                    bofangjiemian.setVisibility(View.GONE);
                    break;
            }
        }
    };
    private boolean isNetUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        yincangTitleBar();
        setContentView(R.layout.activity_bofangshipin);
        findViews();
        initData();
        getData();
        setData();
        setListner();

    }

    private void findViews() {
        videoView = (VideoView) findViewById(R.id.videoView);
        bofangjiemian = (RelativeLayout) findViewById(R.id.bofangjiemian);
        zanting = (ImageView) findViewById(R.id.zanting);
        over_time = (TextView) findViewById(R.id.over_time);
        shipin_name = (TextView) findViewById(R.id.shipin_name);
        shengyin = (ImageView) findViewById(R.id.shengyin);
        fanhui = (ImageView) findViewById(R.id.fanhui);
        quanping = (ImageView) findViewById(R.id.quanping);
        shang = (ImageView) findViewById(R.id.shang);
        net_loading_speed = (TextView) findViewById(R.id.net_loading_speed);
        xia = (ImageView) findViewById(R.id.xia);
        bofangjindu = (SeekBar) findViewById(R.id.bofangjindu);
        qiehuan = (ImageView) findViewById(R.id.qiehuan);
        ontime = (TextView) findViewById(R.id.ontime);
        net_speed = (TextView) findViewById(R.id.net_speed);
        yinliang = (SeekBar) findViewById(R.id.yinliang);
        sysytem_time = (TextView) findViewById(R.id.sysytem_time);
        dianliang = (ImageView) findViewById(R.id.dianliang);
        shipinBuffer = (LinearLayout) findViewById(R.id.shipinBuffer);
        shipin_loading = (LinearLayout) findViewById(R.id.shipin_loading);
        shang.setOnClickListener(this);
        xia.setOnClickListener(this);
        shengyin.setOnClickListener(this);
        zanting.setOnClickListener(this);
        quanping.setOnClickListener(this);
        qiehuan.setOnClickListener(this);
        fanhui.setOnClickListener(this);

    }

    private void getData() {
        //getIntent().getData()方法是得到的是Uri对象
        uri = getIntent().getData();
        myinyueItems = getIntent().getParcelableArrayListExtra("myinyueItems");
        mediaItems = getIntent().getParcelableArrayListExtra("mediaItems");
        shipinposition = getIntent().getIntExtra("shipinposition", 0);
        yinyueposition = getIntent().getIntExtra("yinyueposition", 0);

    }

    private void setData() {
        if (mediaItems != null && mediaItems.size() > 0) {
            mediaItem = mediaItems.get(shipinposition);
            shipin_name.setText(mediaItem.getName());
            isNetUri = utils.isNetUri(mediaItem.getData());
            videoView.setVideoPath(mediaItem.getData());
        } else if (uri != null) {
            shipin_name.setText(uri.toString());
            isNetUri = utils.isNetUri(uri.toString());
            videoView.setVideoURI(uri);
        }
        if (myinyueItems != null) {
            yinyueItem = myinyueItems.get(yinyueposition);

            shipin_name.setText(yinyueItem.getName());
            videoView.setVideoPath(yinyueItem.getData());
        }
        videoView.start();
    }


    private void initData() {
        //注册电量广播
        recevier = new DianliangReceiver();
        //接收过滤器
        IntentFilter intentFilter = new IntentFilter();
        //过滤Intent....什么行为
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        //注册广播接收器
        registerReceiver(recevier, intentFilter);
//        手势识别器
        gesture = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public void onLongPress(MotionEvent e) {
                super.onLongPress(e);
                Toast.makeText(getApplicationContext(), "长按", Toast.LENGTH_SHORT).show();
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return super.onFling(e1, e2, velocityX, velocityY);
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {

                Toast.makeText(getApplicationContext(), "单机", Toast.LENGTH_SHORT).show();
                if (bofangjiemian.getVisibility() == View.VISIBLE) {
                    bofangjiemian.setVisibility(View.INVISIBLE);
                } else {
                    bofangjiemian.setVisibility(View.VISIBLE);
                    handler.removeMessages(YINCANG_KONGZHI_MIANBAN);
                }
                yincangNavigationBar();
                jiemianZidongYincang();
                return super.onSingleTapConfirmed(e);
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                Toast.makeText(getApplicationContext(), "双击", Toast.LENGTH_SHORT).show();
                return super.onDoubleTap(e);
            }
        });
        //得到系统音量管理员
        audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);//系统音量服务
        //得到系统最大音量
        maxvoice = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        //得到当前音量
        currentvoice = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        xianshiyinliang();

        /**
         * 得到屏幕宽和高的新方式
         * */
        DisplayMetrics outMetrics = new DisplayMetrics();//显示度量类
        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        screenWidth = outMetrics.widthPixels;
        screenHight = outMetrics.heightPixels;
        handler.sendEmptyMessage(SHOW_SPEED);

    }

    public boolean onTouchEvent(MotionEvent event) {
        gesture.onTouchEvent(event);//把事件传递给手势识别器
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = event.getY();
                mVol = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                pingmugao = Math.min(screenHight, screenWidth);
                handler.removeMessages(YINCANG_KONGZHI_MIANBAN);
                break;
            case MotionEvent.ACTION_MOVE:
                endY = event.getY();
                distanceY = endY - startY;
                gaibianVol = (int) (2 * distanceY * maxvoice / pingmugao);
                if (event.getX() > screenWidth * 0.5) {
                    if (Math.abs(distanceY) > 50) {
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mVol - gaibianVol, 0);
                        yinliang.setProgress(mVol - gaibianVol);
                        if (mVol - gaibianVol <= 0) {
                            shengyin.setImageResource(R.drawable.jingyin);
                        } else {
                            shengyin.setImageResource(R.drawable.shengyin);
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:

                break;

        }
        jiemianZidongYincang();
        return super.onTouchEvent(event);
    }
    public void setBattery(int dian) {
        if (dian <= 10) {
            dianliang.setImageResource(R.drawable.dianliang0);
        } else if (dian < 10 && dian <= 20) {
            dianliang.setImageResource(R.drawable.dianliang10);
        } else if (dian < 20 && dian <= 40) {
            dianliang.setImageResource(R.drawable.dianliang20);
        } else if (dian < 40 && dian <= 60) {
            dianliang.setImageResource(R.drawable.dianliang40);
        } else if (dian < 60 && dian <= 80) {
            dianliang.setImageResource(R.drawable.dianliang60);
        } else if (dian < 80 && dian <= 100) {
            dianliang.setImageResource(R.drawable.dianliang80);
        } else {
            dianliang.setImageResource(R.drawable.dianliang100);
        }

    }

    class DianliangReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //得到系统电量level,键名“level”
            int dianliang = intent.getIntExtra("level", 50);
            setBattery(dianliang);
        }
    }

    private void setListner() {
        //准备好的监听
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                videoView.start();
                gengxinjindu();
                shipin_loading.setVisibility(View.GONE);
                bofangjiemian.setVisibility(View.INVISIBLE);
            }
        });


        //播放出错的监听
        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Toast.makeText(getApplicationContext(), "播放 出错", Toast.LENGTH_SHORT).show();
/**
 * 1.播放视频格式不支持，转到万能播放器
 * 2.播放网络视频的时候，中间有空白
 * 3.播放网络 视频的时候，间歇性断网，可以试着联网三次
 *
 */

                return true;
            }
        });
        //播放完成的监听
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Toast.makeText(getApplicationContext(), "播放完成", Toast.LENGTH_SHORT).show();
                zanting.setImageResource(R.drawable.bofang);
            }
        });
        //设置SEEKbar状态变化的监听
        bofangjindu.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override//进度变化时回调，会自动回调，自动会掉时fromUser为false，用户引起则为true
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                所以判断是自动还是用户引起的
                if (fromUser) {
                    videoView.seekTo(progress);
                    jiemianZidongYincang();
                }
            }

            @Override//刚触碰时回调
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override//离开时回调
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                @Override
                public boolean onInfo(MediaPlayer mp, int what, int extra) {
                    switch (what) {
                        case MediaPlayer.MEDIA_INFO_BUFFERING_START://开始正在缓冲，拖动卡
                            Toast.makeText(getApplicationContext(), "卡了", Toast.LENGTH_SHORT).show();
                            shipinBuffer.setVisibility(View.VISIBLE);
                            break;
                        case MediaPlayer.MEDIA_INFO_BUFFERING_END://结束正在缓冲，拖动卡结束
                            Toast.makeText(getApplicationContext(), "不卡了", Toast.LENGTH_SHORT).show();
                            shipinBuffer.setVisibility(View.GONE);
                            break;
                    }
                    return true;//已经处理就返回true
                }
            });
        }
        yinliang.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    yinliang.setProgress(progress);
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);//如果是就吊起系统的，如果是0就不掉
                    currentvoice = progress;
                    if (progress == 0) {
                        shengyin.setImageResource(R.drawable.jingyin);
                    } else {
                        shengyin.setImageResource(R.drawable.shengyin);
                    }
                    jiemianZidongYincang();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


    private void xianshiyinliang() {
        yinliang.setMax(maxvoice);
        yinliang.setProgress(currentvoice);
    }


    private void gengxinjindu() {
        //1.视频的总时长
        int duration = videoView.getDuration();
        bofangjindu.setMax(duration);
//                2.发消息去处理
        handler.sendEmptyMessage(PROGRESS);

        over_time.setText(utils.formatterTime(duration));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.shengyin:
                if (currentvoice != 0) {
                    Toast.makeText(getApplicationContext(), "静音", Toast.LENGTH_SHORT).show();
                    shengyin.setImageResource(R.drawable.jingyin);
                    yinliang.setProgress(0);
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
                    ercitvoice = currentvoice;
                    currentvoice = 0;
                } else {
                    shengyin.setImageResource(R.drawable.shengyin);
                    if (ercitvoice != 0) {
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, ercitvoice, 0);
                        yinliang.setProgress(ercitvoice);
                    } else {
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 5, 0);
                        yinliang.setProgress(5);
                    }

                    currentvoice = ercitvoice;
                }

                break;
            case R.id.zanting:
                if (videoView.isPlaying()) {
                    videoView.pause();
                    zanting.setImageResource(R.drawable.bofang);
                } else {
                    videoView.start();
                    zanting.setImageResource(R.drawable.zanting);
                    yincangNavigationBar();
                }
                break;
            case R.id.fanhui:
                finish();
                break;
            case R.id.shang:
                playUpvideo();
                break;
            case R.id.xia:
                playNextViedeo();
                break;
            case R.id.qiehuan:
                Toast.makeText(getApplicationContext(), "切换", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(uri, "video/*");
                getApplicationContext().startActivity(intent);
                break;
            case R.id.quanping:
                Toast.makeText(getApplicationContext(), "全屏", Toast.LENGTH_SHORT).show();
                bofangjiemian.setVisibility(View.INVISIBLE);
                yincangNavigationBar();
                break;
        }

        jiemianZidongYincang();
    }

    private void playUpvideo() {
        shipin_loading.setVisibility(View.GONE);
        if (mediaItems != null && mediaItems.size() > 0) {
            if (shipinposition == 0) {
                Toast.makeText(getApplicationContext(), "已是第一个", Toast.LENGTH_SHORT).show();
            } else {
                shipinposition--;
                mediaItem = mediaItems.get(shipinposition);
                shipin_name.setText(mediaItem.getName());
                isNetUri = utils.isNetUri(mediaItem.getData());
                videoView.setVideoPath(mediaItem.getData());
            }

        } else if (uri != null) {

        }
        videoView.start();
    }

    private void playNextViedeo() {
        shipin_loading.setVisibility(View.GONE);
        if (mediaItems != null && mediaItems.size() > 0) {
            if (mediaItems.size() == shipinposition + 1) {
                Toast.makeText(getApplicationContext(), "已是最后一个", Toast.LENGTH_SHORT).show();
            } else {
                shipinposition++;//先加成（shipinposition+1）,在使用加1后的整体
                mediaItem = mediaItems.get(shipinposition);
                shipin_name.setText(mediaItem.getName());
                isNetUri = utils.isNetUri(mediaItem.getData());
                videoView.setVideoPath(mediaItem.getData());
            }


        } else if (uri != null) {
            //上下不能点击
        }
        videoView.start();
    }

    private float startY;
    private float pingmugao;
    private int mVol;
    private int gaibianVol;
    private float endY;
    private float distanceY;



    @Override//监听物理键关联
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            bofangjiemian.setVisibility(View.VISIBLE);
            if (currentvoice > 0) {
                currentvoice--;
            }
            if (currentvoice == 0) {
                shengyin.setImageResource(R.drawable.jingyin);
            }
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentvoice, 0);
            yinliang.setProgress(currentvoice);
            jiemianZidongYincang();
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            bofangjiemian.setVisibility(View.VISIBLE);
            if (currentvoice < 15) {
                currentvoice++;
                shengyin.setImageResource(R.drawable.shengyin);
            }
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentvoice, 0);
            yinliang.setProgress(currentvoice);
            jiemianZidongYincang();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void yincangNavigationBar() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    private void jiemianZidongYincang() {
        handler.removeMessages(YINCANG_KONGZHI_MIANBAN);
        handler.sendEmptyMessageDelayed(YINCANG_KONGZHI_MIANBAN, 4500);

    }

    private void yincangTitleBar() {
        yincangNavigationBar();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        if (recevier != null) {
            unregisterReceiver(recevier);

        }
        videoView.resume();
        super.onDestroy();
    }

    private static final String TAG = "BofangshipinActivity";

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    protected void onStop() {

        super.onStop();
        currentPosition = videoView.getCurrentPosition();
        Log.d(TAG, "onStop: ");


    }

    @Override
    protected void onRestart() {

        super.onRestart();
        videoView.seekTo(currentPosition);
        Log.d(TAG, "onRestart: ");
    }

    @Override
    protected void onPause() {
        currentPosition = videoView.getCurrentPosition();
        Log.d(TAG, "onPause: ");
        videoView.pause();
        super.onPause();
    }
}
