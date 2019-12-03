package com.spark.coinpaypddd.add_store;

import com.android.volley.VolleyError;
import com.spark.library.acp.model.PinduoduoStores;
import com.spark.modulebase.callback.ResponseCallBack;
import com.spark.modulebase.entity.HttpErrorEntity;
import com.spark.modulebase.utils.LogUtils;
import com.spark.moduleotc.entity.StoreEntity;
import com.spark.moduleotc.model.PddControllerModel;

import java.util.HashMap;
import java.util.List;

/**
 * 资产明细
 */
public class StoreListPresenterImpl implements StoreListContract.MyAssetTradePresenter {
    private StoreListContract.MyAssetTradeView mView;
    private PddControllerModel pddControllerModel;

    public StoreListPresenterImpl(StoreListContract.MyAssetTradeView myAssetTradeView) {
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
        pddControllerModel.findListStore(map, new ResponseCallBack.SuccessListener<List<StoreEntity>>() {
            @Override
            public void onResponse(List<StoreEntity> list) {
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
        pddControllerModel.deleteStore(id, new ResponseCallBack.SuccessListener<String>() {
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
    public void update(boolean isShow, PinduoduoStores pinduoduoGoodsDTO) {
        if (isShow)
            showLoading();
        pddControllerModel.updateStore(pinduoduoGoodsDTO, new ResponseCallBack.SuccessListener<String>() {
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
