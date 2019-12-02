package com.spark.coinpaypddd.main.order_detail;

import com.spark.library.acp.model.MessageResult;
import com.spark.modulebase.base.BaseContract;

/**
 * 订单详情
 */

public class OrderDetailContract {
    interface View extends BaseContract.BaseView {

        void cancelOrderSuccess(MessageResult response);

        void releaseOrderSuccess(MessageResult response);

    }

    interface Presenter extends BaseContract.BasePresenter {

        void cancelOrder(String businessId, String tradePassword);

        void ensureOrderRelease(String businessId, String tradePassword);

    }
}
