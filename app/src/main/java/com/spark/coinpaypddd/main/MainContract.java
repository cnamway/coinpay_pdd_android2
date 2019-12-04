package com.spark.coinpaypddd.main;

import com.spark.library.acp.model.AcceptMerchantFrontVo;
import com.spark.library.acp.model.MessageResult;
import com.spark.library.acp.model.MessageResultAcceptMerchantTrade;
import com.spark.library.acp.model.MessageResultPageOrderInTransitVo;
import com.spark.library.acp.model.OrderInTransitVo;
import com.spark.moduleassets.entity.Wallet;
import com.spark.modulebase.base.BaseContract;
import com.spark.modulebase.entity.HttpErrorEntity;
import com.spark.modulelogin.entity.CasLoginEntity;
import com.spark.moduleotc.entity.AcceptanceMerchantListEntity;

import java.util.HashMap;
import java.util.List;

/**
 * 主界面
 */

public class MainContract {
    interface MainView extends BaseContract.BaseView {

        void checkBusinessLoginSuccess(CasLoginEntity casLoginEntity);

        void doLoginBusinessSuccess(String type);

        void findAcceptMerchantTradeSuccess(MessageResultAcceptMerchantTrade response);

        void findOrderInTransitSuccess(MessageResultPageOrderInTransitVo response);

        void findAuthenticationStatusSuccess(String response);

        void loginOutSuccess(String obj);

        //查询超时单
        void getOrderSuccess(List<OrderInTransitVo> list);

        void getWalletSuccess(String type, List<Wallet> list);


        void releaseOrderSuccess(MessageResult response);

        void releaseOrderFail(HttpErrorEntity httpErrorEntity);

        void checkVersionSuccess(String obj);

        void getSelfLevelInfoSuccess(AcceptMerchantFrontVo acceptMerchantFrontVo);

        void getTradeSuccess(AcceptanceMerchantListEntity response);

        void getTradeYesterdaySuccess(MessageResult response);

        void getTakingDetailUsingPOSTSuccess(MessageResult response);
    }

    interface MainPresenter extends BaseContract.BasePresenter {

        void findAcceptMerchantTrade();

        void findOrderInTransit();

        void checkBusinessLogin(String type);

        void doLoginBusiness(String tgc, String type);

        void findAuthenticationStatus();

        void loginOut();

        //查询超时单
        void getOrder(boolean isShow, HashMap<String, String> params);

        void getWallet(String type);

        void ensureOrderRelease(String businessId, String tradePassword);

        void checkVersion();

        void getTradeYesterday(String tradingDay);

        void getSelfLevelInfo();

        void getTrade();

        void getTakingDetailUsingPOST();
    }
}
