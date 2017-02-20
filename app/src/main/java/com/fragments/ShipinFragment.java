package com.fragments;

import android.Manifest;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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

import com.adapter.SpAdapter;
import com.cexample.myplayer.R;
import com.domain.Mediaitem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/16.
 */

public class ShipinFragment extends Fragment {
    private RecyclerView spRecyclerview;
    private TextView notv;
    private ProgressBar tvloading;
    public List<Mediaitem> mediaitems = null;//装视频数据集合
    private SpAdapter shipinAdapter;
    private LinearLayoutManager layoutManager;
    private TextView jiazaishipin;
    private Map<Long,Bitmap> map;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shipin, container, false);
        spRecyclerview = (RecyclerView) view.findViewById(R.id.recyclerview);
        notv = (TextView) view.findViewById(R.id.notv);
        tvloading = (ProgressBar) view.findViewById(R.id.tvloading);
        jiazaishipin = (TextView) view.findViewById(R.id.jiazaishipin);
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
            if (mediaitems != null && mediaitems.size() > 0) {
                //有数据就设置适配器,把文本隐藏
                layoutManager = new LinearLayoutManager(getActivity());
                shipinAdapter = new SpAdapter(mediaitems,map);
                spRecyclerview.setAdapter(shipinAdapter);
                spRecyclerview.setLayoutManager(layoutManager);
                notv.setVisibility(View.GONE);
                jiazaishipin.setVisibility(View.GONE);
            } else {
                //没有数据隐藏进度条，并显示文本
                notv.setVisibility(View.VISIBLE);
            }
            //Progressbar隐藏
            tvloading.setVisibility(View.GONE);
        }
    };

    //1，遍历后缀名获取（太慢），2.从内容提供者中获取3.如果是6.0需要动态权限
    private void getDatafromlocal() {
        mediaitems = new ArrayList<>();
        map =new HashMap<Long, Bitmap>();
        new Thread() {
            public void run() {
                ContentResolver contenResolver = getActivity().getContentResolver();
                String[] Prjs = {
                        MediaStore.Video.Media.TITLE,
                        MediaStore.Video.Media.SIZE,
                        MediaStore.Video.Media.DURATION,
                        MediaStore.Video.Media.DATA,
                        MediaStore.Video.Media._ID,
                };
                Cursor cusor = contenResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, Prjs, null, null, null);
                if (cusor.moveToFirst()) {
                    do {
                        Mediaitem sp = new Mediaitem();

                        Bitmap bitmap = MediaStore.Video.Thumbnails.getThumbnail(contenResolver, cusor.getLong(4), 1, new BitmapFactory.Options());
                        sp.setSize(cusor.getLong(1));
                        sp.setDuration(cusor.getLong(2));
                        sp.setName(cusor.getString(0));
                        sp.setData(cusor.getString(3));
                        sp.setId(cusor.getLong(4));
                        map.put(cusor.getLong(4),bitmap);
                        mediaitems.add(sp);
                        handler.sendEmptyMessage(10);
                    } while (cusor.moveToNext());
                    cusor.close();
                }
                /**
                 * 写在线程里边，因为线程是执行一小段时间，如果写在外边，就会导致数据只加载一点，
                 * handler就去执行，再回到线程加载剩余数据时，handler已经不发送消息，
                 * 因此发送消息handler应该写在线程里边，加载一点数据，就去更新一段UI
                 *
                 */
            }
        }.start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(getActivity(), "你必须同意权限才能使用本程序", Toast.LENGTH_SHORT).show();
                        }
                    }
                    getDatafromlocal();
                }
                break;
            default:
        }
    }

}
