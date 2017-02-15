package com.grace.book.fragments;


import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.grace.book.R;
import com.grace.book.activitys.MainActivity;
import com.grace.book.base.BaseLoadingWithTitleFragment;
import com.grace.book.utils.SystemUtils;
import com.grace.book.utils.ThemeUtils;
import com.grace.book.widget.spinner.NiceSpinner;
import com.grace.book.widget.theme.ColorBackButton;
import com.grace.book.widget.theme.ColorIconTextView;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeMainFragment extends BaseLoadingWithTitleFragment {
    @Bind(R.id.avatar)
    ImageView mAvatar;
    @Bind(R.id.section_spinner)
    NiceSpinner mNiceSpinner;

    @Bind(R.id.icon_message)
    ColorIconTextView mIconMessage;
    @Bind(R.id.btn_read_book)
    ColorBackButton mBtnReadBook;
    @Bind(R.id.btn_read_bible)
    ColorBackButton mBtnReadBible;

    @Override
    public void initFragment() {
        setLoadingContentView(R.layout.fragment_home_main);

        SystemUtils.setStatusBar(getActivity(), mStatusBar);
        mIcon.setImageDrawable(new IconicsDrawable(getActivity()).color(Color.WHITE).icon(MaterialDesignIconic.Icon.gmi_home).sizeDp(20));
        mTitle.setText(R.string.homepage);

        ThemeUtils.addThemeToView(mBtnReadBook);
        ThemeUtils.addThemeToView(mBtnReadBible);
        mIconMessage.setIIcon(MaterialDesignIconic.Icon.gmi_email, 24);

        showContentView();
        Glide.with(getActivity())
                .load(R.drawable.logo)
                .bitmapTransform(new CropCircleTransformation(getActivity()))
                .dontAnimate()
                .into(mAvatar);
        List<String> dataset = new LinkedList<>(Arrays.asList("One", "Two", "Three", "Four", "Five"));
        mNiceSpinner.attachDataSource(dataset);
        mNiceSpinner.setText("选择");
    }

    @OnClick({R.id.ll_mall, R.id.ll_record, R.id.ll_contact, R.id.ll_more})
    public void onClick(View view) {
        MainActivity activity = (MainActivity) getActivity();
        switch (view.getId()) {
            case R.id.ll_mall:
                activity.switchFragment(1);
                break;
            case R.id.ll_record:
                activity.switchFragment(2);
                break;
            case R.id.ll_contact:
                activity.switchFragment(3);
                break;
            case R.id.ll_more:
                activity.switchFragment(4);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        EventBus.getDefault().unregister(this);

    }

    @Override
    protected void loadData() {

    }
}
