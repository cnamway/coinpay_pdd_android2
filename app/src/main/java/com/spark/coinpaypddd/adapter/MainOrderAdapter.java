package com.spark.coinpaypddd.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spark.coinpaypddd.GlobalConstant;
import com.spark.coinpaypddd.MyApplication;
import com.spark.coinpaypddd.R;
import com.spark.coinpaypddd.entity.PayData;
import com.spark.coinpaypddd.event.MainTimeDownFinishEvent;
import com.spark.library.acp.model.OrderInTransitVo;
import com.spark.modulebase.utils.AppUtils;
import com.spark.modulebase.utils.DateUtils;
import com.spark.modulebase.utils.LogUtils;
import com.spark.modulebase.utils.MathUtils;
import com.spark.modulebase.utils.StringUtils;

import java.util.Date;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 首页多单
 */
public class MainOrderAdapter extends BaseQuickAdapter<OrderInTransitVo, BaseViewHolder> {
    private List<OrderInTransitVo> payWaySettings;

    public MainOrderAdapter(int layoutResId, @Nullable List<OrderInTransitVo> data) {
        super(layoutResId, data);
        this.payWaySettings = data;
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderInTransitVo item) {
        int direction = item.getDirection();

        helper.setText(R.id.tvCount, MathUtils.subZeroAndDot(item.getAmount() + ""));
        helper.setText(R.id.tvOrderCreateTime, DateUtils.getFormatTime(DateUtils.DATE_FORMAT_1, item.getCreateTime()));

        helper.setText(R.id.tvPayCount, MathUtils.subZeroAndDot(item.getNumber() + ""));
        List<PayData> payDatas = new Gson().fromJson(item.getPayData(), new TypeToken<List<PayData>>() {
        }.getType());
        if (payDatas != null && payDatas.size() > 0) {
            PayData curPayData = payDatas.get(0);
            String payType = curPayData.getPayType();
            String result = "";
            switch (payType) {
                case GlobalConstant.ALIPAY:
                    result = AppUtils.getPayAccountStr(curPayData.getPayAddress(), 1, MyApplication.getMyApplication().getResources().getString(R.string.str_ali));
                    break;
                case GlobalConstant.WECHAT:
                    result = AppUtils.getPayAccountStr(curPayData.getPayAddress(), 2, MyApplication.getMyApplication().getResources().getString(R.string.str_wechat));
                    break;
                case GlobalConstant.BANK:
                    result = AppUtils.getPayAccountStr(curPayData.getPayAddress(), 3, curPayData.getBank());
                    break;
                case GlobalConstant.PAYPAL:
                    result = AppUtils.getPayAccountStr(curPayData.getPayAddress(), 4, MyApplication.getMyApplication().getResources().getString(R.string.str_paypal));
                    break;
                case GlobalConstant.PDD_ALIPAY:
                    result = AppUtils.getPayAccountStr(curPayData.getRealName(), 5, MyApplication.getMyApplication().getResources().getString(R.string.str_pdd));
                    break;
                case GlobalConstant.PDD_WECHAT:
                    result = AppUtils.getPayAccountStr(curPayData.getRealName(), 6, MyApplication.getMyApplication().getResources().getString(R.string.str_pdd));
                    break;
            }
            helper.setText(R.id.tvPayRefer, result);
        }
        helper.setText(R.id.tvRate, MathUtils.subZeroAndDot(item.getPrice() + ""));

        if (direction == 2) {//买币
            helper.setImageResource(R.id.ivType, R.mipmap.icon_duichu);
            helper.setText(R.id.tvTypeCNY, R.string.str_duichu_cny);
            helper.setTextColor(R.id.tvCount, MyApplication.getMyApplication().getResources().getColor(R.color.font_red));
            helper.setText(R.id.tvDirection, String.format(MyApplication.getMyApplication().getString(R.string.str_get_usdt), item.getCoinId()));
            helper.setGone(R.id.llRelease,false);
        } else {//卖币
            helper.setImageResource(R.id.ivType, R.mipmap.icon_duiru);
            helper.setText(R.id.tvTypeCNY, R.string.str_duiru_cny);
            helper.setTextColor(R.id.tvCount, MyApplication.getMyApplication().getResources().getColor(R.color.font_green));
            helper.setText(R.id.tvDirection, String.format(MyApplication.getMyApplication().getString(R.string.str_pay_usdt), item.getCoinId()));
            helper.setGone(R.id. llRelease,true);
        }
        if (StringUtils.isEmpty(item.getActualPayment())) {
            helper.setText(R.id.tvPay, MyApplication.getMyApplication().getString(R.string.str_wait_pay));
            helper.setText(R.id.tvRealease, MyApplication.getMyApplication().getString(R.string.str_need_release));
            helper.setText(R.id.tvRealeaseTime, "");
            if (direction == 2) {
                long endTime = item.getCreateTime().getTime() + item.getTimeLimit() * 1000;
                long nowTime = new Date().getTime();
                long randomTime = endTime - nowTime;
                LogUtils.e("时间差：" + randomTime);
                if (randomTime > 0) {
                    setTime(1, randomTime, helper);
                } else {
                    setTime(1, 0, helper);
                    EventBus.getDefault().post(new MainTimeDownFinishEvent(randomTime));
                }
            } else {
                long endTime = item.getCreateTime().getTime() + item.getTimeLimit() * 1000;
                long nowTime = new Date().getTime();
                long randomTime = endTime - nowTime;
                LogUtils.e("时间差：" + randomTime);
                if (randomTime > 0) {
                    setTime(3, randomTime, helper);
                } else {
                    setTime(3, 0, helper);
                    EventBus.getDefault().post(new MainTimeDownFinishEvent(randomTime));
                }

                helper.setGone(R.id.llRelease,false);
            }
        } else {
            helper.setText(R.id.tvPay, MyApplication.getMyApplication().getString(R.string.str_already_pay));
            if (item.getStatus() != null) {
                //订单状态 0-已取消 1-未付款 2-已付款 3-已完成 4-申诉中 5.超时单
                if (item.getStatus() == 5) {//超时单
                    helper.setText(R.id.tvRealease, MyApplication.getMyApplication().getString(R.string.str_need_release_out));
                    helper.setText(R.id.tvRealeaseTime, "");
                } else {
                    helper.setText(R.id.tvRealease, MyApplication.getMyApplication().getString(R.string.str_need_release));
                    long endTime = item.getCreateTime().getTime() + item.getTimeLimit() * 1000;
                    long nowTime = new Date().getTime();
                    long randomTime = endTime - nowTime;
                    LogUtils.e("时间差：" + randomTime);
                    if (randomTime > 0) {
                        setTime(2, randomTime, helper);
                    } else {
                        setTime(2, 0, helper);
                        EventBus.getDefault().post(new MainTimeDownFinishEvent(randomTime));
                    }

                }
            } else {
                helper.setText(R.id.tvRealease, MyApplication.getMyApplication().getString(R.string.str_need_release));
                helper.setText(R.id.tvRealeaseTime, "");
            }

            helper.setText(R.id.tvPayTime, DateUtils.getFormatTime(DateUtils.DATE_FORMAT_1, item.getPayTime()));
        }

    }

    /**
     * 设置时间
     *
     * @param position
     * @param randomTime
     * @param helper
     */
    private void setTime(int position, long randomTime, BaseViewHolder helper) {
        if (helper != null) {
            if (position == 1) {//1待付款 2待放行
                helper.setText(R.id.tvPayTime, DateUtils.getTimeDown(randomTime, DateUtils.DATE_FORMAT_2, "", DateUtils.DATE_FORMAT_3));
            } else if (position == 2) {
                helper.setText(R.id.tvRealeaseTime, DateUtils.getTimeDown(randomTime, DateUtils.DATE_FORMAT_2, "", DateUtils.DATE_FORMAT_4));
            } else if (position == 3) {
                helper.setText(R.id.tvPayTime, DateUtils.getTimeDown(randomTime, DateUtils.DATE_FORMAT_2, "", DateUtils.DATE_FORMAT_4));
            }
        }
    }
}
