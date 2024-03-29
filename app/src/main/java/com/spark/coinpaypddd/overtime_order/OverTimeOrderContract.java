package com.spark.coinpaypddd.overtime_order;

import com.spark.library.acp.model.MessageResult;
import com.spark.library.acp.model.OrderInTransitVo;
import com.spark.modulebase.base.BaseContract;

import java.util.HashMap;
import java.util.List;

/**
 * 超时单
 */

public interface OverTimeOrderContract {
    interface MyOrderView extends BaseContract.BaseView {
        void getOrderSuccess(List<OrderInTransitVo> list);

        void releaseOrderSuccess(MessageResult response);
    }

    interface MyOrderPresenter extends BaseContract.BasePresenter {
        void getOrder(boolean isShow, HashMap<String, String> params);

        void releaseOrder(String businessId, String tradePassword);
    }

}
