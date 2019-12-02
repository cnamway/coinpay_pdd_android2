package com.spark.coinpaypddd.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.spark.coinpaypddd.R;
import com.spark.library.acp.model.OrderInTransitVo;
import com.spark.modulebase.utils.DateUtils;

import java.util.List;

/**
 * 消息中心
 */

public class MessageListAdapter extends BaseQuickAdapter<OrderInTransitVo, BaseViewHolder> {

    public MessageListAdapter(@LayoutRes int layoutResId, @Nullable List<OrderInTransitVo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderInTransitVo item) {
        int direction = item.getDirection();

        helper.setText(R.id.tvTitle, item.getAmount().toString())
                .setText(R.id.tvContent, item.getNumber().toString())
                .setText(R.id.tvCreateTime, DateUtils.getFormatTime("HH:mm:ss MM/dd", item.getCreateTime()));

        if (direction == 2) {//未读消息
            helper.setImageResource(R.id.ivNew, R.mipmap.icon_duichu);
        } else {//已读消息
            helper.setImageResource(R.id.ivNew, R.mipmap.icon_duiru);
        }

    }

}
