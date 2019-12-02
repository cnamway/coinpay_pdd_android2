package com.spark.coinpaypddd.base;


import com.spark.modulebase.base.BaseContract;

/**
 * 全局校验
 */

public interface GlobalCaptchContract {
    interface GlobalCaptchView extends BaseContract.BaseView {
        void captchSuccess(Object obj);
    }

    interface GlobalCaptchPresenter extends BaseContract.BasePresenter {
        void captch();
    }
}
