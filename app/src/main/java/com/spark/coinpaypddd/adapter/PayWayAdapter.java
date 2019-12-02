package com.spark.coinpaypddd.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.spark.coinpaypddd.GlobalConstant;
import com.spark.coinpaypddd.MyApplication;
import com.spark.coinpaypddd.R;
import com.spark.coinpaypddd.entity.PayWaySetting;
import com.spark.modulebase.utils.MathUtils;

import java.util.List;

/**
 * 收款方式
 */
public class PayWayAdapter extends BaseQuickAdapter<PayWaySetting, BaseViewHolder> {
    private List<PayWaySetting> payWaySettings;

    public PayWayAdapter(int layoutResId, @Nullable List<PayWaySetting> data) {
        super(layoutResId, data);
        this.payWaySettings = data;
    }

    @Override
    protected void convert(BaseViewHolder helper, PayWaySetting item) {
        if (item.getStatus() == 2) {
            helper.setGone(R.id.llUnUse, true);
            helper.getView(R.id.ivStatus).setSelected(false);
        } else {
            helper.setGone(R.id.llUnUse, false);
            if (item.getStatus() == 1) {
                helper.getView(R.id.ivStatus).setSelected(true);
            } else {
                helper.getView(R.id.ivStatus).setSelected(false);
            }
        }

//        String name = "";
//        User user = MyApplication.getAppContext().getCurrentUser();
//        if (user != null && StringUtils.isNotEmpty(user.getRealName())) {
//            name = user.getRealName();
//        }
        helper.setText(R.id.tvName, item.getRealName()).setText(R.id.tvBankNum, item.getPayAddress());

        switch (item.getPayType()) {
            case GlobalConstant.ALIPAY:
                helper.setImageResource(R.id.ivType, R.mipmap.icon_pay_ali);
                helper.setText(R.id.tvBankName, "支付宝");
                helper.setBackgroundRes(R.id.llPay, R.mipmap.icon_pay_ali_bg);
                break;
            case GlobalConstant.WECHAT:
                helper.setImageResource(R.id.ivType, R.mipmap.icon_pay_wx);
                helper.setText(R.id.tvBankName, "微信");
                helper.setBackgroundRes(R.id.llPay, R.mipmap.icon_pay_wx_bg);
                break;
            case GlobalConstant.BANK:
                helper.setImageResource(R.id.ivType, R.mipmap.icon_pay_bank);
                helper.setText(R.id.tvBankName, item.getBank());
                helper.setBackgroundRes(R.id.llPay, R.mipmap.icon_pay_bank_bg);
                break;
            case GlobalConstant.PAYPAL:
                helper.setImageResource(R.id.ivType, R.mipmap.icon_paypal);
                helper.setText(R.id.tvBankName, "Paypal");
                break;
            case GlobalConstant.PDD_ALIPAY:
                helper.setImageResource(R.id.ivType, R.mipmap.icon_pay_pdd);
                helper.setText(R.id.tvBankName, "拼多多支付宝渠道");
                helper.setText(R.id.tvBankNum, item.getRealName());
                break;
            case GlobalConstant.PDD_WECHAT:
                helper.setImageResource(R.id.ivType, R.mipmap.icon_pay_pdd);
                helper.setText(R.id.tvBankName, "拼多多微信渠道");
                helper.setText(R.id.tvBankNum, item.getRealName());
                break;
            default:
                helper.setImageResource(R.id.ivType, R.mipmap.icon_bank);
                helper.setText(R.id.tvBankName, "");
                break;
        }

        String dayUseed = "";
        if (item.getDayLimit() > item.getDayAmount()) {
            dayUseed = "(今日可用" + MathUtils.subZeroAndDot(MathUtils.getBigDecimalSubtractWithScale(item.getDayLimit() + "", item.getDayAmount() + "", 2)) + ")";
        } else {
            dayUseed = "(今日额度已用完)";
        }
        helper.setText(R.id.tvDayLimit, MyApplication.getMyApplication().getString(R.string.str_account_day_tag) + MathUtils.subZeroAndDot(item.getDayLimit() + "") + dayUseed);
//        helper.setText(R.id.tvDayLimit, MyApplication.getMyApplication().getString(R.string.str_account_day_tag) + MathUtils.subZeroAndDot(item.getDayLimit() + ""));

        helper.addOnClickListener(R.id.ivStatus);

    }
}
