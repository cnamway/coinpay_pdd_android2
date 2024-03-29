package com.spark.coinpaypddd.my.assets.base;


import com.spark.moduleassets.entity.AssetRecord;
import com.spark.moduleassets.entity.Wallet;
import com.spark.modulebase.base.BaseContract;

import java.util.List;

/**
 * 我的资产
 */

public interface MyAssetBaseContract {
    interface MyAssetBaseView extends BaseContract.BaseView {

        void getWalletSuccess(String type, List<Wallet> list);

        void getRecordListSuccess(List<AssetRecord> list);

    }

    interface MyAssetBasePresenter extends BaseContract.BasePresenter {

        void getWallet(String type);

        void getRecordList(Integer type, String busiType);

    }
}
