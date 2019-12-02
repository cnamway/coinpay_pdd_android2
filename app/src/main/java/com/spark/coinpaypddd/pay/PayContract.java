package com.spark.coinpaypddd.pay;

import com.spark.library.acp.model.MessageResult;
import com.spark.modulebase.base.BaseContract;

/**
 * 支付界面
 */

public class PayContract {
    interface PayView extends BaseContract.BaseView {

        void payCompleteSuccess(MessageResult response);

    }

    interface PayPresenter extends BaseContract.BasePresenter {

        void payComplete(String actualPayment, String orderNo, String tradePassword);

    }
}
