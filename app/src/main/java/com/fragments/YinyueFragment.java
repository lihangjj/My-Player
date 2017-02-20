package com.fragments;

import android.Manifest;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.adapter.YyAdapter;
import com.cexample.myplayer.R;
import com.domain.YinyueItem;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/1/16.
 */

public class YinyueFragment extends Fragment {
    private static final String TAG = "YinyueFragment";
    private RecyclerView yyRecyclerview;
    private TextView noyy;
    private ProgressBar yyloading;
    private ArrayList<YinyueItem> yinyueItems;//装数据集合
    private YyAdapter yinyueAdapter;
    private LinearLayoutManager layoutManager;
    public static MediaPlayer mediaPlayer = new MediaPlayer();
    private TextView jiazaiyinyue;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_yinyue, container, false);
        yyRecyclerview = (RecyclerView) view.findViewById(R.id.yylistview);
        noyy = (TextView) view.findViewById(R.id.noyy);
        yyloading = (ProgressBar) view.findViewById(R.id.yyloading);
        jiazaiyinyue = (TextView) view.findViewById(R.id.jiazaiyinyue);
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            getDatafromlocal();
        }

        return view;
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (yinyueItems != null && yinyueItems.size() > 0) {
                //有数据就设置适配器,把文本隐藏
                layoutManager = new LinearLayoutManager(getActivity());
                yinyueAdapter = new YyAdapter(yinyueItems);
                yyRecyclerview.setAdapter(yinyueAdapter);
                yyRecyclerview.setLayoutManager(layoutManager);
                noyy.setVisibility(View.GONE);
                jiazaiyinyue.setVisibility(View.GONE);

            } else {
                //没有数据隐藏进度条，并显示文本
                noyy.setVisibility(View.VISIBLE);
            }
            //Progressbar隐藏
            yyloading.setVisibility(View.GONE);
        }
    };

    //1，遍历后缀名获取（太慢），2.从内容提供者中获取3.如果是6.0需要动态权限
    private void getDatafromlocal() {
        yinyueItems = new ArrayList<>();
        new Thread() {
            public void run() {
                super.run();
                ContentResolver contentResolver = getActivity().getContentResolver();
                String[] Prjs = {
                        MediaStore.Audio.Media.DURATION,
                        MediaStore.Audio.Media.SIZE,
                        MediaStore.Audio.Media.TITLE,
                        MediaStore.Audio.Media.DATA,
                };
                Cursor cursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, Prjs, null, null, null);
                if (cursor.moveToFirst()) {
                    do {
                        YinyueItem yy = new YinyueItem();
                        yy.setData(cursor.getString(3));
                        yy.setName(cursor.getString(2));
                        yy.setSize(cursor.getLong(1));
                        yy.setDuration(cursor.getLong(0));
                        yinyueItems.add(yy);
                        handler.sendEmptyMessage(119);
                    } while (cursor.moveToNext());
                }
//                MusicUtils.getMusicFile(yinyueItems, Environment.getExternalStorageDirectory());
                //发消息
            }
        }.start();
    }

    public static class MusicUtils {

        /**
         * 获取指定路径中的视频文件
         *
         * @param list 装扫描出来的视频文件实体类
         * @param file 指定的文件
         */
        public static void getMusicFile(final List<YinyueItem> list, File file) {// 获得音乐文件
            file.listFiles(new FileFilter() {
                @Override
                public boolean accept(File file) {
                    // sdCard找到音乐名称
                    String name = file.getName();
                    int i = name.indexOf('.');
                    if (i != -1) {
                        name = name.substring(i);//获取文件后缀名
                        if (name.equalsIgnoreCase(".mp3")  //忽略大小写
                                || name.equalsIgnoreCase(".3gp")
                                || name.equalsIgnoreCase(".xvid")) {
                            YinyueItem vi = new YinyueItem();
                            vi.setName(file.getName());//文件名
                            vi.setData(file.getAbsolutePath()); //文件路径
                            vi.setSize(file.length());
                            list.add(vi);
                            return true;
                        }
                    } else if (file.isDirectory()) {
                        getMusicFile(list, file);
                    }
                    return false;
                }
            });
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getDatafromlocal();
                } else {
                    Toast.makeText(getActivity(), "拒绝权限则失败", Toast.LENGTH_LONG).show();
                }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }
}
