package com.fragments;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.adapter.NetSpAdapter;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cexample.myplayer.R;
import com.domain.Mediaitem;
import com.google.gson.Gson;
import com.utils.CacheUtils;
import com.utils.Constans;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/1/16.
 */

public class NetShipinFragment extends Fragment {
    private ProgressBar net_video_progressBar;
    private TextView netShipinText;
    private RecyclerView net_video_recyclerview;
    private RequestQueue mQueue;
    private StringRequest stringRequest;
    private List<Mediaitem> movieInfoList;
    private LinearLayoutManager layoutManager;
    private NetSpAdapter netSpadapter;
    private SwipeRefreshLayout xialashuaxin;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shipin_net, container, false);
        net_video_progressBar = (ProgressBar) view.findViewById(R.id.net_video_progressBar);
        netShipinText = (TextView) view.findViewById(R.id.net_video_text);
        net_video_recyclerview = (RecyclerView) view.findViewById(R.id.net_video_recyclerview);
        xialashuaxin = (SwipeRefreshLayout) view.findViewById(R.id.xialashuaxin);
        xialashuaxin.setColorSchemeColors(Color.BLACK);//这个方法是设置指定颜色
//        xialashuaxin.setColorSchemeResources();//这个方法是从资源文件中设置颜色
        xialashuaxin.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshMovie();
            }
        });
        getDatafromNet();
        return view;
    }

    private void refreshMovie() {
        getDatafromNet();
        if (netSpadapter != null) {
            netSpadapter.notifyDataSetChanged();//通知适配器数据改变，更新一下
        }

        xialashuaxin.setRefreshing(false);//刷新事件结束
    }


    private void refreshRecyclerView() {
        if (movieInfoList != null && movieInfoList.size() > 0) {
            //有数据就设置适配器,把文本隐藏
            layoutManager = new LinearLayoutManager(getActivity());
            netSpadapter = new NetSpAdapter(movieInfoList);
            net_video_recyclerview.setAdapter(netSpadapter);
            net_video_recyclerview.setLayoutManager(layoutManager);
            netShipinText.setVisibility(View.GONE);
        } else {
            //没有数据隐藏进度条，并显示文本
            net_video_progressBar.setVisibility(View.GONE);
            netShipinText.setText("加载网络视频错误！请检查网络连接");

        }
        //Progressbar隐藏
        net_video_progressBar.setVisibility(View.GONE);
    }

    private void getDatafromNet() {
        mQueue = Volley.newRequestQueue(getActivity());
//        final Gson gson = new Gson();//用GSON解析成Java对象则不能序列化传递
        movieInfoList = new ArrayList<>();
/**
 * 请的结果里边可以直接进行UI更新操.0
 * 0.作
 */
        jiexishuju(CacheUtils.getString(getActivity(), Constans.NET_URL));
        refreshRecyclerView();
        stringRequest = new StringRequest(Constans.NET_URL,//第二个参数为null
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.d("sssssssssss", "onResponse: " + s);
                        CacheUtils.putString(getActivity(), Constans.NET_URL, s);
                        jiexishuju(s);
                        refreshRecyclerView();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        net_video_progressBar.setVisibility(View.GONE);
                        netShipinText.setText("加载网络视频错误！请检查网络连接");
                    }
                });

        mQueue.add(stringRequest);

    }

    private void jiexishuju(String s) {
        try {
            JSONObject jsobject = new JSONObject(s);
            JSONArray array = jsobject.optJSONArray("trailers");//用optJSONArray不会崩溃，只会返回null
            if (array != null && array.length() > 0) {
                for (int i = 0; i < array.length(); i++) {//用完了再加1
                    Mediaitem mv = new Mediaitem();
                    JSONObject jsonObjectItem = (JSONObject) array.get(i);
                    mv.setData(jsonObjectItem.optString("hightUrl"));
                    mv.setImageUrl(jsonObjectItem.optString("coverImg"));
                    mv.setShipinshuoming(jsonObjectItem.optString("summary"));
                    mv.setName(jsonObjectItem.optString("movieName"));
                    mv.setVideoLength(jsonObjectItem.optString("videoLength"));
                    movieInfoList.add(mv);
                }

            }
            /**
             * 用GSON解析成list，但是不能序列化传递，所以还是先用原始的解析方式
             */
//                            String str = array.toString();
//                            movieInfoList = gson.fromJson(str, new TypeToken<List<MovieInfo>>() {
//                            }.getType());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
