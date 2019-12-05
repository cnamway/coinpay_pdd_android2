package com.spark.coinpaypddd.add_store;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.spark.coinpaypddd.GlobalConstant;
import com.spark.coinpaypddd.R;
import com.spark.coinpaypddd.adapter.StoreAdapter;
import com.spark.coinpaypddd.base.BaseActivity;
import com.spark.coinpaypddd.event.CheckLoginSuccessEvent;
import com.spark.library.acp.model.PinduoduoStores;
import com.spark.modulebase.utils.ToastUtils;
import com.spark.moduleotc.entity.StoreEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 店铺列表
 */
public class StoreListActivity extends BaseActivity implements StoreListContract.MyAssetTradeView {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.tvOrderCount)
    TextView tvOrderCount;
    @BindView(R.id.tvCommission)
    TextView tvCommission;
    @BindView(R.id.tvFinishRate)
    TextView tvFinishRate;
    @BindView(R.id.ivSearch)
    ImageView ivSearch;
    @BindView(R.id.etUsername)
    EditText etUsername;
    @BindView(R.id.llSearch)
    LinearLayout llSearch;

    private int pageNo = 1;
    private String name;
    private StoreListPresenterImpl presenter;
    private ArrayList<StoreEntity> mDatas = new ArrayList<>();
    private StoreAdapter adapter;
    public static boolean isFlush;

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_store_list;
    }

    @Override
    protected void initView(Bundle saveInstance) {
        super.initView(saveInstance);
        ivBack.setVisibility(View.VISIBLE);
        tvTitle.setText("店铺列表");
        ivMessage.setVisibility(View.VISIBLE);
        ivMessage.setImageResource(R.mipmap.search_product);
    }

    @Override
    protected void initRv() {
        super.initRv();
        LinearLayoutManager manager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(manager);
        adapter = new StoreAdapter(mDatas);
        adapter.bindToRecyclerView(recyclerView);
    }

    @OnClick({R.id.ivMessage, R.id.ivSearch})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        Bundle bundle = null;
        switch (v.getId()) {
            case R.id.ivMessage://搜索
                llSearch.setVisibility(View.VISIBLE);
                break;
            case R.id.ivSearch:
                reflush();
                break;
        }
    }

    private void reflush() {
        adapter.setEnableLoadMore(false);
        pageNo = 1;
        getList(false);
    }

    @Override
    protected void setListener() {
        super.setListener();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reflush();
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
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                StoreEntity entity = (StoreEntity) adapter.getItem(position);
                if (entity != null) {
                    switch (view.getId()) {
                        case R.id.ivStatus://状态(0禁用,1启用)
                            if (entity.getStatus() == 1) {
                                PinduoduoStores pinduoduoGoodsDTO = new PinduoduoStores();
                                pinduoduoGoodsDTO.setId(entity.getId().intValue());
                                pinduoduoGoodsDTO.setStatus(0);
                                showCofirmUpDialog(pinduoduoGoodsDTO);
                            } else {
                                PinduoduoStores pinduoduoGoodsDTO = new PinduoduoStores();
                                pinduoduoGoodsDTO.setId(entity.getId().intValue());
                                pinduoduoGoodsDTO.setStatus(1);
                                showCofirmUpDialog(pinduoduoGoodsDTO);
                            }
                            break;
                        case R.id.llDelete:
                            showCofirmDialog(entity.getId());
                            break;
                    }

                }
            }
        });
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                StoreEntity entity = (StoreEntity) adapter.getItem(position);
                if (entity != null) {
                    Bundle bundle = new Bundle();
                    bundle = new Bundle();
                    bundle.putSerializable("data", entity);
                    showActivity(StoreDetailActivity.class, bundle);
                }
            }
        });
        etUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                reflush();
            }
        });
    }

    private void showCofirmDialog(final Long id) {
        final NormalDialog dialog = new NormalDialog(activity);
        dialog.isTitleShow(false).bgColor(Color.parseColor("#FFFFFF"))
                .content(getString(R.string.str_pay_confirm_delete))
                .contentGravity(Gravity.CENTER)
                .contentTextColor(Color.parseColor("#313131"))
                .btnTextColor(Color.parseColor("#313131"), Color.parseColor("#313131"))
                .btnText(getString(R.string.str_cancel), getString(R.string.str_sure))
                .show();
        dialog.setOnBtnClickL(new OnBtnClickL() {
            @Override
            public void onBtnClick() {
                dialog.dismiss();
            }
        }, new OnBtnClickL() {
            @Override
            public void onBtnClick() {
                presenter.delete(id);
                dialog.superDismiss();
            }
        });
    }

    //状态(0禁用,1启用)
    private void showCofirmUpDialog(final PinduoduoStores pinduoduoGoodsDTO) {
        final NormalDialog dialog = new NormalDialog(activity);
        String content = "";
        if (pinduoduoGoodsDTO.getStatus() == 1) {
            content = getString(R.string.str_pay_confirm_open);
        } else {
            content = getString(R.string.str_pay_confirm_close);
        }
        dialog.isTitleShow(false).bgColor(Color.parseColor("#FFFFFF"))
                .content(content)
                .contentGravity(Gravity.CENTER)
                .contentTextColor(Color.parseColor("#313131"))
                .btnTextColor(Color.parseColor("#313131"), Color.parseColor("#313131"))
                .btnText(getString(R.string.str_cancel), getString(R.string.str_sure))
                .show();
        dialog.setOnBtnClickL(new OnBtnClickL() {
            @Override
            public void onBtnClick() {
                dialog.dismiss();
            }
        }, new OnBtnClickL() {
            @Override
            public void onBtnClick() {
                presenter.update(true, pinduoduoGoodsDTO);
                dialog.superDismiss();
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        presenter = new StoreListPresenterImpl(this);
    }

    /**
     * 获取列表
     */
    private void getList(boolean isShow) {
        name = etUsername.getText().toString().trim();
        HashMap<String, String> map = new HashMap<>();
        map.put("pageIndex", pageNo + "");
        map.put("pageSize", GlobalConstant.PageSizeMax + "");
        map.put("name", name);
        presenter.getRecordList(isShow, map);
    }

    @Override
    protected void loadData() {
        super.loadData();
        getList(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destory();
    }

    @Override
    public void getRecordListSuccess(List<StoreEntity> list) {
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
        culData();
    }

    private void culData() {
        tvOrderCount.setText("" + mDatas.size());
        int count = 0;
        int count2 = 0;
        for (StoreEntity entity : mDatas) {
            count += entity.getDealTotal();
            count2 += entity.getOrderTotal();
        }
        tvCommission.setText("" + count);
        tvFinishRate.setText("" + count2);
    }

    @Override
    public void deleteSuccess(String response) {
        ToastUtils.showToast(activity, response);
        reflush();
    }

    @Override
    public void updateSuccess(String response) {
        ToastUtils.showToast(activity, response);
        reflush();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isFlush) {
            isFlush = false;
            reflush();
        }
    }

    /**
     * check uc、ac、acp成功后，通知刷新界面
     */
    public void onEvent(CheckLoginSuccessEvent event) {
        getList(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
