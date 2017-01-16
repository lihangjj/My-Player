package com.cexample.myplayer;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private ImageView shipinImage;
    private ImageView yinyueImage;
    private ImageView netshipinImage;
    private ImageView netyinyueImage;
    private TextView shipin;
    private TextView yinyue;
    private TextView netshipin;
    private TextView netyinyue;
    private LinearLayout tab_group;
    private int position;
    Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        View decorView = getWindow().getDecorView();
        decorView.setBackgroundColor(getResources().getColor(R.color.toolbar));
        tab_group = (LinearLayout) findViewById(R.id.tab_group);
        shipin = (TextView) findViewById(R.id.shipin);
        yinyue = (TextView) findViewById(R.id.yinyue);
        netshipin = (TextView) findViewById(R.id.net_shipin);
        netyinyue = (TextView) findViewById(R.id.net_yinyue);
        shipinImage = (ImageView) findViewById(R.id.shipin_image);
        yinyueImage = (ImageView) findViewById(R.id.yinyue_image);
        netshipinImage = (ImageView) findViewById(R.id.net_shipin_image);
        netyinyueImage = (ImageView) findViewById(R.id.net_yinyue_image);
        shipin.setTextColor(getResources().getColor(R.color.tab_text));
        shipinImage.setImageDrawable(getResources().getDrawable(R.drawable.tabpressed));

    }

    public void onclick(View view) {
        qingkongtubiao();
        switch (view.getId()) {
            case R.id.yi:
                fragment = new ShipinFragment();
                shipin.setTextColor(getResources().getColor(R.color.tab_text));
                shipinImage.setImageDrawable(getResources().getDrawable(R.drawable.tabpressed));
                break;
            case R.id.er:
                fragment = new YinyueFragment();
                yinyue.setTextColor(getResources().getColor(R.color.tab_text));
                yinyueImage.setImageDrawable(getResources().getDrawable(R.drawable.tabpressed));
                break;
            case R.id.san:
                fragment = new NetShipinFragment();
                netshipin.setTextColor(getResources().getColor(R.color.tab_text));
                netshipinImage.setImageDrawable(getResources().getDrawable(R.drawable.tabpressed));
                break;
            case R.id.si:
                fragment = new NetYinyueFragment();
                netyinyue.setTextColor(getResources().getColor(R.color.tab_text));
                netyinyueImage.setImageDrawable(getResources().getDrawable(R.drawable.tabpressed));
                break;
            default:
                break;
        }
        createFragment(fragment);
    }

    private void createFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction tansaction = fragmentManager.beginTransaction();
        tansaction.replace(R.id.maincontent, fragment);
        tansaction.commit();
    }

    private void qingkongtubiao() {
        shipin.setTextColor(Color.DKGRAY);
        shipinImage.setImageDrawable(getResources().getDrawable(R.drawable.tabnormal));
        yinyue.setTextColor(Color.DKGRAY);
        yinyueImage.setImageDrawable(getResources().getDrawable(R.drawable.tabnormal));
        netshipin.setTextColor(Color.DKGRAY);
        netshipinImage.setImageDrawable(getResources().getDrawable(R.drawable.tabnormal));
        netyinyue.setTextColor(Color.DKGRAY);
        netyinyueImage.setImageDrawable(getResources().getDrawable(R.drawable.tabnormal));

    }
}
