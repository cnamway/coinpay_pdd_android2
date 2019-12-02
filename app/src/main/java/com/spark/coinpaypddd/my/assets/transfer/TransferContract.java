package com.spark.coinpaypddd.my.assets.transfer;


import com.spark.library.ac.model.AssetTransferDto;
import com.spark.modulebase.base.BaseContract;

import java.math.BigDecimal;

/**
 * 资金划转
 */

public interface TransferContract {
    interface TransferView extends BaseContract.BaseView {
        void doWithDrawSuccess(String response);
    }

    interface TransferPresenter extends BaseContract.BasePresenter {
        void doWithDraw(BigDecimal amount, String coinName, AssetTransferDto.FromEnum from, AssetTransferDto.ToEnum to, String tradePassword);

    }
}
