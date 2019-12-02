package com.spark.coinpaypddd.my.order;

import com.spark.library.acp.model.OrderAchiveVo;
import com.spark.library.acp.model.OrderInTransitVo;
import com.spark.modulebase.base.BaseContract;

import java.util.HashMap;
import java.util.List;

/**
 * 我的订单
 */

public interface MyOrderContract {
    interface MyOrderView extends BaseContract.BaseView {
        void getOrderSuccess(List<OrderInTransitVo> list);
    }

    interface MyOrderPresenter extends BaseContract.BasePresenter {
        void getOrder(boolean isShow, HashMap<String, String> params);
    }

    interface MyOrderFinishView extends BaseContract.BaseView {
        void getOrderSuccess(List<OrderAchiveVo> list);
    }

    interface MyOrderFinishPresenter extends BaseContract.BasePresenter {
        void getOrder(boolean isShow, HashMap<String, String> params);
    }
}
