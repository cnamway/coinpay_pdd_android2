package com.spark.coinpaypddd.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.spark.coinpaypddd.R;
import com.spark.modulebase.utils.DateUtils;
import com.spark.moduleotc.entity.StoreEntity;

import java.util.List;

/**
 * 资产记录
 */

public class StoreAdapter extends BaseQuickAdapter<StoreEntity, BaseViewHolder> {

    public StoreAdapter(@Nullable List<StoreEntity> data) {
        super(R.layout.item_store, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, StoreEntity item) {

        helper.setText(R.id.tvTitle, item.getName())
                .setText(R.id.tvOrderCount, "" + item.getDealTotal())
        .setText(R.id.tvCommission,""+item.getOrderTotal())
        .setText(R.id.tvTime, DateUtils.getFormatTime("yyyy-MM-dd HH:mm:ss", item.getCtime()));

        //状态(0禁用,1启用)
        switch (item.getStatus()) {
            case 0:
                helper.setImageResource(R.id.ivStatus, R.mipmap.store_open);
                break;
            case 1:
                helper.setImageResource(R.id.ivStatus, R.mipmap.store_stop);
                break;
        }

        helper.addOnClickListener(R.id.ivStatus);
        helper.addOnClickListener(R.id.llDelete);
    }
}
