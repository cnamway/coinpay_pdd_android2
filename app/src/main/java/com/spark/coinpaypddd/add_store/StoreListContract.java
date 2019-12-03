package com.spark.coinpaypddd.add_store;

import com.spark.library.acp.model.PinduoduoStores;
import com.spark.modulebase.base.BaseContract;
import com.spark.moduleotc.entity.StoreEntity;

import java.util.HashMap;
import java.util.List;

/**
 * 资产明细
 */
public interface StoreListContract {
    interface MyAssetTradeView extends BaseContract.BaseView {

        void getRecordListSuccess(List<StoreEntity> list);

        void deleteSuccess(String list);

        void updateSuccess(String list);
    }

    interface MyAssetTradePresenter extends BaseContract.BasePresenter {

        void getRecordList(boolean isShow, HashMap<String, String> params);

        void delete(Long id);

        void update(boolean isShow, PinduoduoStores pinduoduoGoodsDTO);
    }
}
