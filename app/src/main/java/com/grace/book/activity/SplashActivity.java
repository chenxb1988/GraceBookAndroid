package com.grace.book.activity;

import android.os.Bundle;
import android.widget.ImageView;

import com.grace.book.R;
import com.grace.book.activity.base.BaseActivity;
import com.grace.book.http.response.SVG;
import com.grace.book.utils.ActivityUtils;
import com.grace.book.utils.ConstData;
import com.grace.book.widget.AnimatedSvgView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by chenxb
 * 17/1/13.
 */

public class SplashActivity extends BaseActivity {
    @Bind(R.id.iv_splash)
    ImageView mIvSplash;

    @Bind(R.id.animated_svg_view)
    AnimatedSvgView mSvgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        ConstData.initData();

        mSvgView.setOnStateChangeListener(new AnimatedSvgView.OnStateChangeListener() {

            @Override
            public void onStateChange(int state) {
                if (state == AnimatedSvgView.STATE_FINISHED) {
                    ActivityUtils.launchActivityWithAnim(SplashActivity.this, MainActivity.class, mIvSplash);
                    finish();
                }
            }
        });
        setSvg(SVG.getGraceLogo());
    }

    private void setSvg(SVG svg) {
        mSvgView.setGlyphStrings(svg.glyphs);
        mSvgView.setFillColors(svg.colors);
        mSvgView.setViewportSize(svg.width, svg.height);
        mSvgView.setTraceResidueColor(0x32000000);
        mSvgView.setTraceColors(svg.colors);
        mSvgView.rebuildGlyphData();
        mSvgView.start();
    }

    @Override
    public void onBackPressed() {

    }
}
