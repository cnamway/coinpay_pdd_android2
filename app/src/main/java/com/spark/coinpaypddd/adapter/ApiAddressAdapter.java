package com.spark.coinpaypddd.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.spark.coinpaypddd.R;
import com.spark.coinpaypddd.entity.ApiAddressEntity;
import com.spark.modulebase.utils.StringUtils;
import com.spark.moduleuc.UcUrls;

import java.util.List;

/**
 * Api地址
 */
public class ApiAddressAdapter extends BaseQuickAdapter<ApiAddressEntity, BaseViewHolder> {
    private List<ApiAddressEntity> payWaySettings;
    private String apiAddress;

    public ApiAddressAdapter(int layoutResId, @Nullable List<ApiAddressEntity> data, String apiAddress) {
        super(layoutResId, data);
        this.payWaySettings = data;
        this.apiAddress = apiAddress;
    }

    @Override
    protected void convert(BaseViewHolder helper, ApiAddressEntity item) {
        if (apiAddress.equals(item.getApiAddress()) && StringUtils.isNotEmpty(UcUrls.getInstance().getHost())) {
            helper.setImageResource(R.id.ivType, R.mipmap.icon_selected);
            helper.setVisible(R.id.ivType, true);
        } else {
            helper.setImageResource(R.id.ivType, R.mipmap.icon_selected);
            helper.setVisible(R.id.ivType, false);
        }
        helper.setText(R.id.tvAppName, item.getAppName()).setText(R.id.tvApiAddress, item.getApiAddress());
    }
}
