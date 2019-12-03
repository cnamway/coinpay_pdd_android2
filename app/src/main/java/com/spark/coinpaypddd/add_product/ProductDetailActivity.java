package com.spark.coinpaypddd.add_product;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.spark.coinpaypddd.R;
import com.spark.coinpaypddd.base.BaseActivity;
import com.spark.modulebase.utils.DateUtils;
import com.spark.moduleotc.entity.ProductEntity;

import butterknife.BindView;

/**
 * 商品详情
 */
public class ProductDetailActivity extends BaseActivity {

    @BindView(R.id.tvStore)
    TextView tvStore;
    @BindView(R.id.tvProduct)
    TextView tvProduct;
    @BindView(R.id.tvURL)
    TextView tvURL;
    @BindView(R.id.tvPrice)
    TextView tvPrice;
    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.tvStatus)
    TextView tvStatus;

    private ProductEntity entity;

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_product_detail;
    }

    @Override
    protected void initView(Bundle saveInstance) {
        super.initView(saveInstance);
        ivBack.setVisibility(View.VISIBLE);
        tvTitle.setText("商品详情");
    }

    @Override
    protected void setListener() {
        super.setListener();

    }

    @Override
    protected void initData() {
        super.initData();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            entity = (ProductEntity) bundle.getSerializable("data");
            if (entity != null) {
                tvStore.setText("" + entity.getMallId());
                tvProduct.setText(entity.getGoodsName());
                tvURL.setText(entity.getGoodsUrl());
                tvPrice.setText(entity.getNormalPrice() + " 元");
                tvTime.setText(DateUtils.getFormatTime("yyyy-MM-dd HH:mm:ss", entity.getCtime()));
                if (entity.getStatus() == 1) {//是否上架(0否,1是)
                    tvStatus.setText("上架");
                } else {
                    tvStatus.setText("下架");
                }
            }
        }
    }

}
