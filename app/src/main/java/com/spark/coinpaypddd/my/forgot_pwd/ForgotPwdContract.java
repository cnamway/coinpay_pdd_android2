package com.spark.coinpaypddd.my.forgot_pwd;


import com.spark.modulebase.base.BaseContract;

import org.json.JSONObject;

import java.io.InputStream;

/**
 * 忘记密码
 */

public interface ForgotPwdContract {
    interface ForgotPwdView extends BaseContract.BaseView {
        void getPhoneCodeSuccess(String obj);

        void captchSuccess(JSONObject obj);

        void updateForgetSuccess(String obj);

        void codeSuccess(String obj);

        void checkPhoneCodeSuccess(String response);

        void getImgSuccess(InputStream obj);
    }

    interface ForgotPwdPresenter extends BaseContract.BasePresenter {
        void getPhoneCode(String phone);

        void captch();

        void getPhoneCode(String phone, String check, String cid);

        void checkPhoneCode(String code);

        void updateForget(String mobilePhone, String newPassword, String code, String countryCode);

        void getImg();
    }

}
