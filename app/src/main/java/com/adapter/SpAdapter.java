package com.adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
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

import com.cexample.myplayer.BofangshipinActivity;
import com.cexample.myplayer.MyApplication;
import com.cexample.myplayer.R;
import com.domain.Mediaitem;
import com.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by Administrator on 2017/1/27.
 */

public class SpAdapter extends RecyclerView.Adapter<SpAdapter.ViewHolder> {
    public SpAdapter(List<Mediaitem> mediaitems,Map<Long,Bitmap> map) {
        mshipinItems = mediaitems;
        mymap=map;
    }

    private Map<Long, Bitmap> mymap;
    private List<Mediaitem> mshipinItems;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shipin_fragment, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.shipinbofang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Mediaitem mediaItem = mshipinItems.get(holder.getAdapterPosition());
                Toast.makeText(MyApplication.getContext(), "播放" + mediaItem.getName(), Toast.LENGTH_SHORT).show();
                //传递列表数据，对象需要序列化
                Intent intent = new Intent(MyApplication.getContext(), BofangshipinActivity.class);
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

                        }

                    }
                });
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Mediaitem mediaItem = mshipinItems.get(position);
        holder.shipin_name.setText(mediaItem.getName());
        holder.shipin_daxiao.setText(android.text.format.Formatter.formatFileSize(MyApplication.getContext()
                , mediaItem.getSize()));
        holder.shipin_time.setText(new Utils().formatterTime((int) mediaItem.getDuration()));
        holder.shipin_icon.setImageBitmap(mymap.get(mediaItem.getId()));
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


}
