package com.spark.coinpaypddd.my.account_pwd;

import com.android.volley.VolleyError;
import com.spark.modulebase.callback.ResponseCallBack;
import com.spark.modulebase.entity.HttpErrorEntity;
import com.spark.moduleuc.MemberControllerModel;

/**
 * 设置/修改资金密码
 */

public class AccountPwdPresenterImpl implements AccountPwdContract.Presenter {

    private AccountPwdContract.View accountPwdView;
    private MemberControllerModel memberControllerModel;

    public AccountPwdPresenterImpl(AccountPwdContract.View accountPwdView) {
        this.accountPwdView = accountPwdView;
        this.memberControllerModel = new MemberControllerModel();
    }

    @Override
    public void showLoading() {
        if (accountPwdView != null)
            accountPwdView.showLoading();
    }

    @Override
    public void hideLoading() {
        if (accountPwdView != null)
            accountPwdView.hideLoading();
    }

    @Override
    public void destory() {
        accountPwdView = null;
    }


    @Override
    public void accountPwd(String tradePassword){
        showLoading();
        memberControllerModel.setTradePassword(tradePassword, new ResponseCallBack.SuccessListener<String>() {
            @Override
            public void onResponse(String response) {
                hideLoading();
                if(accountPwdView!=null){
                    accountPwdView.accountPwdSuccess(response);
                }
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (accountPwdView != null)
                    accountPwdView.dealError(httpErrorEntity);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (accountPwdView != null)
                    accountPwdView.dealError(volleyError);
            }
        });
    }

    @Override
    public void editAccountPwd(String oldPassword,String newPassword) {
        showLoading();
        memberControllerModel.updateTradePassword(oldPassword, newPassword, new ResponseCallBack.SuccessListener<String>() {
            @Override
            public void onResponse(String response) {
                hideLoading();
                if (accountPwdView != null) {
                    accountPwdView.editAccountPwdSuccess(response);
                }
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (accountPwdView != null)
                    accountPwdView.dealError(httpErrorEntity);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (accountPwdView != null)
                    accountPwdView.dealError(volleyError);
            }
        });
    }
}
