package com.spark.coinpaypddd.add_product;

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

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.spark.coinpaypddd.GlobalConstant;
import com.spark.coinpaypddd.R;
import com.spark.coinpaypddd.adapter.ProductAdapter;
import com.spark.coinpaypddd.base.BaseActivity;
import com.spark.coinpaypddd.event.CheckLoginSuccessEvent;
import com.spark.modulebase.utils.ToastUtils;
import com.spark.moduleotc.entity.ProductEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.spark.moduleassets.AssetsConstants.ACP;

/**
 * 商品列表
 */
public class ProductListActivity extends BaseActivity implements ProductListContract.MyAssetTradeView {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.ivSearch)
    ImageView ivSearch;
    @BindView(R.id.etUsername)
    EditText etUsername;

    private int pageNo = 1;
    private String name;
    private ProductListPresenterImpl presenter;
    private ArrayList<ProductEntity> mDatas = new ArrayList<>();
    private ProductAdapter adapter;

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_product_list;
    }

    @Override
    protected void initView(Bundle saveInstance) {
        super.initView(saveInstance);
        ivBack.setVisibility(View.VISIBLE);
        tvTitle.setText("商品列表");
    }

    @Override
    protected void initRv() {
        super.initRv();
        LinearLayoutManager manager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(manager);
        adapter = new ProductAdapter(mDatas);
        adapter.bindToRecyclerView(recyclerView);
    }

    @OnClick({R.id.ivSearch})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
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
                ProductEntity entity = (ProductEntity) adapter.getItem(position);
                if (entity != null) {
                    switch (view.getId()) {
                        case R.id.llUp:
                            if (entity.getIsUpper() == 1) {
                                showCofirmUpDialog(entity.getId(), 0);
                            } else {
                                showCofirmUpDialog(entity.getId(), 1);
                            }
                            break;
                        case R.id.llDelete:
                            showCofirmDialog(entity.getId());
                            break;
                        case R.id.ivCopy:
                            copyText(entity.getGoodsUrl());
                            break;
                    }

                }
            }
        });
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ProductEntity entity = (ProductEntity) adapter.getItem(position);
                if (entity != null) {
                    Bundle bundle = new Bundle();
                    bundle = new Bundle();
                    bundle.putSerializable("data", entity);
                    showActivity(ProductDetailActivity.class, bundle);
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

    //是否上架(0否,1是)
    private void showCofirmUpDialog(final Long id, final Integer isUpper) {
        final NormalDialog dialog = new NormalDialog(activity);
        String content = "";
        if (isUpper == 1) {
            content = getString(R.string.str_pay_confirm_up);
        } else {
            content = getString(R.string.str_pay_confirm_down);
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
                presenter.update(id, isUpper);
                dialog.superDismiss();
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        presenter = new ProductListPresenterImpl(this);
    }

    /**
     * 获取列表
     */
    private void getList(boolean isShow) {
        name = etUsername.getText().toString().trim();
        HashMap<String, String> map = new HashMap<>();
        map.put("pageIndex", pageNo + "");
        map.put("pageSize", GlobalConstant.PageSize + "");
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
    public void getRecordListSuccess(List<ProductEntity> list) {
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

    /**
     * check uc、ac、acp成功后，通知刷新界面
     */
    public void onEvent(CheckLoginSuccessEvent event) {
        getList(false);
    }

}