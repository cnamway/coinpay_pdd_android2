package com.spark.coinpaypddd.my.order.detail;

import com.spark.library.acp.model.MessageResult;
import com.spark.modulebase.base.BaseContract;

/**
 * 我的订单详情
 */

public class MyOrderDetailContract {
    public interface MyOrderDetailIngView extends BaseContract.BaseView {

        void releaseOrderSuccess(MessageResult response);

        void cancelOrderSuccess(MessageResult response);

    }

    interface MyOrderDetailIngPresenter extends BaseContract.BasePresenter {

        void releaseOrder(String businessId, String tradePassword);

        void cancelOrder(String businessId, String tradePassword);
    }
}
