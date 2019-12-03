package com.spark.coinpaypddd.add_store;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.spark.coinpaypddd.R;
import com.spark.coinpaypddd.base.BaseActivity;
import com.spark.library.acp.model.PinduoduoStores;
import com.spark.modulebase.utils.DateUtils;
import com.spark.modulebase.utils.KeyboardUtils;
import com.spark.modulebase.utils.StringUtils;
import com.spark.moduleotc.entity.StoreEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 商品详情
 */
public class StoreDetailActivity extends BaseActivity implements StoreListContract.MyAssetTradeView {

    @BindView(R.id.tvStore)
    TextView tvStore;
    @BindView(R.id.tvProduct)
    TextView tvProduct;
    @BindView(R.id.tvURL)
    EditText tvURL;
    @BindView(R.id.tvPrice)
    TextView tvPrice;
    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.tvStatus)
    TextView tvStatus;
    @BindView(R.id.tvPay)
    TextView tvPay;
    @BindView(R.id.ivEdit)
    ImageView ivEdit;

    private StoreEntity entity;
    private StoreListPresenterImpl presenter;

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_store_detail;
    }

    @Override
    protected void initView(Bundle saveInstance) {
        super.initView(saveInstance);
        ivBack.setVisibility(View.VISIBLE);
        tvTitle.setText("店铺详情");
    }

    @OnClick({R.id.ivEdit, R.id.emptyLayout})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.ivEdit:
                tvURL.requestFocus();
                tvURL.setSelection(tvURL.getText().toString().length());
                KeyboardUtils.showSoftInput(this);
                break;
            case R.id.emptyLayout:
                tvURL.clearFocus();
                KeyboardUtils.hideSoftInput(this);
                break;
        }
    }

    @Override
    protected void setListener() {
        super.setListener();
        tvURL.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String name = tvURL.getText().toString().trim();
                if (StringUtils.isNotEmpty(name)) {
                    PinduoduoStores pinduoduoGoodsDTO = new PinduoduoStores();
                    pinduoduoGoodsDTO.setId(entity.getId().intValue());
                    pinduoduoGoodsDTO.setStoreRemainTotal(Long.parseLong(name));
                    presenter.update(false, pinduoduoGoodsDTO);
                }
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        presenter = new StoreListPresenterImpl(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            entity = (StoreEntity) bundle.getSerializable("data");
            if (entity != null) {
                tvStore.setText("" + entity.getName());
                tvProduct.setText("" + entity.getMallId());
                tvURL.setText("" + entity.getStoreRemainTotal());
                tvPrice.setText(entity.getDealTotal() + "");
                tvPay.setText("" + entity.getOrderTotal());
                tvStatus.setText("");
                tvTime.setText(DateUtils.getFormatTime("yyyy-MM-dd HH:mm:ss", entity.getCtime()));
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destory();
    }

    @Override
    public void getRecordListSuccess(List<StoreEntity> list) {

    }

    @Override
    public void deleteSuccess(String list) {

    }

    @Override
    public void updateSuccess(String list) {
        StoreListActivity.isFlush = true;
    }
}
