package com.spark.coinpaypddd.my.order;

import com.android.volley.VolleyError;
import com.spark.library.acp.model.OrderInTransitVo;
import com.spark.modulebase.callback.ResponseCallBack;
import com.spark.modulebase.entity.HttpErrorEntity;
import com.spark.moduleotc.model.OrderModel;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2019/3/21 0021.
 */

public class MyOrderPresenterImpl implements MyOrderContract.MyOrderPresenter {

    private MyOrderContract.MyOrderView myOrderView;
    private OrderModel orderModel;

    public MyOrderPresenterImpl(MyOrderContract.MyOrderView myOrderView) {
        this.myOrderView = myOrderView;
        orderModel = new OrderModel();
    }

    @Override
    public void getOrder(boolean isShow, HashMap<String, String> params) {
        if (isShow)
            showLoading();
        orderModel.findOrderIng(params, new ResponseCallBack.SuccessListener<List<OrderInTransitVo>>() {
            @Override
            public void onResponse(List<OrderInTransitVo> list) {
                hideLoading();
                if (myOrderView != null) {
                    myOrderView.getOrderSuccess(list);
                }
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (myOrderView != null) {
                    myOrderView.dealError(httpErrorEntity);
                }
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (myOrderView != null) {
                    myOrderView.dealError(volleyError);
                }
            }
        });
    }

    @Override
    public void showLoading() {
        if (myOrderView != null)
            myOrderView.showLoading();
    }

    @Override
    public void hideLoading() {
        if (myOrderView != null)
            myOrderView.hideLoading();
    }

    @Override
    public void destory() {
        myOrderView = null;
    }
}
