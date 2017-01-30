package com.grace.book.fragments;


import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.CompoundButton;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.grace.book.R;
import com.grace.book.base.BaseFragment;
import com.grace.book.base.MallItemAdapter;
import com.grace.book.beans.GanHuo;
import com.grace.book.beans.MallItem;
import com.grace.book.event.SkinChangeEvent;
import com.grace.book.http.CallBack;
import com.grace.book.http.RequestManager;
import com.grace.book.widget.SwitchButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeMallFragment extends BaseFragment implements OnRefreshListener, OnLoadMoreListener {

    @Bind(R.id.swipe_target)
    RecyclerView recyclerView;
    @Bind(R.id.swipeToLoadLayout)
    SwipeToLoadLayout mSwipeToLoadLayout;
    @Bind(R.id.switch_list)
    SwitchButton switchButton;

    private MallItemAdapter itemAdapter;
    private GridLayoutManager gridLayoutManager;
    private List<MallItem> items = new ArrayList<>();

    private int page = 1;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_home_mall;
    }

    @Override
    public void initFragment() {
        EventBus.getDefault().register(this);
        initView();
        onRefresh();
    }

    @Subscribe
    public void onEvent(SkinChangeEvent event) {
//        adapter.notifyDataSetChanged();

    }

    //TODO
    private void addTestItems(int page) {
        items.add(new MallItem(R.drawable.logo, "Image 1" + page, 20, 33));
        items.add(new MallItem(R.drawable.logo, "Image 2" + page, 10, 54));
        items.add(new MallItem(R.drawable.logo, "Image 3" + page, 27, 20));
        items.add(new MallItem(R.drawable.logo, "Image 4" + page, 45, 67));
    }

    private void getData(final boolean isRefresh) {
        int pageSize = 4;
        RequestManager.get(getName(), "http://gank.io/api/data/all/"
                        + String.valueOf(pageSize) + "/"
                        + String.valueOf(page), isRefresh,
                new CallBack<List<GanHuo>>() {
                    @Override
                    public void onSuccess(List<GanHuo> result) {
                        if (isRefresh) {
                            items.clear();
                            page = 2;
                        } else {
                            page++;
                        }
                        addTestItems(page);
                        itemAdapter.notifyDataSetChanged();

                        if (mSwipeToLoadLayout != null) {
                            mSwipeToLoadLayout.setRefreshing(false);
                            mSwipeToLoadLayout.setLoadingMore(false);
                        }

                        // 如果最后一页,取消上拉加载更多
//                        mSwipeToLoadLayout.setLoadMoreEnabled(false);
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
        addTestItems(1);

        mSwipeToLoadLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeToLoadLayout.setRefreshing(true);
            }
        });
        mSwipeToLoadLayout.setOnRefreshListener(this);
        mSwipeToLoadLayout.setOnLoadMoreListener(this);

        gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        itemAdapter = new MallItemAdapter(items, gridLayoutManager);
        recyclerView.setAdapter(itemAdapter);
        recyclerView.setLayoutManager(gridLayoutManager);

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                checkLoadMore(newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switchLayout();
            }
        });
    }

    private void checkLoadMore() {
        checkLoadMore(RecyclerView.SCROLL_STATE_IDLE);
    }

    private void checkLoadMore(int state) {
        LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
        // 当不滚动时
        if (state == RecyclerView.SCROLL_STATE_IDLE) {
            int lastVisibleItem = manager.findLastCompletelyVisibleItemPosition()
                    * (gridLayoutManager.getSpanCount() + 1) - 1;
            int totalItemCount = manager.getItemCount();

            if (lastVisibleItem >= (totalItemCount - 1)) {
                mSwipeToLoadLayout.setLoadingMore(true);
            }
        }
    }

    private void switchLayout() {
        switch (gridLayoutManager.getSpanCount()) {
            case 1:
                gridLayoutManager.setSpanCount(3);
                break;
            case 3:
                gridLayoutManager.setSpanCount(1);
                break;
            default:
                gridLayoutManager.setSpanCount(1);
                break;
        }
        itemAdapter.notifyItemRangeChanged(0, itemAdapter.getItemCount());
        mSwipeToLoadLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkLoadMore();
            }
        }, 500);
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