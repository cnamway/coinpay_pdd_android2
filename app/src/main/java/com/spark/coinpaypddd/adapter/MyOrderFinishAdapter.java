package com.spark.coinpaypddd.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.spark.coinpaypddd.MyApplication;
import com.spark.coinpaypddd.R;
import com.spark.library.acp.model.OrderAchiveVo;
import com.spark.modulebase.utils.MathUtils;
import com.spark.modulebase.utils.StringUtils;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 我的订单-已完成
 */
public class MyOrderFinishAdapter extends BaseQuickAdapter<OrderAchiveVo, BaseViewHolder> {

    public MyOrderFinishAdapter(@LayoutRes int layoutResId, @Nullable List<OrderAchiveVo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderAchiveVo item) {
        int direction = item.getDirection();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        helper.setText(R.id.tvCount, MathUtils.subZeroAndDot(item.getAmount() + ""));

        helper.setText(R.id.tvOrderCreateTime, simpleDateFormat.format(item.getCreateTime()));

        if (direction == 2) {//兑出
            helper.setImageResource(R.id.ivType, R.mipmap.icon_duichu);
            helper.setText(R.id.tvTypeCNY, R.string.str_duichu_cny);
            helper.setTextColor(R.id.tvCount, MyApplication.getAppContext().getResources().getColor(R.color.font_red));
            helper.setGone(R.id.llRelease, false);
        } else {//兑入
            helper.setImageResource(R.id.ivType, R.mipmap.icon_duiru);
            helper.setText(R.id.tvTypeCNY, R.string.str_duiru_cny);
            helper.setTextColor(R.id.tvCount, MyApplication.getAppContext().getResources().getColor(R.color.font_green));
            helper.setGone(R.id.llRelease, true);
        }
        if (StringUtils.isEmpty(item.getActualPayment())) {
            helper.setText(R.id.tvPay, MyApplication.getAppContext().getString(R.string.str_wait_pay));
            if (direction == 2) {
                helper.setText(R.id.tvPayTime, "");
            } else {
                helper.setText(R.id.tvPayTime, "");
            }
        } else {
            if (direction == 2) {
                helper.setText(R.id.tvPay, MyApplication.getAppContext().getString(R.string.str_already_pay_m));
            } else {
                helper.setText(R.id.tvPay, MyApplication.getAppContext().getString(R.string.str_already_pay));
            }
            helper.setText(R.id.tvPayTime, simpleDateFormat.format(item.getPayTime()));
        }

        if (item.getReleaseTime() != null) {
            helper.setText(R.id.tvRealeaseTime, simpleDateFormat.format(item.getReleaseTime()));
        } else {
            helper.setText(R.id.tvRealeaseTime, "");
        }

        if (item.getStatus() != null && item.getStatus() == 10) {
            helper.setText(R.id.tvRealease, MyApplication.getAppContext().getString(R.string.str_already_release_force));
        } else if (item.getStatus() != null && item.getStatus() == 7) {
            helper.setText(R.id.tvRealease, MyApplication.getAppContext().getString(R.string.str_already_release_cancel_force));
        } else {
            helper.setText(R.id.tvRealease, MyApplication.getAppContext().getString(R.string.str_already_release));
        }

    }
}
