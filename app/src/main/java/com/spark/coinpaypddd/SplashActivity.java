package com.spark.coinpaypddd;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.spark.coinpaypddd.base.BaseActivity;
import com.spark.coinpaypddd.entity.SystemEntity;
import com.spark.coinpaypddd.login.LoginActivity;
import com.spark.coinpaypddd.main.MainActivity;
import com.spark.moduleassets.AcUrls;
import com.spark.modulebase.BaseApplication;
import com.spark.modulebase.entity.HttpErrorEntity;
import com.spark.modulebase.entity.User;
import com.spark.modulebase.okhttp.SendRemoteDataUtil;
import com.spark.modulebase.utils.LogUtils;
import com.spark.modulebase.utils.SharedPreferencesUtil;
import com.spark.modulebase.utils.StringUtils;
import com.spark.modulelogin.LoginUrls;
import com.spark.moduleotc.OtcUrls;
import com.spark.moduleuc.AgentUrls;
import com.spark.moduleuc.UcUrls;
import com.spark.netty_library.GuardService;
import com.spark.netty_library.WebSocketService;


import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;

import static com.spark.modulebase.utils.SharedPreferencesUtil.SP_KEY_API_ADDRESS;
import static com.spark.modulebase.utils.SharedPreferencesUtil.SP_KEY_WRITE_API;
import static com.spark.modulebase.utils.SharedPreferencesUtil.SP_KEY_WRITE_API_AC;
import static com.spark.modulebase.utils.SharedPreferencesUtil.SP_KEY_WRITE_API_ACP;
import static com.spark.modulebase.utils.SharedPreferencesUtil.SP_KEY_WRITE_API_UC;
import static com.spark.modulebase.utils.SharedPreferencesUtil.SP_KEY_clearCode310;

public class SplashActivity extends BaseActivity {
    @BindView(R.id.iv_pic)
    ImageView ivPic;

    private Timer timer;
    private int n = 3;

//    String appName = "欧沃";
//    String apiAddress = "caymanex.pro";

//    String appName = "合众承兑";
//    String apiAddress = "555hub.com";

//            String appName = "币承兑";
//            String apiAddress = "bifushangmao.com";

//            String appName = "亿易商贸";
//            String apiAddress = "dbipay.com";

//            String appName = "通支付";
//            String apiAddress = "tongzhifu.vip";

//            String appName = "鑫众支付";
//            String apiAddress = "brick.asia";

//            String appName = "币承兑";
//            String apiAddress = "bp.wxmarket.cn";

//            String appName = "Money承兑";
//            String apiAddress = "money123.vip";

//            String appName = "蚂蚁搬砖";
//            String apiAddress = "1688up.net";

//            String appName = "网思";
//            String apiAddress = "51ws.vip";

//            String appName = "BTD";
//            String apiAddress = "btd.bifushangmao.com";

//            String appName = "Subverter";
//            String apiAddress = "sub.bifushangmao.com";

//            String appName = "泰达易宝";
//            String apiAddress = "hn772.cn";

//            String appName = "币安";
//            String apiAddress = "bittoppayment.top";

    //    String appName = "币赚";
//    String apiAddress = "bizhuanex.com";

    //            String appName = "币付";
//            String apiAddress = "bittopbian.work";

//    String appName = "币客";
    String apiAddress = "bikeshangmao.com";

    //压测环境
//    String apiAddress = "bench.bitpay.com";

    private final String baseURL = "http://bian2." + apiAddress + "/config.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //RestartUtils.getInstance().setAppStatus(STATUS_NORMAL);
        LogUtils.e("BaseApplication=======SplashActivity()=====flag==" + BaseApplication.flag);
        BaseApplication.flag = 0;
        super.onCreate(savedInstanceState);
        isRoot();
        ImmersionBar.with(this).fullScreen(true).init();
        timerStart();
        //获取系统配置信息
        handler.sendEmptyMessage(0);
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initView(Bundle saveInstance) {
        //启动页变形优化
        //AppUtils.scaleImage(this, ivPic, R.mipmap.splash);
    }

