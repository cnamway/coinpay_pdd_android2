package com.spark.coinpaypddd.acceptances.process;


import android.os.Bundle;
import android.view.View;

import com.spark.coinpaypddd.R;
import com.spark.coinpaypddd.base.BaseActivity;

/**
 * 认证流程-用户协议界面
 */
public class AcceptancesProcessTextActivity extends BaseActivity {

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_acceptances_process_text;
    }

    @Override
    protected void initView(Bundle saveInstance) {
        super.initView(saveInstance);
        ivBack.setVisibility(View.VISIBLE);
        tvTitle.setText("用户协议");
    }

}
