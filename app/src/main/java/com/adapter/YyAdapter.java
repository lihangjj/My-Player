package com.adapter;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
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
import com.domain.YinyueItem;
import com.utils.Utils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2017/1/27.
 */

public class YyAdapter extends RecyclerView.Adapter<YyAdapter.ViewHolder> {
    private List<YinyueItem> myinyueItems;
   private static MediaPlayer mediaPlayer=new MediaPlayer();

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_yinyue_fragment, parent, false);
        final ViewHolder holder=new ViewHolder(view);
        holder.yinyuebofang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                YinyueItem yinyueItem=myinyueItems.get(holder.getAdapterPosition());
                Toast.makeText(v.getContext(), "播放" + yinyueItem.getName(), Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(MyApplication.getContext(),BofangshipinActivity.class);
                intent.putParcelableArrayListExtra("myinyueItems", (ArrayList<? extends Parcelable>) myinyueItems);
                intent.putExtra("yinyueposition",holder.getAdapterPosition());
//                Intent intent=new Intent(Intent.ACTION_VIEW);
//                intent.setDataAndType(Uri.parse(yinyueItem.getData()),"audio/*");
                MyApplication.getContext().startActivity(intent);
//                mediaPlayer.reset();
//                try {
//                    mediaPlayer.setDataSource(yinyueItem.getData());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                try {
//                    mediaPlayer.prepare();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                mediaPlayer.start();
            }
        });
        holder.gengduo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow();

            }

            private void showPopupWindow() {
                View contentView=LayoutInflater.from(MyApplication.getContext()).inflate(R.layout.popupwindow,null);
                final String[] data={"收藏","添加到...","接下来播放","获取封面歌词","设为铃声","音乐随心载","歌曲信息","隐藏","删除"};
                ListView listView= (ListView) contentView.findViewById(R.id.listview);
                ArrayAdapter<String> adapter=new ArrayAdapter<String>(MyApplication.getContext(),android.R.layout.simple_list_item_1,data);
                listView.setAdapter(adapter);
                PopupWindow mpopupwindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT,true);
                mpopupwindow.showAsDropDown(holder.yinyue_name);
                mpopupwindow.setTouchable(true);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String str=(String)((TextView)view).getText();
                        Toast.makeText(view.getContext(), "点击了"+str, Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });
        return holder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
         YinyueItem yinyueItem = myinyueItems.get(position);
        holder.yinyue_name.setText(yinyueItem.getName());
        holder.yinyuedaxiao.setText(android.text.format.Formatter.formatFileSize(MyApplication.getContext()
                , yinyueItem.getSize()));
        holder.yinyue_time.setText(new Utils().formatterTime((int) yinyueItem.getDuration()));
        }


    @Override
    public int getItemCount() {
        return myinyueItems.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View yinyuebofang;
        ImageView gengduo;
        ImageView yinyue_icon;
        TextView yinyue_name;
        TextView yinyue_time;
        TextView yinyuedaxiao;

        public ViewHolder(View view) {
            super(view);
            gengduo= (ImageView) view.findViewById(R.id.gengduo);
            yinyuebofang = view.findViewById(R.id.yinyuebofang);
            yinyue_icon = (ImageView) view.findViewById(R.id.yinyue_icon);
            yinyue_name = (TextView) view.findViewById(R.id.yinyue_name);
            yinyue_time = (TextView) view.findViewById(R.id.yinyue_time);
            yinyuedaxiao = (TextView) view.findViewById(R.id.yinyuedaxiao);

        }
    }

    public YyAdapter(List<YinyueItem> YinyueItems) {
        myinyueItems = YinyueItems;
    }
}
