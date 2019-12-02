package com.spark.coinpaypddd.acceptances.authentication;

import com.spark.modulebase.base.BaseContract;

/**
 * 承兑商申请
 */
public class AuthentucatuinContract {
    interface AuthentucatuinView extends BaseContract.BaseView {

        void doUpLoadSuccess(String response);

        void doAuthencationSuccess(String response);

    }

    interface AuthentucatuinPresenter extends BaseContract.BasePresenter {

        void doUpLoad(String base64);

        void doAuthencation(Long id, String assetImg, String detail, String tradeDataImg);
    }
}
