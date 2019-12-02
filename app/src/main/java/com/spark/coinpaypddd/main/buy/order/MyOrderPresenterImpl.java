package com.spark.coinpaypddd.main.buy.order;

import com.android.volley.VolleyError;
import com.spark.library.acp.model.MessageResult;
import com.spark.modulebase.callback.ResponseCallBack;
import com.spark.modulebase.entity.HttpErrorEntity;
import com.spark.moduleotc.model.OneKeyBuyControllerModel;

import java.util.HashMap;

/**
 * Created by Administrator on 2019/3/21 0021.
 */

public class MyOrderPresenterImpl implements MyOrderContract.MyOrderPresenter {

    private MyOrderContract.MyOrderView myOrderView;
    private OneKeyBuyControllerModel oneKeyBuyControllerModel;

    public MyOrderPresenterImpl(MyOrderContract.MyOrderView myOrderView) {
        this.myOrderView = myOrderView;
        oneKeyBuyControllerModel = new OneKeyBuyControllerModel();
    }

    @Override
    public void getOrder(boolean isShow, HashMap<String, String> params) {
        if (isShow)
            showLoading();
        oneKeyBuyControllerModel.findOrderIng(params, new ResponseCallBack.SuccessListener<MessageResult>() {
            @Override
            public void onResponse(MessageResult list) {
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
