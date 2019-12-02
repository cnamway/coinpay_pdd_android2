package com.spark.coinpaypddd.acceptances.process;

import com.spark.library.acp.model.AcceptMerchantApplyMarginType;
import com.spark.modulebase.base.BaseContract;

/**
 * 认证类型详情
 */

public class AcceptancesProcessContract {
    interface AcceptancesProcessView extends BaseContract.BaseView {
        void getAcceptancesProcessInfoSuccess(AcceptMerchantApplyMarginType acceptMerchantApplyMarginType);

        void doAuthencationSuccess(String response);
    }

    interface AcceptancesProcessPresenter extends BaseContract.BasePresenter {
        void getAcceptancesProcessInfo(int type);

        void doAuthencation(Long id, String assetImg, String detail, String tradeDataImg);
    }
}
