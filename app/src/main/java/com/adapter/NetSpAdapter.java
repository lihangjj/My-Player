package com.adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cexample.myplayer.BofangshipinActivity;
import com.cexample.myplayer.MyApplication;
import com.cexample.myplayer.R;
import com.domain.Mediaitem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2017/1/27.
 */

public class NetSpAdapter extends RecyclerView.Adapter<NetSpAdapter.ViewHolder> {
    public NetSpAdapter(List<Mediaitem> mediaitems) {
        mshipinItems = mediaitems;
    }

    private List<Mediaitem> mshipinItems;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_net_shipin_fragment, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.shipinbofang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Mediaitem movieInfo = mshipinItems.get(holder.getAdapterPosition());
                Toast.makeText(MyApplication.getContext(), "播放" + movieInfo.getName(), Toast.LENGTH_SHORT).show();
                //传递列表数据，对象需要序列化
                Intent intent = new Intent(MyApplication.getContext(), BofangshipinActivity.class);
                intent.setData(Uri.parse(movieInfo.getData()));
                intent.putExtra("shipinposition", holder.getAdapterPosition());
                intent.putParcelableArrayListExtra("mediaItems", (ArrayList<? extends Parcelable>) mshipinItems);
                MyApplication.getContext().startActivity(intent);
            }
        });

        holder.gengduo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow();
            }

            private void showPopupWindow() {
                View contentView = LayoutInflater.from(MyApplication.getContext()).inflate(R.layout.popupwindow, null);
                final String[] data = {"收藏", "添加到...", "接下来播放", "获取视频封面", "设为喜欢", "视频随心载", "视频信息", "隐藏", "删除"};
                ListView listView = (ListView) contentView.findViewById(R.id.listview);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MyApplication.getContext(), android.R.layout.simple_list_item_1, data);
                listView.setAdapter(adapter);
                final PopupWindow mpopupwindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
                mpopupwindow.showAsDropDown(holder.shipin_name);
                mpopupwindow.setTouchable(true);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String str = (String) ((TextView) view).getText();
                        switch (position) {
                            case 8:
                                Toast.makeText(view.getContext(), str + "成功", Toast.LENGTH_SHORT).show();
                                mshipinItems.remove(holder.getAdapterPosition());
                                mpopupwindow.dismiss();
                                notifyDataSetChanged();
                                break;
                            default:


                        }
                        mpopupwindow.dismiss();
                    }
                });
            }
        });
        return holder;
    }

    /**
     * 1.第一步创建RequestQueue对象
     */
    private RequestQueue mQueue = Volley.newRequestQueue(MyApplication.getContext());

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Mediaitem movieInfo = mshipinItems.get(position);
//        RequestQueue mQueue= Volley.newRequestQueue(MyApplication.getContext());//放在这里边会导致内存溢出，因为不停创建RequestQueue
        /**
         * 使用ImageLoader加载图片
         */
        ImageLoaderByImageLoader(holder, movieInfo);

        /**
         * 使用glide加载图片，实践证明没有VOLLEY快
         */
//        Glide.with(MyApplication.getContext()).load(movieInfo.getImageUrl())
//                .diskCacheStrategy(DiskCacheStrategy.ALL)//全尺寸缓存
//                .placeholder(R.drawable.xiaoshipin)//加载过程中的图片
//                .error(null)//加载出错的图片
//                .into(holder.shipin_icon);
        /**
         * 使用picasso加载图片,一般用glide
         */
//        Picasso.with(MyApplication.getContext()).load(movieInfo.getImageUrl())
////                .diskCacheStrategy(DiskCacheStrategy.ALL)//全尺寸缓存
//        .placeholder(R.drawable.xiaoshipin)//加载过程中的图片
////                .error(null)//加载出错的图片
//                .into(holder.shipin_icon);
        holder.shipin_name.setText(movieInfo.getName());
        holder.shipin_time.setText(movieInfo.getShipinshuoming());
        holder.shipin_daxiao.setText(movieInfo.getVideoLength() + "分钟");
    }

    private void ImageLoaderByImageLoader(ViewHolder holder, Mediaitem movieInfo) {
        /**
         * 2.创建ImageLoader对象,两个参数，一为请求队列对象，二为实现ImageLoader.ImageCache接口的类，
         */
        ImageLoader imageLoader = new ImageLoader(mQueue, new BitmapCache());
        /**
         * 3.创建ImageLoader.ImageListener，一参为显示控件，2位加载过程中显示的图片，三为失败后显示的图片
         */
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(holder.shipin_icon, 0, 0);
        /**
         * 4.最后imageLoader.get(movieInfo.getImageurl(), listener);
         */
        imageLoader.get(movieInfo.getImageUrl(), listener);
    }

    @Override
    public int getItemCount() {
        return mshipinItems.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View shipinbofang;
        ImageView gengduo;
        ImageView shipin_icon;
        TextView shipin_name;
        TextView shipin_time;
        TextView shipin_daxiao;

        public ViewHolder(View view) {
            super(view);
            gengduo = (ImageView) view.findViewById(R.id.gengduo);
            shipinbofang = view.findViewById(R.id.shipinbofang);
            shipin_icon = (ImageView) view.findViewById(R.id.shipin_icon);
            shipin_name = (TextView) view.findViewById(R.id.shipin_name);
            shipin_time = (TextView) view.findViewById(R.id.shipin_time);
            shipin_daxiao = (TextView) view.findViewById(R.id.shipin_daxiao);

        }
    }

    public class BitmapCache implements ImageLoader.ImageCache {

        private LruCache<String, Bitmap> mCache;

        public BitmapCache() {
            int maxSize = 15 * 1024 * 1024;
            mCache = new LruCache<String, Bitmap>(maxSize) {
                @Override
                protected int sizeOf(String key, Bitmap bitmap) {
                    return bitmap.getRowBytes() * bitmap.getHeight();
                }
            };
        }

        @Override
        public Bitmap getBitmap(String url) {
            return mCache.get(url);
        }

        @Override
        public void putBitmap(String url, Bitmap bitmap) {
            mCache.put(url, bitmap);
        }

    }


}
