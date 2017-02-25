package com.utils;


import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.TrafficStats;

/**
 * Created by Administrator on 2017/2/17.
 */

public class Utils {
    private long lastTotalRxBytes = 0;
    private long lastTimeStamp = 0;

    //判断是否是网络Uri
    public boolean isNetUri(String uri) {
        if (uri != null) {
            if (uri.toLowerCase().startsWith("http") | uri.toLowerCase().startsWith("rtsp") | uri.toLowerCase().startsWith("mms"))
                ;
            return true;
        }
        return false;
    }

    public static String formatterTime(int duration) {
        int time1 = duration / 1000;
        int fen = time1 / 60;
        int miao = time1 % 60;
        String strfen;
        String strmiao;
        if (fen < 10) {
            strfen = "0" + fen + ":";
        } else {
            strfen = fen + ":";
        }
        if (miao < 10) {
            strmiao = "0" + miao;
        } else {
            strmiao = String.valueOf(miao);
        }
        return strfen + strmiao;
    }

    //得到网速,每个两秒调用一次
    public String getNetSpeed(Context context) {
        long nowTotalRxBytes = TrafficStats.getUidRxBytes(context.getApplicationInfo().uid) == TrafficStats.UNSUPPORTED ? 0 : (TrafficStats.getTotalRxBytes() / 1024);//转为KB;
        long nowTimeStamp = System.currentTimeMillis();
        long speed = ((nowTotalRxBytes - lastTotalRxBytes) * 1000 / (nowTimeStamp - lastTimeStamp));//毫秒转换
        lastTimeStamp = nowTimeStamp;
        lastTotalRxBytes = nowTotalRxBytes;
        return String.valueOf(speed) + " kb/s";
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // 源图片的高度和宽度
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            // 计算出实际宽高和目标宽高的比率
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
            // 一定都会大于等于目标的宽和高。
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }


}
