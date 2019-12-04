package com.spark.coinpaypddd.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.spark.coinpaypddd.R;
import com.spark.moduleotc.entity.ProductEntity;

import java.util.List;

/**
 * 资产记录
 */

public class ProductAdapter extends BaseQuickAdapter<ProductEntity, BaseViewHolder> {

    public ProductAdapter(@Nullable List<ProductEntity> data) {
        super(R.layout.item_product, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ProductEntity item) {

        helper.setText(R.id.tvTitle, item.getGoodsName())
                .setText(R.id.tvContent, "链接：" + item.getGoodsUrl() + "\n" + "价格：￥" + item.getNormalPrice() + "\n" + "所属店铺：" + item.getMallId());

        //isUpper是否上架(0否,1是)
        switch (item.getIsUpper()) {
            case 0:
                helper.setText(R.id.tvStatus, "上架");
                helper.setImageResource(R.id.ivStatus, R.mipmap.up_product);
                break;
            case 1:
                helper.setText(R.id.tvStatus, "下架");
                helper.setImageResource(R.id.ivStatus, R.mipmap.down_product);
                break;
        }

        helper.addOnClickListener(R.id.llUp);
        helper.addOnClickListener(R.id.llDelete);
        helper.addOnClickListener(R.id.ivCopy);
    }
}
