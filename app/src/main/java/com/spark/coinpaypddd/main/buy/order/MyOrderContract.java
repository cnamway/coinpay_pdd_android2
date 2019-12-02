package com.spark.coinpaypddd.main.buy.order;

import com.spark.library.acp.model.MessageResult;
import com.spark.modulebase.base.BaseContract;

import java.util.HashMap;

/**
 * 我的订单
 */

public interface MyOrderContract {
    interface MyOrderView extends BaseContract.BaseView {
        void getOrderSuccess(MessageResult list);
    }

    interface MyOrderPresenter extends BaseContract.BasePresenter {
        void getOrder(boolean isShow, HashMap<String, String> params);
    }

}
