package com.spark.coinpaypddd.my.account_pwd;


import com.spark.modulebase.base.BaseContract;

/**
 * 设置/修改资金密码
 */

public interface AccountPwdContract {
    interface View extends BaseContract.BaseView {

        void accountPwdSuccess(String obj);

        void editAccountPwdSuccess(String obj);

        void doPostFail(Integer code, String toastMessage);
    }

    interface Presenter extends BaseContract.BasePresenter {
        void accountPwd(String tradePassword);

        void editAccountPwd(String oldPassword, String newPassword);
    }


}
