package com.cexample.myplayer;

import android.annotation.TargetApi;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fragments.NetShipinFragment;
import com.fragments.NetYinyueFragment;
import com.fragments.ShipinFragment;
import com.fragments.YinyueFragment;

public class MainActivity extends AppCompatActivity {
    private ImageView shipinImage;
    private ImageView yinyueImage;
    private ImageView netshipinImage;
    private ImageView netyinyueImage;
    private TextView shipin;
    private TextView sousuo;
    private TextView yinyue;
    private TextView netshipin;
    private TextView netyinyue;
    private ShipinFragment shipinFragment;
    private YinyueFragment yinyueFragment;
    private NetYinyueFragment netyinyueFragment;
    private NetShipinFragment netshipinFragment;
    private FragmentManager fragmentManager = getFragmentManager();

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        shuaxinMediaSrore();
        Log.d("abcdefg", "onCreate: +++++++++++6++++++++++++++++++++++++++++++++");
        chushiFragment();
    }

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initView() {
        View decorView = getWindow().getDecorView();
        decorView.setBackgroundColor(getResources().getColor(R.color.toolbar));
        shipin = (TextView) findViewById(R.id.shipin);
        yinyue = (TextView) findViewById(R.id.yinyue);
        netshipin = (TextView) findViewById(R.id.net_shipin);
        netyinyue = (TextView) findViewById(R.id.net_yinyue);
        shipinImage = (ImageView) findViewById(R.id.shipin_image);
        yinyueImage = (ImageView) findViewById(R.id.yinyue_image);
        netshipinImage = (ImageView) findViewById(R.id.net_shipin_image);
        netyinyueImage = (ImageView) findViewById(R.id.net_yinyue_image);
        shipin.setTextColor(getColor(R.color.tab_text));
        shipinImage.setImageResource(R.drawable.tabpressed);

    }

    private void chushiFragment() {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        shipinFragment = new ShipinFragment();
        transaction.add(R.id.maincontent, shipinFragment);
        transaction.commit();
    }

    private void shuaxinMediaSrore() {
        Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        scanIntent.setData(Uri.fromFile(Environment.getExternalStorageDirectory()));
        sendBroadcast(scanIntent);
    }

    public void onclick(View view) {
        qingkongtubiao();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        yincangfragment(transaction);
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况

        switch (view.getId()) {
            case R.id.yi:
                transaction.show(shipinFragment);
                shipin.setTextColor(getResources().getColor(R.color.tab_text));
                shipinImage.setImageDrawable(getResources().getDrawable(R.drawable.tabpressed));
                break;
            case R.id.er:
                if (yinyueFragment == null) {
                    yinyueFragment = new YinyueFragment();
                    transaction.add(R.id.maincontent, yinyueFragment);
                } else {
                    transaction.show(yinyueFragment);
                }
                yinyue.setTextColor(getResources().getColor(R.color.tab_text));
                yinyueImage.setImageDrawable(getResources().getDrawable(R.drawable.aodipressed));
                break;
            case R.id.san:
                if (netyinyueFragment == null) {
                    netyinyueFragment = new NetYinyueFragment();
                    transaction.add(R.id.maincontent, netyinyueFragment);
                } else {
                    transaction.show(netyinyueFragment);
                }
                netshipin.setTextColor(getResources().getColor(R.color.tab_text));
                netshipinImage.setImageDrawable(getResources().getDrawable(R.drawable.benchipressed));
                break;
            case R.id.si:
                if (netshipinFragment == null) {
                    netshipinFragment = new NetShipinFragment();
                    transaction.add(R.id.maincontent, netshipinFragment);
                } else {
                    transaction.show(netshipinFragment);
                }
                netyinyue.setTextColor(getResources().getColor(R.color.tab_text));
                netyinyueImage.setImageDrawable(getResources().getDrawable(R.drawable.baomapressed));
                break;
            default:
                break;
        }
        transaction.commit();
    }
    private void yincangfragment(FragmentTransaction transaction) {
        if (shipinFragment != null) {
            transaction.hide(shipinFragment);
        }
        if (yinyueFragment != null) {
            transaction.hide(yinyueFragment);
        }
        if (netyinyueFragment != null) {
            transaction.hide(netyinyueFragment);
        }
        if (netshipinFragment != null) {
            transaction.hide(netshipinFragment);
        }
    }
    private void qingkongtubiao() {
        shipin.setTextColor(Color.DKGRAY);
        shipinImage.setImageDrawable(getResources().getDrawable(R.drawable.tabnormal));
        yinyue.setTextColor(Color.DKGRAY);
        yinyueImage.setImageDrawable(null);
        netshipin.setTextColor(Color.DKGRAY);
        netshipinImage.setImageDrawable(null);
        netyinyue.setTextColor(Color.DKGRAY);
        netyinyueImage.setImageDrawable(null);
    }
}
