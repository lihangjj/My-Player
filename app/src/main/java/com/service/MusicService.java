package com.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.cexample.myplayer.IMusicService;
import com.cexample.myplayer.R;
import com.cexample.myplayer.YybofangActivity;
import com.domain.YinyueItem;

import java.io.IOException;
import java.util.List;

/**
 * Created by Administrator on 2017/2/25.
 */

public class MusicService extends Service {
    public static final String BOFANGWAN_WANBI = "bofangwan_WANBI";
    private List<YinyueItem> myYinyueItems;
    private YinyueItem yinyueItem;
    private int position;
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private static final int SHUXUBOFANG = 0;
    private static final int LIEBIAOXUNHUAN = 1;
    private static final int SUIJIBOFANG = 2;
    private static final int DANQUXUNHUAN = 3;
    private int bofangstyle = SHUXUBOFANG;

    @Override
    public void onCreate() {
        super.onCreate();
        /**
         * 一旦创建就去加载音乐列表
         */

    }

    private void setListener() {
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                switch (bofangstyle) {
                    case DANQUXUNHUAN://单曲循环
                        try {
                            playMusic(position);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case LIEBIAOXUNHUAN://列表循环
                        if (position == myYinyueItems.size() - 1) {
                            position = 0;
                            try {
                                playMusic(position);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            position++;
                            try {
                                playMusic(position);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    case SHUXUBOFANG://顺序播放
                        if (position == myYinyueItems.size() - 1) {
                            Toast.makeText(getApplicationContext(), "已经最后一个", Toast.LENGTH_SHORT).show();

                        } else {
                            position++;
                            try {
                                playMusic(position);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    case SUIJIBOFANG://随机播放
                        position = (int) (Math.random() * myYinyueItems.size());
                        try {
                            playMusic(position);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                }

            }
        });
    }

    private Uri uri;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        myYinyueItems = intent.getParcelableArrayListExtra("myinyueItems");
        uri = intent.getData();
        return super.onStartCommand(intent, flags, startId);
    }

    private IMusicService.Stub stub = new IMusicService.Stub() {
        MusicService service = MusicService.this;

        @Override
        public void start() throws RemoteException {
            service.start();
        }

        @Override
        public void pause() throws RemoteException {
            service.pause();
        }

        @Override
        public void stop() throws RemoteException {
            service.stop();
        }

        @Override
        public int getCurrentPosition() throws RemoteException {
            return service.getCurrentPosition();
        }

        @Override
        public void playMusic(int position) throws RemoteException {
            try {
                service.playMusic(position);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public boolean isPlaying() throws RemoteException {
            return service.isPlaying();
        }

        @Override
        public void seekTo(int progress) throws RemoteException {
            service.seekTo(progress);
        }

        @Override
        public int setBofangStyle(int style) throws RemoteException {
            return service.setBofangStyle(style);
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        /**
         * 千万不要忘记返回stub！！！！
         */
        return stub;
    }

    /**
     * 根据位置播放音乐
     */


    private void playMusic(int position1) throws IOException {
        position = position1;
        mediaPlayer.reset();
        if (uri != null) {
            //设置context和uri
            mediaPlayer.setDataSource(getApplicationContext(), uri);
        } else if (myYinyueItems != null) {
            yinyueItem = myYinyueItems.get(position1);
            mediaPlayer.setDataSource(yinyueItem.getData());
        }
        mediaPlayer.prepare();
        setListener();
        mediaPlayer.start();
        qiantaitongzhi();

        /**
         * 通知Activity接收数据
         */
        Intent intent = new Intent(BOFANGWAN_WANBI);
        intent.putExtra("bofangPosition", position);
        intent.putExtra("shichang", mediaPlayer.getDuration());
        sendBroadcast(intent);

    }

    private void qiantaitongzhi() {
        Intent intent = new Intent(this, YybofangActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle(yinyueItem.getName())
                .setContentText(yinyueItem.getArtist())
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.lhok)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.yysmall))
                .setContentIntent(pi)
                .build();
        startForeground(1, notification);
    }

    /**
     * 开始播放音乐
     */
    private void start() {
        mediaPlayer.start();
    }

    /**
     * 暂停音乐
     */
    private void pause() {
        mediaPlayer.pause();
    }

    /**
     * 停止音乐
     */
    private void stop() {
        mediaPlayer.stop();
    }

    /**
     * 得到当前的播放进度
     */
    private int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    private void seekTo(int progress) {
        mediaPlayer.seekTo(progress);
    }

    private boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    private int setBofangStyle(int style) {
        return bofangstyle = style;
    }

}
