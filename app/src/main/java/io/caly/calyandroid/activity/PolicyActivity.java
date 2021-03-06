package io.caly.calyandroid.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.caly.calyandroid.activity.base.BaseAppCompatActivity;
import io.caly.calyandroid.CalyApplication;
import io.caly.calyandroid.R;
import io.caly.calyandroid.util.Util;

/**
 * Copyright 2017 JSpiner. All rights reserved.
 *
 * @author jspiner (jspiner@naver.com)
 * @project CalyAndroid
 * @since 17. 2. 20
 */

public class PolicyActivity extends BaseAppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;

    @Bind(R.id.tv_policy)
    TextView tvPolicy;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy);

        init();
    }

    void init(){
        ButterKnife.bind(this);

        //init actionbar
        tvToolbarTitle.setText("개인정보 취급방침");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp);
        upArrow.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //set policy
        tvPolicy.setText(
                Html.fromHtml(
                        Util.readTextFile(getBaseContext(), "privacy_policy.html")
                )
        );

    }

    @OnClick(R.id.btn_policy_agree)
    void onAgreeClick(){
        Intent resultIntent = new Intent();
        resultIntent.putExtra("agree", true);

        setResult(RESULT_OK, resultIntent);
        finish();


        Tracker t = ((CalyApplication)getApplication()).getDefaultTracker();
        t.setScreenName(this.getClass().getName());
        t.send(
                new HitBuilders.EventBuilder()
                        .setCategory(getString(R.string.ga_action_button_click))
                        .setAction(Util.getCurrentMethodName())
                        .build()
        );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                Intent resultIntent = new Intent();
                resultIntent.putExtra("agree", false);

                setResult(RESULT_OK, resultIntent);
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
