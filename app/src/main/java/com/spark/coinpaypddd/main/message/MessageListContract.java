package com.spark.coinpaypddd.main.message;

import com.spark.library.acp.model.OrderInTransitVo;
import com.spark.modulebase.base.BaseContract;

import java.util.HashMap;
import java.util.List;

/**
 * 消息中心
 */

public interface MessageListContract {
    interface MessageListView extends BaseContract.BaseView {
        void getOrderSuccess(List<OrderInTransitVo> list);

    }

    interface MessageListPresenter extends BaseContract.BasePresenter {
        void getOrder(boolean isShow, HashMap<String, String> params);

    }

}