    private void timerStart() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (n == 0) {
                    timer.cancel();
                    timer = null;
//                    boolean isFirst = !SharedPreferencesUtil.getInstance(activity).contains(SP_KEY_FIRST_USE);
//                    if (isFirst) {
//                        showActivity(GuideActivity.class, null);
//                    } else {
                    User user = MyApplication.getAppContext().getCurrentUser();
                    if (user.isLogin()) {

                        String clearCode310 = SharedPreferencesUtil.getInstance(activity).getStringParam(SP_KEY_clearCode310);

                        if (StringUtils.isEmpty(clearCode310)) {
                            MyApplication.getAppContext().deleteCurrentUser();
                            MyApplication.getAppContext().getCookieManager().getCookieStore().removeAll();
                            showActivity(LoginActivity.class, null);
                        } else {
                            boolean isTest = SharedPreferencesUtil.getInstance(activity).getBooleanParam(SP_KEY_WRITE_API);
                            if (!isTest) {
                                String apiAddress = SharedPreferencesUtil.getInstance(activity).getStringParam(SP_KEY_API_ADDRESS);
                                if (StringUtils.isEmpty(apiAddress)) {
                                    //ToastUtils.showToast("请先设置API地址");
                                    return;
                                }

                                com.spark.coinpaypddd.GlobalConstant.isTest = false;
                                GuardService.isTest = false;
                                WebSocketService.isTest = false;

                                com.spark.coinpaypddd.GlobalConstant.CUR_HOST = apiAddress;
                                WebSocketService.setHost(apiAddress);
                                GuardService.setHost(apiAddress);

                                LoginUrls.getInstance().setHost(com.spark.coinpaypddd.GlobalConstant.getHost() + "/" + com.spark.coinpaypddd.GlobalConstant.TYPE_UC);
                                LoginUrls.getInstance().setHostBusiness(com.spark.coinpaypddd.GlobalConstant.getHost());
                                LoginUrls.getInstance().setHostLogin(com.spark.coinpaypddd.GlobalConstant.getHostLogin());
                                AcUrls.getInstance().setHost(com.spark.coinpaypddd.GlobalConstant.getHostAC());
                                UcUrls.getInstance().setHost(com.spark.coinpaypddd.GlobalConstant.getHostUC());
                                OtcUrls.getInstance().setHost(com.spark.coinpaypddd.GlobalConstant.getHostOTC());
                                AgentUrls.getInstance().setHost(com.spark.coinpaypddd.GlobalConstant.getHostAgent());
                            } else {
                                String uc = SharedPreferencesUtil.getInstance(activity).getStringParam(SP_KEY_WRITE_API_UC);
                                String ac = SharedPreferencesUtil.getInstance(activity).getStringParam(SP_KEY_WRITE_API_AC);
                                String acp = SharedPreferencesUtil.getInstance(activity).getStringParam(SP_KEY_WRITE_API_ACP);

                                com.spark.coinpaypddd.GlobalConstant.isTest = true;
                                GuardService.isTest = true;
                                WebSocketService.isTest = true;

                                com.spark.coinpaypddd.GlobalConstant.CUR_HOST_UC = uc;
                                com.spark.coinpaypddd.GlobalConstant.CUR_HOST_AC = ac;
                                com.spark.coinpaypddd.GlobalConstant.CUR_HOST_ACP = acp;
                                WebSocketService.setHost(acp);
                                GuardService.setHost(acp);

                                LoginUrls.getInstance().setHost(com.spark.coinpaypddd.GlobalConstant.getHost() + "/" + com.spark.coinpaypddd.GlobalConstant.TYPE_UC);
                                LoginUrls.getInstance().setHostBusiness(com.spark.coinpaypddd.GlobalConstant.getHost());
                                LoginUrls.getInstance().setHostLogin(com.spark.coinpaypddd.GlobalConstant.getHostLogin());
                                AcUrls.getInstance().setHost(com.spark.coinpaypddd.GlobalConstant.getHostAC());
                                UcUrls.getInstance().setHost(com.spark.coinpaypddd.GlobalConstant.getHostUC());
                                OtcUrls.getInstance().setHost(com.spark.coinpaypddd.GlobalConstant.getHostOTC());
                                AgentUrls.getInstance().setHost(com.spark.coinpaypddd.GlobalConstant.getHostAgent());
                            }
                            showActivity(MainActivity.class, null);
                        }
                    } else {
                        showActivity(LoginActivity.class, null);
                    }
//                    }
                    finish();
                }
                n--;
            }
        }, 100, 800);
    }


    @Override
    public void onBackPressed() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        super.onBackPressed();
    }

    /**
     * 修改每次点击桌面图标都会重新启动应用的Bug
     */
    private void isRoot() {
        if (!isTaskRoot()) {
            Intent intent = getIntent();
            if (intent != null) {
                String action = intent.getAction();
                if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && Intent.ACTION_MAIN.equals(action)) {
                    finish();
                }
            }
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //getBase();
        }
    };

    public void getBase() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> params = new HashMap<>();
                SendRemoteDataUtil.getInstance().doStringGet(baseURL, params, new SendRemoteDataUtil.DataCallback() {
                    @Override
                    public void onDataLoaded(Object obj) {
                        try {
                            SystemEntity entity = new Gson().fromJson(obj.toString(), SystemEntity.class);
                            MyApplication.getMyApplication().setSystemEntity(entity);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onDataNotAvailable(HttpErrorEntity httpErrorEntity) {
                    }
                });
            }
        }).start();
    }
}
