package com.spark.coinpaypddd.add_product;

import com.spark.modulebase.base.BaseContract;
import com.spark.moduleotc.entity.ProductEntity;

import java.util.HashMap;
import java.util.List;

/**
 * 资产明细
 */
public interface ProductListContract {
    interface MyAssetTradeView extends BaseContract.BaseView {

        void getRecordListSuccess(List<ProductEntity> list);

        void deleteSuccess(String list);

        void updateSuccess(String list);
    }

    interface MyAssetTradePresenter extends BaseContract.BasePresenter {

        void getRecordList(boolean isShow, HashMap<String, String> params);

        void delete(Long id);

        void update(Long id, Integer isUpper);
    }
}
