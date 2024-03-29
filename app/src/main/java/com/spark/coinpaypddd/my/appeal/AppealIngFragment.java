package com.spark.coinpaypddd.my.appeal;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.spark.coinpaypddd.GlobalConstant;
import com.spark.coinpaypddd.R;
import com.spark.coinpaypddd.adapter.AppealIngAdapter;
import com.spark.coinpaypddd.base.BaseFragment;
import com.spark.coinpaypddd.event.CheckLoginSuccessEvent;
import com.spark.coinpaypddd.event.PsListFrushEvent;
import com.spark.coinpaypddd.my.appeal.detail.AppealDetailIngActivity;
import com.spark.library.acp.model.AppealApplyInTransitVo;
import com.spark.modulebase.utils.LogUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;


/**
 * 纠纷管理-进行中
 */
public class AppealIngFragment extends BaseFragment implements AppealContract.AppealView {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    private int pageNo = 1;
    private AppealPresenterImpl presenter;
    private List<AppealApplyInTransitVo> mDatas = new ArrayList<>();
    private AppealIngAdapter adapter;
    public static AppealApplyInTransitVo appealApplyInTransit;


    @Override
    public int getFragmentLayoutId() {
        return R.layout.activity_common_listview;
    }

    @Override
    public String getmTag() {
        return this.getClass().getSimpleName();
    }

    @Override
    protected void initView(Bundle saveInstance) {
        super.initView(saveInstance);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new AppealIngAdapter(R.layout.item_appeal, mDatas);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void initData() {
        presenter = new AppealPresenterImpl(this);
    }

    @Override
    protected void loadData() {
        pageNo = 1;
        getList(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.destory();
    }

    @Override
    protected void setListener() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reflush();
            }
        });
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                appealApplyInTransit = (AppealApplyInTransitVo) adapter.getItem(position);
                showActivity(AppealDetailIngActivity.class, null, 1);
            }
        });
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                refreshLayout.setEnabled(false);
                pageNo = pageNo + 1;
                getList(false);
            }
        }, recyclerView);
    }


    /**
     * 获取列表
     */
    private void getList(boolean isShow) {
        HashMap<String, String> map = new HashMap<>();
        map.put("pageNo", pageNo + "");
        map.put("pageSize", GlobalConstant.PageSize + "");
        //map.put("status", "0");
        presenter.getOrder(isShow, map);
    }


    @Override
    public void getOrderSuccess(List<AppealApplyInTransitVo> list) {
        refreshLayout.setEnabled(true);
        refreshLayout.setRefreshing(false);
        adapter.setEnableLoadMore(true);
        adapter.loadMoreComplete();
        if (list == null) return;
        if (pageNo == 1) {
            mDatas.clear();
            if (list.size() == 0) {
                adapter.loadMoreEnd();
                adapter.setEmptyView(R.layout.empty_layout);
                adapter.notifyDataSetChanged();
            } else {
                mDatas.addAll(list);
            }
        } else {
            if (list.size() != 0) mDatas.addAll(list);
            else adapter.loadMoreEnd();
        }
        adapter.notifyDataSetChanged();
        adapter.disableLoadMoreIfNotFullPage();
    }

    public void onEvent(PsListFrushEvent event) {
        LogUtils.e("====确认======回调刷新啦");
        if (AppealActivity.isSaleOrder) {
            reflush();
        } else {
            reflush();
        }
    }

    private void reflush() {
        adapter.setEnableLoadMore(false);
        pageNo = 1;
        getList(false);
    }
    /**
     * check uc、ac、acp成功后，通知刷新界面
     */
    public void onEvent(CheckLoginSuccessEvent event) {
        loadData();
    }
}
