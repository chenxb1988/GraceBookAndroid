package com.grace.book.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.ListView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.grace.book.R;
import com.grace.book.adapter.AndroidAdapter;
import com.grace.book.base.BaseFragment;
import com.grace.book.beans.GanHuo;
import com.grace.book.event.SkinChangeEvent;
import com.grace.book.http.CallBack;
import com.grace.book.http.RequestManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends BaseFragment implements OnRefreshListener, OnLoadMoreListener {

    @Bind(R.id.swipe_target)
    ListView mListView;
    @Bind(R.id.swipeToLoadLayout)
    SwipeToLoadLayout mSwipeToLoadLayout;
    private AndroidAdapter adapter;
    private List<GanHuo> ganHuos = new ArrayList<>();

    private int page = 1;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_android;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        EventBus.getDefault().register(this);
        if (ganHuos.size() == 0) {
            initView();
            onRefresh();
        }
    }

    @Subscribe
    public void onEvent(SkinChangeEvent event) {
        adapter.notifyDataSetChanged();

    }

    private void getData(final boolean isRefresh) {
        int pageSize = 30;
        RequestManager.get(getName(), "http://gank.io/api/data/all/"
                        + String.valueOf(pageSize) + "/"
                        + String.valueOf(page), isRefresh,
                new CallBack<List<GanHuo>>() {
                    @Override
                    public void onSuccess(List<GanHuo> result) {
                        if (isRefresh) {
                            ganHuos.clear();
                            page = 2;
                        } else {
                            page++;
                        }
                        for (GanHuo ganHuo : result) {
                            if (!ganHuo.getType().equals("福利")) {
                                ganHuos.add(ganHuo);
                            }
                        }
//                      ganHuos.addAll(result);
                        adapter.notifyDataSetChanged();
                        if (mSwipeToLoadLayout != null) {
                            mSwipeToLoadLayout.setRefreshing(false);
                            mSwipeToLoadLayout.setLoadingMore(false);
                        }
                    }

                    @Override
                    public void onFailure(String message) {
                        super.onFailure(message);
                        if (mSwipeToLoadLayout != null) {
                            mSwipeToLoadLayout.setRefreshing(false);
                            mSwipeToLoadLayout.setLoadingMore(false);
                        }
                    }
                });
    }

    private void initView() {
        mSwipeToLoadLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeToLoadLayout.setRefreshing(true);
            }
        });
        mSwipeToLoadLayout.setOnRefreshListener(this);
        mSwipeToLoadLayout.setOnLoadMoreListener(this);
        adapter = new AndroidAdapter(getActivity(), ganHuos);
        mListView.setAdapter(adapter);
    }

    @Override
    public void onRefresh() {
        page = 1;
        getData(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);

    }

    @Override
    public void onLoadMore() {
        getData(false);
    }
}
