package com.spark.coinpaypddd.my.about;


import com.android.volley.VolleyError;
import com.spark.modulebase.callback.ResponseCallBack;
import com.spark.modulebase.entity.HttpErrorEntity;
import com.spark.moduleotc.model.AppVersionModel;


/**
 * Created by Administrator on 2017/9/25.
 */

public class AboutPresenterImpl implements AboutContract.AboutPresenter {
    private AboutContract.AboutView aboutView;
    private AppVersionModel appVersionModel;

    public AboutPresenterImpl(AboutContract.AboutView AboutView) {
        this.aboutView = AboutView;
        appVersionModel = new AppVersionModel();
    }


    @Override
    public void checkVersion() {
        showLoading();
        appVersionModel.getAppVersion(new ResponseCallBack.SuccessListener<String>() {
            @Override
            public void onResponse(String response) {
                hideLoading();
                if (aboutView != null)
                    aboutView.checkVersionSuccess(response);
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (aboutView != null)
                    aboutView.dealError(httpErrorEntity);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (aboutView != null)
                    aboutView.dealError(volleyError);
            }
        });
    }

    @Override
    public void showLoading() {
        if (aboutView != null)
            aboutView.showLoading();
    }

    @Override
    public void hideLoading() {
        if (aboutView != null)
            aboutView.hideLoading();
    }

    @Override
    public void destory() {
        aboutView = null;
    }
}
