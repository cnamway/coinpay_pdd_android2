package com.spark.coinpaypddd.add_product;

import com.android.volley.VolleyError;
import com.spark.modulebase.callback.ResponseCallBack;
import com.spark.modulebase.entity.HttpErrorEntity;
import com.spark.modulebase.utils.LogUtils;
import com.spark.moduleotc.entity.ProductEntity;
import com.spark.moduleotc.model.PddControllerModel;

import java.util.HashMap;
import java.util.List;

/**
 * 资产明细
 */
public class ProductListPresenterImpl implements ProductListContract.MyAssetTradePresenter {
    private ProductListContract.MyAssetTradeView mView;
    private PddControllerModel pddControllerModel;

    public ProductListPresenterImpl(ProductListContract.MyAssetTradeView myAssetTradeView) {
        this.mView = myAssetTradeView;
        pddControllerModel = new PddControllerModel();
    }

    @Override
    public void showLoading() {
        if (mView != null)
            mView.showLoading();
    }

    @Override
    public void hideLoading() {
        if (mView != null)
            mView.hideLoading();
    }

    @Override
    public void destory() {
        mView = null;
    }

    @Override
    public void getRecordList(boolean isShow, HashMap<String, String> map) {
        if (isShow)
            showLoading();
        pddControllerModel.findList(map, new ResponseCallBack.SuccessListener<List<ProductEntity>>() {
            @Override
            public void onResponse(List<ProductEntity> list) {
                hideLoading();
                if (mView != null)
                    mView.getRecordListSuccess(list);
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (mView != null)
                    mView.dealError(httpErrorEntity);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (mView != null)
                    mView.dealError(volleyError);
            }
        });
    }

    @Override
    public void delete(Long id) {
        showLoading();
        pddControllerModel.deleteProduct(id, new ResponseCallBack.SuccessListener<String>() {
            @Override
            public void onResponse(String list) {
                LogUtils.i("response==" + list.toString());
                hideLoading();
                if (mView != null)
                    mView.deleteSuccess(list);
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (mView != null)
                    mView.dealError(httpErrorEntity);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (mView != null)
                    mView.dealError(volleyError);
            }
        });
    }

    @Override
    public void update(Long id, Integer isUpper) {
        showLoading();
        pddControllerModel.updateProduct(id, isUpper, new ResponseCallBack.SuccessListener<String>() {
            @Override
            public void onResponse(String list) {
                LogUtils.i("response==" + list.toString());
                hideLoading();
                if (mView != null)
                    mView.updateSuccess(list);
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (mView != null)
                    mView.dealError(httpErrorEntity);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (mView != null)
                    mView.dealError(volleyError);
            }
        });
    }

}
