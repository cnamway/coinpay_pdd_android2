package com.spark.coinpaypddd.main;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.KeyguardManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spark.coinpaypddd.LoginStatus;
import com.spark.coinpaypddd.MyApplication;
import com.spark.coinpaypddd.R;
import com.spark.coinpaypddd.adapter.MainOrderAdapter;
import com.spark.coinpaypddd.entity.PayData;
import com.spark.coinpaypddd.entity.SystemEntity;
import com.spark.coinpaypddd.entity.VisionEntity;
import com.spark.coinpaypddd.event.CheckLoginSuccessEvent;
import com.spark.coinpaypddd.event.HasPayEvent;
import com.spark.coinpaypddd.event.LoginoutEvent;
import com.spark.coinpaypddd.event.MainTimeDownFinishEvent;
import com.spark.coinpaypddd.login.LoginActivity;
import com.spark.coinpaypddd.main.buy.BuyActivity;
import com.spark.coinpaypddd.main.order_detail.OrderDetailActivity;
import com.spark.coinpaypddd.my.MyActivity;
import com.spark.coinpaypddd.my.assets.extract.ExtractActivity;
import com.spark.coinpaypddd.my.assets.recharge.RechargeActivity;
import com.spark.coinpaypddd.my.assets.record.MyAssetTradeRecordActivity;
import com.spark.coinpaypddd.my.order.MyOrderActivity;
import com.spark.coinpaypddd.view.AlertIosDialog;
import com.spark.coinpaypddd.view.AppVersionDialog;
import com.spark.library.acp.model.AcceptMerchantFrontVo;
import com.spark.library.acp.model.AcceptMerchantTrade;
import com.spark.library.acp.model.MessageResult;
import com.spark.library.acp.model.MessageResultAcceptMerchantTrade;
import com.spark.library.acp.model.MessageResultPageOrderInTransitVo;
import com.spark.library.acp.model.OrderInTransitVo;
import com.spark.library.acp.model.PageOrderInTransitVo;
import com.spark.moduleassets.entity.Wallet;
import com.spark.modulebase.BaseApplication;
import com.spark.modulebase.base.BaseConstant;
import com.spark.modulebase.entity.HttpErrorEntity;
import com.spark.modulebase.entity.LoadExceptionEvent;
import com.spark.modulebase.entity.User;
import com.spark.modulebase.event.CheckLoginEvent;
import com.spark.modulebase.event.LoginoutWithoutApiEvent;
import com.spark.modulebase.okhttp.FileCallback;
import com.spark.modulebase.okhttp.OkhttpUtils;
import com.spark.modulebase.utils.ActivityManage;
import com.spark.modulebase.utils.AppUtils;
import com.spark.modulebase.utils.DateUtils;
import com.spark.modulebase.utils.FileUtils;
import com.spark.modulebase.utils.LogUtils;
import com.spark.modulebase.utils.MathUtils;
import com.spark.modulebase.utils.NetworkUtil;
import com.spark.modulebase.utils.SharedPreferencesUtil;
import com.spark.modulebase.utils.StringUtils;
import com.spark.modulebase.utils.ToastUtils;
import com.spark.modulelogin.entity.CasLoginEntity;
import com.spark.moduleotc.entity.AcceptMerchantInfoEntity;
import com.spark.moduleotc.entity.AcceptanceMerchantListEntity;
import com.spark.netty_library.CMD;
import com.spark.netty_library.ConnectCloseEvent;
import com.spark.netty_library.GuardService;
import com.spark.netty_library.JobWakeUpService;
import com.spark.netty_library.ServiceOpenSuccessEvent;
import com.spark.netty_library.ServiceTypeEvent;
import com.spark.netty_library.WebSocketService;
import com.spark.netty_library.listener.SendMsgListener;
import com.spark.netty_library.message.SocketMessage;
import com.spark.netty_library.message.SocketResponse;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import okhttp3.Request;

import static com.spark.coinpaypddd.GlobalConstant.TYPE_AC;
import static com.spark.coinpaypddd.GlobalConstant.TYPE_OTC;
import static com.spark.coinpaypddd.GlobalConstant.TYPE_UC;
import static com.spark.moduleassets.AssetsConstants.ACP;
import static com.spark.moduleassets.AssetsConstants.COMMON;
import static com.spark.modulebase.base.BaseConstant.SUCCESS_CODE;
import static com.spark.modulebase.utils.SharedPreferencesUtil.SP_KEY_APP_NAME;
import static com.spark.modulebase.utils.SharedPreferencesUtil.SP_KEY_SHOW;
import static com.spark.modulebase.utils.SharedPreferencesUtil.SP_KEY_VOICE;
import static com.spark.modulebase.utils.SharedPreferencesUtil.SP_PAY_PASS;

/**
 * 主页面
 */
public class MainActivity extends OrderActivity implements MainContract.MainView {

    @BindView(R.id.ivMy)
    ImageView ivMy;
    @BindView(R.id.ivOverTimeOrder)
    ImageView ivOverTimeOrder;
    @BindView(R.id.ivBuy)
    ImageView ivBuy;
    @BindView(R.id.tvPhone)
    TextView tvPhone;
    @BindView(R.id.ivAssetEyeTrade)
    ImageView ivAssetEyeTrade;
    @BindView(R.id.tvAssetAmountTrade)
    TextView tvAssetAmountTrade;
    @BindView(R.id.tvAssetMoneyTrade)
    TextView tvAssetMoneyTrade;
    @BindView(R.id.tvAssetMoneyTradeBail)
    TextView tvAssetMoneyTradeBail;
    @BindView(R.id.tvAssetMoneyTradeDj)
    TextView tvAssetMoneyTradeDj;
    @BindView(R.id.tvAssetCommission)
    TextView tvAssetCommission;
    @BindView(R.id.tvMoneyTypeTag)
    TextView tvMoneyTypeTag;
    @BindView(R.id.tvAssetCommissionY)
    TextView tvAssetCommissionY;
    @BindView(R.id.tvAssetCommissionUn)
    TextView tvAssetCommissionUn;
    @BindView(R.id.tvOrderCount)
    TextView tvOrderCount;
    @BindView(R.id.tvOrderCountDes)
    TextView tvOrderCountDes;
    @BindView(R.id.tvCommission)
    TextView tvCommission;
    @BindView(R.id.tvFinishRate)
    TextView tvFinishRate;
    @BindView(R.id.tvOrderSpeed)
    TextView tvOrderSpeed;
    @BindView(R.id.tvStartOrder)
    TextView tvStartOrder;
    @BindView(R.id.tvAdd)
    TextView tvAdd;
    @BindView(R.id.tvOrderingOrNot)
    TextView tvOrderingOrNot;
    @BindView(R.id.ivType)
    ImageView ivType;
    @BindView(R.id.tvTypeCNY)
    TextView tvTypeCNY;
    @BindView(R.id.tvCount)
    TextView tvCount;
    @BindView(R.id.tvOrderCreate)
    TextView tvOrderCreate;
    @BindView(R.id.tvOrderCreateTime)
    TextView tvOrderCreateTime;
    @BindView(R.id.ivProcess)
    ImageView ivProcess;
    @BindView(R.id.viewProcess)
    View viewProcess;
    @BindView(R.id.tvPay)
    TextView tvPay;
    @BindView(R.id.tvPayTime)
    TextView tvPayTime;
    @BindView(R.id.tvRealease)
    TextView tvRealease;
    @BindView(R.id.tvRealeaseTime)
    TextView tvRealeaseTime;
    @BindView(R.id.llRelease)
    LinearLayout llRelease;
    @BindView(R.id.tvDirection)
    TextView tvDirection;
    @BindView(R.id.tvPayCount)
    TextView tvPayCount;
    @BindView(R.id.tvRate)
    TextView tvRate;
    @BindView(R.id.tvPayRefer)
    TextView tvPayRefer;
    @BindView(R.id.ivPayReferCopy)
    ImageView ivPayReferCopy;
    @BindView(R.id.ivAuthStatusRealName)
    ImageView ivAuthStatusRealName;
    @BindView(R.id.tvAuthStatusRealName)
    TextView tvAuthStatusRealName;
    @BindView(R.id.ivAuthStatusRealName2)
    ImageView ivAuthStatusRealName2;
    @BindView(R.id.tvAuthStatusRealNameSet)
    TextView tvAuthStatusRealNameSet;
    @BindView(R.id.ivAuthStatusPass)
    ImageView ivAuthStatusPass;
    @BindView(R.id.tvAuthStatusPass)
    TextView tvAuthStatusPass;
    @BindView(R.id.ivAuthStatusPass2)
    ImageView ivAuthStatusPass2;
    @BindView(R.id.tvAuthStatusPassSet)
    TextView tvAuthStatusPassSet;
    @BindView(R.id.ivAuthStatusType)
    ImageView ivAuthStatusType;
    @BindView(R.id.tvAuthStatusType)
    TextView tvAuthStatusType;
    @BindView(R.id.ivAuthStatusType2)
    ImageView ivAuthStatusType2;
    @BindView(R.id.tvAuthStatusTypeSet)
    TextView tvAuthStatusTypeSet;
    @BindView(R.id.ivAuthStatusAcceptanceMerchant)
    ImageView ivAuthStatusAcceptanceMerchant;
    @BindView(R.id.tvAuthStatusAcceptanceMerchant)
    TextView tvAuthStatusAcceptanceMerchant;
    @BindView(R.id.ivAuthStatusAcceptanceMerchant2)
    ImageView ivAuthStatusAcceptanceMerchant2;
    @BindView(R.id.tvAuthStatusAcceptanceMerchantSet)
    TextView tvAuthStatusAcceptanceMerchantSet;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tvType)
    TextView tvType;
    @BindView(R.id.tvStatus)
    TextView tvStatus;
    @BindView(R.id.llAccectOrder)
    LinearLayout llAccectOrder;
    @BindView(R.id.ll1)
    LinearLayout ll1;
    @BindView(R.id.ll2)
    LinearLayout ll2;
    @BindView(R.id.ll3)
    LinearLayout ll3;
    @BindView(R.id.ll4)
    LinearLayout ll4;
    @BindView(R.id.ll5)
    LinearLayout ll5;
    @BindView(R.id.ll6)
    LinearLayout ll6;
    @BindView(R.id.ll7)
    LinearLayout ll7;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    private long lastPressTime;//首页二次返回时间
    private boolean isStart = false;//是否开始接单：true开始接单  false停止接单
    private boolean isHas = false;//是否有订单：true有订单  false没有订单
    private boolean isShort = false;//账户余额是否不足：true不足  false正常
    private MainPresenterImpl presenter;
    private LoginStatus loginStatus;
    private String type;//接单类型： 兑入 兑出 全部
    private int takingType = 1;//接单类型： 1兑入 2兑出 3全部
    private boolean isShowLoading = true;
    private int statusRealName = 0;//实名认证状态:0-未认证 1待审核 2-审核不通过  3-已认证
    private MediaPlayer mediaPlayer;//播放音频
    //防止多次跳转401监听
    private boolean isTokenUnUsed = false;
    private boolean isHasPay = false;//由长连接指令20004获取的支付状态

    //倒计时
    private long randomTime;//订单剩余时间（毫秒）
    private int position = 1;//1待付款 2待放行
    //websocket
    private WebSocketService webSocketService;//接单webSocketService
    private GuardService guardService;//接单webSocketService
    private ServiceConnection mConnection;
    private ServiceConnection mConnection2;
    private List<PayData> payDatas;
    private PowerManager.WakeLock mWakeLock = null;//唤醒锁
    private KeyguardManager.KeyguardLock keyguardLock = null;//禁止锁屏
    private boolean isWebSocketService = true;
    private String orderId;//取消订单、放行、支付使用
    private AlertIosDialog alertIosDialog;
    private ProgressDialog progressDialog;
    private AppVersionDialog appVersionDialog;
    private boolean isCheckVersion = false;//首次检测版本更新
    private boolean isNeedReconnect = false;//是否需要重连websocket
    private boolean isGoToLogin = false;//保证登录界面只跳转一次
    private MainOrderAdapter adapter;
    private List<OrderInTransitVo> mDatas = new ArrayList<>();
    private int tick = 1;//刷新列表间隔
    private int tick_flush = 15;//列表 每隔15秒加载一次数据
    private boolean isNeedReconnect504 = false;//是否504
    private boolean isFirst = false;//是否手动开始接单
    private static final short ORDER_CHANNEL = 0;
    private User currentUser;

    private ArrayList<Wallet> walletsBase = new ArrayList<>();
    private ArrayList<Wallet> walletsTrade = new ArrayList<>();
    private double sumAmount = 0;//账户总数量
    private double sumAmountDj = 0;//账户总冻结数量
    private double sumTrade = 0;//账户金额
    private double sumCny = 0;//保证金金额
    private ArrayList<Wallet> wallets = new ArrayList<>();
    private String coinId = "";

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1://刷新首页数据
                    getMainInfo(false);
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initReceiver();
    }

    /**
     * 注册网络监听的广播
     */
    private void initReceiver() {
        IntentFilter timeFilter = new IntentFilter();
        timeFilter.addAction("android.net.ethernet.ETHERNET_STATE_CHANGED");
        timeFilter.addAction("android.net.ethernet.STATE_CHANGE");
        timeFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        timeFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        timeFilter.addAction("android.net.wifi.STATE_CHANGE");
        timeFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        registerReceiver(netReceiver, timeFilter);
    }

    @Override
    public void onBackPressed() {
        long now = System.currentTimeMillis();
        if (lastPressTime == 0 || now - lastPressTime > 2 * 1000) {
            ToastUtils.showToast(activity, getString(R.string.str_exit_again));
            lastPressTime = now;
        } else if (now - lastPressTime < 2 * 1000) {
            if (isWebSocketService) {
                if (webSocketService != null)
                    webSocketService.closeWebsocket(false);
            } else {
                if (guardService != null)
                    guardService.closeWebsocket();
            }
            super.onBackPressed();
        }
    }

    @Override
    protected void initRv() {
        super.initRv();
        LinearLayoutManager manager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(manager);
        adapter = new MainOrderAdapter(R.layout.view_order_detail, mDatas);
        adapter.bindToRecyclerView(recyclerView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (mConnection != null) {
                if (isWebSocketService)
                    unbindService(mConnection);
            }
            if (mConnection2 != null) {
                if (!isWebSocketService)
                    unbindService(mConnection2);
            }
            if (mediaPlayer != null) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                mediaPlayer.release();//释放资源
            }
            mTimeHandler.removeCallbacks(runnable);
            presenter.destory();
            releaseWakeLock();
            releaseKeyguardLock();

            if (netReceiver != null) {
                unregisterReceiver(netReceiver);
                netReceiver = null;
            }
            stopFlush();
        } catch (Exception e) {
        }
    }

    @Override
    protected void initView(Bundle saveInstance) {
        super.initView(saveInstance);
        String appName = SharedPreferencesUtil.getInstance(activity).getStringParam(SP_KEY_APP_NAME);
        tvTitle.setText(appName);
        SystemEntity entity = MyApplication.getMyApplication().getSystemEntity();
        if (entity != null) {
            if (entity.isEnableOtcBuy()) {
                ivBuy.setVisibility(View.INVISIBLE);
                ivMessage.setVisibility(View.GONE);
            } else {
                ivBuy.setVisibility(View.GONE);
                ivMessage.setVisibility(View.INVISIBLE);
            }
        } else {
            ivBuy.setVisibility(View.INVISIBLE);
            ivMessage.setVisibility(View.GONE);
        }
        currentUser = MyApplication.getAppContext().getCurrentUser();
        if (currentUser != null) {
            if (StringUtils.isNotEmpty(currentUser.getRealName())) {
                tvTitle.setText(currentUser.getRealName());
            }
            if (StringUtils.isNotEmpty(currentUser.getMobilePhone())) {
                String phone = currentUser.getMobilePhone();
                if (StringUtils.isNotEmpty(phone)) {
                    if (phone.startsWith("86")) {
                        phone = phone.substring(2, phone.length());
                    }
                    phone = AppUtils.addStar(phone);
                    tvPhone.setText("Hi, " + phone);
                }
            }
        }
    }

    @Override
    protected void initData() {
        super.initData();
        bindSocketService();
        presenter = new MainPresenterImpl(this);
        initProgressDialog();
    }

    /**
     * 打开raw目录下的音乐mp3文件
     */
    private void openRawMusic(int type) {
        try {
            if (SharedPreferencesUtil.getInstance(activity).contains(SP_KEY_VOICE)) {
                if (!SharedPreferencesUtil.getInstance(activity).getBooleanParam(SP_KEY_VOICE)) {
                    return;
                }
            }
            if (mediaPlayer != null) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                mediaPlayer.release();
                mediaPlayer = null;
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setLooping(false);
            } else {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setLooping(false);
            }
            switch (type) {
                case 1:
                    mediaPlayer = MediaPlayer.create(this, R.raw.start);
                    break;
                case 2:
                    mediaPlayer = MediaPlayer.create(this, R.raw.stop);
                    break;
                case 3:
                    mediaPlayer = MediaPlayer.create(this, R.raw.new_2);
                    break;
                case 4:
                    mediaPlayer = MediaPlayer.create(this, R.raw.pay);
                    break;
                case 5:
                    mediaPlayer = MediaPlayer.create(this, R.raw.paying);
                    break;
                case 6:
                    mediaPlayer = MediaPlayer.create(this, R.raw.auto_release_success);
                    break;
                case 7:
                    mediaPlayer = MediaPlayer.create(this, R.raw.auto_release_fail);
                    break;
                case 8:
                    mediaPlayer = MediaPlayer.create(this, R.raw.connect_close);
                    break;
            }
            mediaPlayer.start();
        } catch (Exception e) {
        }
    }

    /**
     * 加载数据
     */
    @Override
    protected void loadData() {
        super.loadData();
        loginStatus = MyApplication.getMyApplication().getLoginStatus();
        getMainInfo(true);
        mTimeHandler.sendEmptyMessageDelayed(3, 5000);
    }

    /**
     * 加载数据
     */
    private void getMainInfo(boolean isShow) {
        isShowLoading = isShow;
        //查询派单前实名认证、承兑商身份认证、设置资金密码、设置收款方式状态
        //presenter.findAuthenticationStatus();
        //查询交易账户
        presenter.getWallet(COMMON);
        //查询累计佣金、待结算佣金
        presenter.getTrade();
        //查询昨日佣金
        String tradingDay = DateUtils.getBackDate("yyyyMMdd", -1);
        presenter.getTradeYesterday(tradingDay);
        //获取个人等级---获取保证金
        presenter.getSelfLevelInfo();
        //查询今日接单数等数据
        presenter.findAcceptMerchantTrade();
    }

    /**
     * 查询超时单
     */
    private void getList(boolean isShow) {
        HashMap<String, String> map = new HashMap<>();
        map.put("pageNo", 1 + "");
        map.put("pageSize", 1 + "");
        map.put("status", "5");
        presenter.getOrder(isShow, map);
    }

    @Override
    public void getOrderSuccess(List<OrderInTransitVo> list) {
        if (list == null) return;
        if (list.size() > 0) {
            ivOverTimeOrder.setVisibility(View.INVISIBLE);
        } else {
            ivOverTimeOrder.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void showLoading() {
        if (!isShowLoading) {
            return;
        }
        if (randomTime < 0) {//倒计时结束时randomTime<0,此时不展示加载进度条
            return;
        }
        super.showLoading();
    }

    @OnClick({R.id.ivMy, R.id.tvStartOrder, R.id.ivAssetEyeTrade, R.id.ll1, R.id.ll2, R.id.ll3, R.id.ll4, R.id.ll5, R.id.ll6, R.id.ll7})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        Bundle bundle = null;
        switch (v.getId()) {
            case R.id.ivMy://我的
                showActivity(MyActivity.class, null);
                break;
            case R.id.tvStartOrder:
                if (!isStart) {//开始接单
                    //showOrderDialog();
                    isFirst = true;
                    type = getString(R.string.str_duiru);
                    takingType = 1;
                    startOrder();
                } else {//停止接单
                    stopOrder();
                }
                break;
            case R.id.ivAssetEyeTrade://账户-查看
                switchSee();
                break;
            case R.id.ll1://一键买币
                showActivity(BuyActivity.class, null, 1);
                break;
            case R.id.ll2://我的订单
                showActivity(MyOrderActivity.class, null, 1);
                break;
            case R.id.ll3://资金明细
                showActivity(MyAssetTradeRecordActivity.class, null, 1);
                break;
            case R.id.ll4://店铺列表

                break;
            case R.id.ll5://商品列表

                break;
            case R.id.ll6://充币
                bundle = new Bundle();
                bundle.putSerializable("wallets", walletsTrade);
                showActivity(RechargeActivity.class, bundle, 1);
                break;
            case R.id.ll7://提币
                bundle = new Bundle();
                bundle.putSerializable("wallets", walletsTrade);
                showActivity(ExtractActivity.class, bundle, 1);
                break;
        }
    }

    @Override
    protected void setListener() {
        super.setListener();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMainInfo(false);
            }
        });

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                orderInTransit = (OrderInTransitVo) adapter.getItem(position);
                if (orderInTransit != null) showActivity(OrderDetailActivity.class, null, 1);
            }
        });
    }

    /**
     * 开始接单-发送登录指令
     */
    private void startLogin() {
        String sid = SharedPreferencesUtil.getInstance(BaseApplication.getAppContext()).getOtcSid();
        HashMap<String, String> params = new HashMap<>();
        params.put("sid", sid);
        LogUtils.e("开始接单-发送登录指令sid==" + sid);
        String body = new Gson().toJson(params);
        if (isWebSocketService) {
            if (webSocketService != null)
                webSocketService.sendData(new SocketMessage(ORDER_CHANNEL, CMD.LOGIN, body.getBytes()), sendMsgListener);
        } else {
            if (guardService != null)
                guardService.sendData(new SocketMessage(ORDER_CHANNEL, CMD.LOGIN, body.getBytes()), sendMsgListener);
        }

    }

    /**
     * 开始接单-发送连接指令
     */
    private void startConnect() {
        if (isWebSocketService) {
            if (webSocketService != null)
                webSocketService.sendData(new SocketMessage(ORDER_CHANNEL, CMD.CONNECT, null), sendMsgListener);
        } else {
            if (guardService != null)
                guardService.sendData(new SocketMessage(ORDER_CHANNEL, CMD.CONNECT, null), sendMsgListener);
        }
    }

    /**
     * 开始接单-发送接单指令
     */
    private void startOrder() {
        HashMap<String, Integer> params = new HashMap<>();
        params.put("takingType", takingType);
        String body = new Gson().toJson(params);
        if (isWebSocketService) {
            if (webSocketService != null)
                webSocketService.sendData(new SocketMessage(ORDER_CHANNEL, CMD.START_ORDER, body.getBytes()), sendMsgListener);
        } else {
            if (guardService != null)
                guardService.sendData(new SocketMessage(ORDER_CHANNEL, CMD.START_ORDER, body.getBytes()), sendMsgListener);
        }
    }

    /**
     * 停止接单-发送停止接单指令
     */
    private void stopOrder() {
        if (isWebSocketService) {
            if (webSocketService != null)
                webSocketService.sendData(new SocketMessage(ORDER_CHANNEL, CMD.STOP_ORDER, null), sendMsgListener);
        } else {
            if (guardService != null)
                guardService.sendData(new SocketMessage(ORDER_CHANNEL, CMD.STOP_ORDER, null), sendMsgListener);
        }
    }

    /**
     * 选择接单类型
     */
    private void showOrderDialog() {
        final String[] stringItems = {getString(R.string.str_duiru), getString(R.string.str_duichu), getString(R.string.str_all)};
        final ActionSheetDialog dialog = new ActionSheetDialog(activity, stringItems, null);
        dialog.isTitleShow(false).itemTextColor(getResources().getColor(R.color.font_main_title))
                .cancelText(getResources().getColor(R.color.font_main_content))
                .cancelText(getResources().getString(R.string.str_cancel)).show();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.dismiss();
                isFirst = true;
                type = stringItems[position];
                takingType = position + 1;
                startOrder();
            }
        });
    }

    /**
     * 修改状态
     */
    private void changeStatus(boolean start) {
        isStart = start;
        if (tvStartOrder != null) {
            tvStartOrder.setText(isStart ? R.string.str_stop_order : R.string.str_start_order);
        }
        if (tvStatus != null) {
            tvStatus.setText(isStart ? R.string.str_ordering : R.string.str_not_start_order);
        }
        if (tvType != null) {
            if (isStart) {
                tvType.setText(type);
            } else {
                tvType.setText(R.string.str_duiru);
            }
        }
    }

    /**
     * 检测登录状态
     */
    public void onEvent(CheckLoginEvent event) {
        if (event.type.contains(TYPE_OTC)) {
            presenter.checkBusinessLogin(TYPE_OTC);
        } else if (event.type.contains(TYPE_UC)) {
            presenter.checkBusinessLogin(TYPE_UC);
        } else if (event.type.contains(TYPE_AC)) {
            presenter.checkBusinessLogin(TYPE_AC);
        }
//        presenter.checkBusinessLogin(TYPE_OTC);
//        presenter.checkBusinessLogin(TYPE_UC);
//        presenter.checkBusinessLogin(TYPE_AC);
    }

    @Override
    public void checkBusinessLoginSuccess(CasLoginEntity casLoginEntity) {
        if (casLoginEntity != null) {
            String type = casLoginEntity.getType();
            if (!casLoginEntity.isLogin()) {
                String gtc = SharedPreferencesUtil.getInstance(BaseApplication.getAppContext()).getTgt();
                presenter.doLoginBusiness(gtc, type);
            } else {
                setLogin(type);
                if (isNeedReconnect504) {
                    isNeedReconnect504 = false;
                    saveOtcSid();
                }
            }
        }
    }

    private void saveOtcSid() {
        CookieManager cookieManager = MyApplication.getAppContext().getCookieManager();
        CookieStore cookieStore = cookieManager.getCookieStore();
        List<HttpCookie> cookies = cookieStore.getCookies();
        for (HttpCookie cookie : cookies) {
            if ("otcsid".equals(cookie.getName())) {
                LogUtils.e("saveOtcSid==" + cookie.getValue());
                SharedPreferencesUtil.getInstance(MyApplication.getAppContext()).setOtcSid(BaseApplication.getAppContext(), cookie.getValue());
                //开始接单-发送登录指令
                startLogin();
                break;
            }
            /*if ("acsid".equals(cookie.getName())) {
                SharedPreferencesUtil.getInstance(MyApplication.getAppContext()).setAcSid(BaseApplication.getAppContext(), cookie.getValue());
                break;
            }
            if ("ucsid".equals(cookie.getName())) {
                SharedPreferencesUtil.getInstance(MyApplication.getAppContext()).setUcSid(BaseApplication.getAppContext(), cookie.getValue());
                break;
            }*/
        }
    }

    @Override
    public void doLoginBusinessSuccess(String type) {
        if (isNeedReconnect504) {
            isNeedReconnect504 = false;
            saveOtcSid();
        }
        setLogin(type);
        getMainInfo(false);
        EventBus.getDefault().post(new CheckLoginSuccessEvent());
    }

    /**
     * 解析今日接单数等数据
     *
     * @param response
     */
    @Override
    public void findAcceptMerchantTradeSuccess(MessageResultAcceptMerchantTrade response) {
        mTimeHandler.sendEmptyMessage(5);
        //解析今日接单数等数据
        AcceptMerchantTrade trade = response.getData();
        initMerchantTradeInfo(trade);
    }

    /**
     * 设置今日接单数等信息
     *
     * @param trade
     */
    private void initMerchantTradeInfo(AcceptMerchantTrade trade) {
        if (trade != null) {
            int orderCount = trade.getDaysBuyOrder() + trade.getDaysSellOrder();
            tvOrderCount.setText(String.valueOf(orderCount));

//            double totalBuyReward = 0;
//            double totalSellReward = 0;
//            if (trade.getDaysBuyReward() != null) {
//                totalBuyReward = trade.getDaysBuyReward().doubleValue();
//            }
//            if (trade.getDaysSellReward() != null) {
//                totalSellReward = trade.getDaysSellReward().doubleValue();
//            }
            String rewardMoney = (trade.getDaysBuyReward().add(trade.getDaysSellReward())).toPlainString();
            String reward = MathUtils.subZeroAndDot(MathUtils.getRundNumber2(rewardMoney, BaseConstant.MONEY_FORMAT, null));
            tvCommission.setText(reward);

            int successCount = trade.getTotalSuccessBuyOrder() + trade.getTotalSuccessSellOrder();
            int totalCount = trade.getTotalBuyOrder() + trade.getTotalSellOrder();
            String rate = AppUtils.getRate(successCount, totalCount);
            tvFinishRate.setText(rate);

            int avgTime = trade.getAvgPayTime() * trade.getTotalBuyOrder() + trade.getAvgReleaseTime() * trade.getTotalSellOrder();
            String speed = AppUtils.getSpeed(avgTime, totalCount);
            tvOrderSpeed.setText(speed);
        }
    }

    /**
     * 查询在途订单成功
     */
    @Override
    public void findOrderInTransitSuccess(MessageResultPageOrderInTransitVo response) {
        if (response != null) {
            PageOrderInTransitVo data = response.getData();
            List<OrderInTransitVo> records = data.getRecords();
            if (records != null && records.size() > 0) {

                refreshLayout.setEnabled(true);
                refreshLayout.setRefreshing(false);
                adapter.setEnableLoadMore(true);
                adapter.loadMoreComplete();
                mDatas.clear();
                mDatas.addAll(records);
                adapter.notifyDataSetChanged();
                adapter.disableLoadMoreIfNotFullPage();

                /*orderInTransit = records.get(0);
                //设置倒计时
                mTimeHandler.removeCallbacks(runnable);
                initOrderContent(orderInTransit);
                */
                isHas = true;
                if (isHasPay) {
                    openRawMusic(4);
                    EventBus.getDefault().post(new HasPayEvent());
                    isHasPay = false;
                } else {
                    /*if (randomTime > 0) {
                        openRawMusic(3);
                    }*/
                    //openRawMusic(3);
                }
                //查询今日接单数等数据
                presenter.findAcceptMerchantTrade();
                stopFlush();
                handler_timeCurrent.sendEmptyMessageDelayed(0, tick * 1000);
            } else {
                isHas = false;
                //没有订单不在刷新时间显示
                mTimeHandler.removeMessages(2);
                stopFlush();
            }
        }
        //设置界面显示状态
        setVisiableStatus();
        mTimeHandler.sendEmptyMessage(5);
    }

    private void stopRefreshing() {
        if (refreshLayout != null) {
            refreshLayout.setEnabled(true);
            refreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void dealError(VolleyError volleyError) {
        super.dealError(volleyError);
        mTimeHandler.sendEmptyMessage(5);
        if (isTokenUnUsed) {
            isTokenUnUsed = false;
        }
    }

    @Override
    public void dealError(HttpErrorEntity httpErrorEntity) {
        super.dealError(httpErrorEntity);
        mTimeHandler.sendEmptyMessage(5);
        if (isTokenUnUsed) {
            isTokenUnUsed = false;
        }
    }

    /**
     * 设置登录状态
     *
     * @param type
     */
    private void setLogin(String type) {
        if (StringUtils.isNotEmpty(type)) {
            switch (type) {
                case TYPE_AC:
                    loginStatus.setAcLogin(true);
                    break;
                case TYPE_UC:
                    loginStatus.setUcLogin(true);
                    break;
                case TYPE_OTC:
                    loginStatus.setOtcLogin(true);
                    break;
            }
        }
    }

    /**
     * TCP连接socket监听
     */
    SendMsgListener sendMsgListener = new SendMsgListener() {

        @Override
        public void onMessageResponse(SocketResponse socketResponse) {
            Message message = Message.obtain();
            String response = socketResponse.getResponse();
            int cmd = socketResponse.getCmd();
            try {
                JSONObject jsonObject = new JSONObject(response);
                int code = jsonObject.optInt("code");
                if (code == SUCCESS_CODE) {
                    switch (cmd) {
                        case CMD.LOGIN:
                            isNeedReconnect504 = false;
                            startConnect();
                            break;
                        case CMD.CONNECT:
                            LogUtils.e("连接11003建立成功isStart==" + isStart);
                            if (isStart) {
                                isFirst = false;
                                startOrder();
                            }
                            break;
                        case CMD.START_ORDER://收到开始接单指令
                            message.what = CMD.START_ORDER;
                            handler.sendMessage(message);
                            break;
                        case CMD.STOP_ORDER:
                            message.what = CMD.STOP_ORDER;
                            handler.sendMessage(message);
                            break;
                    }
                } else if (code == 30516) {
                    isShort = true;
                    setVisiableStatus();
                } else if (code == 30568) {
                    ToastUtils.showToast("账户被禁用");
                } else if (code == 504) {
                    //{"code":504,"message":"用户会话失效，请重新登录！","responseString":"504~用户会话失效，请重新登录！"}
                    //{"code":504,"message":"NOLOGIN_ERROR","responseString":"504~NOLOGIN_ERROR"}
                    String msg = jsonObject.optString("message");
                    if (StringUtils.isNotEmpty(msg)) {
                        if (msg.contains("NOLOGIN_ERROR")) {
                            if (!isTokenUnUsed) {
                                /*LogUtils.e("504==退出登录=====");
                                isTokenUnUsed = true;
                                presenter.loginOut();*/
                                if (!isNeedReconnect504) {
                                    isNeedReconnect504 = true;
                                    presenter.checkBusinessLogin(TYPE_OTC);
                                }
                            }
//                            isNeedReconnect = true;
//                            reconnect();

                        } else {
                            startLogin();
                        }
                    }
                } else {
                    switch (cmd) {
                        case CMD.ACCEPT_ORDER://订单承接通知,收到该指令，说明承兑商有订单匹配，此时需要查询在途订单
                            int direction = jsonObject.optInt("direction");
                            message.what = CMD.ACCEPT_ORDER;
                            handler.sendMessage(message);
                            break;
                        case CMD.ORDER_STATUS://订单状态变更
                            int status = jsonObject.optInt("status");
                            if (status == 2) {//为2时，订单由  待支付 到 已支付状态
                                isHasPay = true;
                                message.what = CMD.ORDER_STATUS;
                                handler.sendMessage(message);
                            } else if (status == 11) {//为11时，无法支付取消
                                isHasPay = false;
                                message.what = 205;
                                String msg = jsonObject.optString("message");
                                if (StringUtils.isNotEmpty(msg)) {
                                    message.obj = msg;
                                    handler.sendMessage(message);
                                }
                            } else if (status == 8) {//为8时，兑入订单客户取消
                                isHasPay = false;
                                message.what = 206;
                                String msg = jsonObject.optString("message");
                                if (StringUtils.isNotEmpty(msg)) {
                                    message.obj = msg;
                                    handler.sendMessage(message);
                                }
                            } else if (status == 12) {//为12时，正在打款
                                isHasPay = false;
                                message.what = 207;
                                handler.sendMessage(message);
                            } else {
                                isHasPay = false;
                                message.what = CMD.ORDER_STATUS;
                                handler.sendMessage(message);
                            }
                            break;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                LogUtils.e("socket监听===========================================================解析异常");
            }
        }

        @Override
        public void error() {
            LogUtils.e("socket监听===========================================================发送消息异常");
            /*if (!isTokenUnUsed) {
                //ToastUtils.showToast("发送消息异常===退出登录");
                isTokenUnUsed = true;
                presenter.loginOut();
            }*/

            isNeedReconnect = true;
            reconnect();

        }
    };

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case CMD.START_ORDER://开始接单
                    ToastUtils.showToast(MainActivity.this, "开始接单");
                    changeStatus(true);
                    //订单承接通知,收到该指令，说明承兑商有订单匹配，此时需要查询在途订单
                    getOrder();
                    if (isFirst) {
                        openRawMusic(1);
                    }
                    break;
                case CMD.STOP_ORDER://停止接单
                    ToastUtils.showToast(MainActivity.this, "停止接单");
                    changeStatus(false);
                    openRawMusic(2);
                    //设置界面显示状态
                    setVisiableStatus();
                    break;
                case CMD.ACCEPT_ORDER: //订单承接通知,收到该指令，说明承兑商有订单匹配，此时需要查询在途订单
                    getOrder();
                    openRawMusic(3);
                    break;
                case CMD.ORDER_STATUS: //订单状态变更
                    getOrder();
                    break;
                case 205://为11时，无法支付取消
                    String message = (String) msg.obj;
                    //type 1:状态为11 2：状态为8
                    showAlerDialog(message, 1);
                    break;
                case 206://为8时，兑入订单客户取消
                    //type 1:状态为11 2：状态为8
                    showAlerDialog("", 2);
                    break;
                case 207://为12时，正在打款
                    openRawMusic(5);
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    public void findAuthenticationStatusSuccess(String response) {
        mTimeHandler.sendEmptyMessage(5);
        //开始检查版本更新
        if (!isCheckVersion) {
            isCheckVersion = true;
            allowUnKnowSrc(activity);
            checkPermission();
        }
    }


    /**
     * 数据加载异常通知
     * 401登录无效监听
     *
     * @param event
     */
    public void onEvent(LoadExceptionEvent event) {
        if (!isTokenUnUsed) {
            LogUtils.e("MainActivity 401登录无效监听presenter.loginOut(),LoadExceptionEvent event==开始调用退出登录方法=======");
            isTokenUnUsed = true;
            presenter.loginOut();
        }
    }

    /**
     * type：1WebSocketService 2GuardService
     *
     * @param event
     */
    public void onEvent(ServiceOpenSuccessEvent event) {
        if (event.type == 1) {
            startLogin();
        }
        if (event.type == 2) {
            startLogin();
        }
    }

    /**
     * type：1WebSocketService 2GuardService
     *
     * @param event
     */
    public void onEvent(ServiceTypeEvent event) {
        if (event.type == 1) {
            isWebSocketService = true;
        }
        if (event.type == 2) {
            isWebSocketService = false;
        }
    }

    /**
     * type：1WebSocketService 2GuardService
     *
     * @param event
     */
    public void onEvent(ConnectCloseEvent event) {
        if (isStart) {
            LogUtils.e("播放断线语音222222222222222222222222222222222222222");
            //openRawMusic(8);
        }
        isNeedReconnect = true;
    }

    @Override
    public void loginOutSuccess(String response) {
        LogUtils.e("退出登录，loginOutSuccess====webSocketService==" + webSocketService);
        if (response != null) {
            if (isWebSocketService) {
                if (webSocketService != null)
                    webSocketService.closeWebsocket(false);
            } else {
                if (guardService != null)
                    guardService.closeWebsocket();
            }

            goToLogin();
        }
    }

    /**
     * 退出登录-不调用退出接口
     *
     * @param event
     */
    public void onEvent(LoginoutWithoutApiEvent event) {
        LogUtils.e("退出登录，退出登录-不调用退出接口====webSocketService==" + webSocketService);
        if (isWebSocketService) {
            if (webSocketService != null)
                webSocketService.closeWebsocket(false);
        } else {
            if (guardService != null)
                guardService.closeWebsocket();
        }
        goToLogin();
    }

    private void goToLogin() {
        if (!isGoToLogin) {
            LogUtils.e("退出登录，跳转到登录界面LoginActivity================================");
            isGoToLogin = true;
            Bundle bundle = new Bundle();
            if (currentUser == null) return;
            String mobilePhone = currentUser.getMobilePhone();
            String userName = "";
            if (StringUtils.isNotEmpty(mobilePhone)) {
                userName = mobilePhone;
                if (mobilePhone.startsWith("86")) {
                    userName = mobilePhone.substring(2, mobilePhone.length());
                }
            }
            bundle.putString("username", userName);
            MyApplication.getAppContext().deleteCurrentUser();
            MyApplication.getAppContext().getCookieManager().getCookieStore().removeAll();
            isTokenUnUsed = false;
            ActivityManage.finishAll();
            showActivity(LoginActivity.class, bundle);
        }
    }

    /**
     * 退出登录 主动断开长连接
     *
     * @param event
     */
    public void onEvent(LoginoutEvent event) {
        if (isWebSocketService) {
            if (webSocketService != null)
                webSocketService.closeWebsocket(false);
        } else {
            if (guardService != null)
                guardService.closeWebsocket();
        }
    }

    /**
     * 设置界面显示状态
     */
    private void setVisiableStatus() {
        //isShort 账户余额是否不足：true不足  false正常
        if (isShort) {
        } else {
            //isHas 是否有订单：true有订单  false没有订单
            if (isHas) {
            } else {
                //isStart 是否开始接单：true开始接单  false停止接单
                if (isStart) {
                } else {
                }
            }
        }
    }

    Handler mTimeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1://刷新剩余时间
                    if (randomTime > 0) {
                        randomTime -= 1000;
                        setTime(randomTime);
                    } else {
                        setTime(0);
                        mTimeHandler.removeCallbacks(runnable);
                        getOrder();
                    }
                    break;
                case 2://10秒刷新一次
                    LogUtils.e("刷新在途订单时间:" + DateUtils.getFormatTime(DateUtils.DATE_FORMAT_1, new Date()));
                    presenter.findOrderInTransit();
                    break;
                case 3:
                    acquireWakeLock();
                    disableKeyguard();
                    setWifiDormancy();
                    testService();
                    break;
                case 4:
                    startLogin();
                    break;
                case 5:
                    stopRefreshing();
                    break;
            }
        }
    };

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            mTimeHandler.postDelayed(runnable, 1000);
            Message message = new Message();
            message.what = 1;
            mTimeHandler.sendMessage(message);
        }
    };

    /**
     * 设置时间
     *
     * @param randomTime
     */
    private void setTime(long randomTime) {
        if (position == 1) {//1待付款 2待放行
            if (tvPayTime != null)
                tvPayTime.setText(DateUtils.getTimeDown(randomTime, DateUtils.DATE_FORMAT_2, "", DateUtils.DATE_FORMAT_3));
        } else if (position == 2) {
            if (tvRealeaseTime != null)
                tvRealeaseTime.setText(DateUtils.getTimeDown(randomTime, DateUtils.DATE_FORMAT_2, "", DateUtils.DATE_FORMAT_4));
        } else if (position == 3) {
            if (tvPayTime != null)
                tvPayTime.setText(DateUtils.getTimeDown(randomTime, DateUtils.DATE_FORMAT_2, "", DateUtils.DATE_FORMAT_4));
        }

    }

    /**
     * 查询在途订单
     */
    private void getOrder() {
        if (randomTime < 0) {
            mTimeHandler.sendEmptyMessageDelayed(2, 10000);
        } else {
            presenter.findOrderInTransit();
        }
        stopFlush();
    }

    @Override
    public void getWalletSuccess(String type, List<Wallet> list) {
        if (type == COMMON) {
            this.walletsBase.clear();
            this.walletsBase.addAll(list);
            //查询交易账户
            presenter.getWallet(ACP);
        } else if (type == ACP) {
            this.wallets.clear();
            this.wallets.addAll(list);

            this.walletsTrade.clear();
            this.walletsTrade.addAll(list);
            calcuTotal(type, walletsTrade);
            for (Wallet wallet : walletsTrade) {
                for (Wallet entity : walletsBase) {
                    if (entity.getCoinId().equals(wallet.getCoinId())) {
                        wallet.setAddress(entity.getAddress());
                    }
                }
            }
        }
    }

    private void calcuTotal(String type, List<Wallet> coins) {
        sumTrade = 0;
        sumAmount = 0;
        sumAmountDj = 0;
        for (Wallet coin : coins) {
            coinId = coin.getCoinId();
            sumTrade += coin.getTotalLegalAssetBalance();
            sumAmount += coin.getTotalPlatformAssetBalance();
            sumAmountDj += coin.getFrozenBalance();
        }
        if (SharedPreferencesUtil.getInstance(activity).contains(SP_KEY_SHOW)) {
            if (SharedPreferencesUtil.getInstance(activity).getBooleanParam(SP_KEY_SHOW)) {
                tvAssetMoneyTrade.setText("≈" + MathUtils.subZeroAndDot(MathUtils.getRundNumberForShow(sumTrade, BaseConstant.MONEY_FORMAT_ASSET, null)) + " CNY");
                ivAssetEyeTrade.setImageResource(R.mipmap.icon_eye_open);
                tvAssetAmountTrade.setText(sumAmount + " " + coinId);
                tvAssetMoneyTradeDj.setText("冻结资产 " + sumAmountDj + " " + coinId);
            } else {
                tvAssetMoneyTrade.setText("********");
                ivAssetEyeTrade.setImageResource(R.mipmap.icon_eye_close);
                tvAssetAmountTrade.setText("********");
                tvAssetMoneyTradeDj.setText("********");
            }
        } else {
            tvAssetMoneyTrade.setText("≈" + MathUtils.subZeroAndDot(MathUtils.getRundNumberForShow(sumTrade, BaseConstant.MONEY_FORMAT_ASSET, null)) + " CNY");
            ivAssetEyeTrade.setImageResource(R.mipmap.icon_eye_open);
            tvAssetAmountTrade.setText(sumAmount + " " + coinId);
            tvAssetMoneyTradeDj.setText("冻结资产 " + sumAmountDj + " " + coinId);
        }
    }

    /**
     * 控制资产数额可见
     */
    private void switchSee() {
        if (SharedPreferencesUtil.getInstance(activity).contains(SP_KEY_SHOW)) {
            if (SharedPreferencesUtil.getInstance(activity).getBooleanParam(SP_KEY_SHOW)) {
                SharedPreferencesUtil.getInstance(activity).setParam(activity, SP_KEY_SHOW, false);
                tvAssetMoneyTrade.setText("********");
                tvAssetMoneyTradeBail.setText("*****");
                ivAssetEyeTrade.setImageResource(R.mipmap.icon_eye_close);
            } else {
                SharedPreferencesUtil.getInstance(activity).setParam(activity, SP_KEY_SHOW, true);
                tvAssetMoneyTrade.setText("≈" + MathUtils.subZeroAndDot(MathUtils.getRundNumberForShow(sumTrade, BaseConstant.MONEY_FORMAT_ASSET, null)));
                tvAssetMoneyTradeBail.setText("保证金 " + MathUtils.subZeroAndDot(MathUtils.getRundNumber(sumCny, BaseConstant.MONEY_FORMAT, null)) + " " + coinId);
                ivAssetEyeTrade.setImageResource(R.mipmap.icon_eye_open);
            }
        } else {
            SharedPreferencesUtil.getInstance(activity).setParam(activity, SP_KEY_SHOW, false);
            tvAssetMoneyTrade.setText("********");
            tvAssetMoneyTradeBail.setText("*****");
            ivAssetEyeTrade.setImageResource(R.mipmap.icon_eye_close);
        }
    }

    /**
     * 解析昨日佣金数据
     *
     * @param response
     */
    @Override
    public void getTradeYesterdaySuccess(MessageResult response) {
        if (response != null && response.getData() != null) {
            Gson gson = new Gson();
            AcceptMerchantInfoEntity loginEntity = gson.fromJson(gson.toJson(response), new TypeToken<AcceptMerchantInfoEntity>() {
            }.getType());
            //昨日佣金
            String reward = MathUtils.subZeroAndDot(MathUtils.getRundNumber(loginEntity.getData().getDaysBuyReward() + loginEntity.getData().getDaysSellReward(), BaseConstant.MONEY_FORMAT, null));
            tvAssetCommissionY.setText("昨日佣金 " + reward + " " + coinId);
            String type = loginEntity.getData().getCoin();
            if (StringUtils.isNotEmpty(type)) {
                tvMoneyTypeTag.setText(type);
            }
        }
    }

    @Override
    public void getSelfLevelInfoSuccess(AcceptMerchantFrontVo acceptMerchantFrontVo) {
        if (acceptMerchantFrontVo != null) {
            if (acceptMerchantFrontVo.getCertifiedBusinessStatus() == 1) {
                if (acceptMerchantFrontVo.getAmount() != null) {
                    sumCny = acceptMerchantFrontVo.getAmount().doubleValue();
                }
            } else {
                if (acceptMerchantFrontVo.getMargin() != null) {
                    sumCny = acceptMerchantFrontVo.getMargin().doubleValue();
                }
            }
        }
        coinId = acceptMerchantFrontVo.getCoin();
        tvAssetMoneyTradeBail.setText("保证金 " + MathUtils.subZeroAndDot(MathUtils.getRundNumber(sumCny, BaseConstant.MONEY_FORMAT, null)) + " " + coinId);
    }

    @Override
    public void getTradeSuccess(AcceptanceMerchantListEntity response) {
        if (response != null && response.getData() != null) {
            //累计佣金
            String reward = MathUtils.subZeroAndDot(MathUtils.getRundNumber(response.getData().getTotalBuyReward() + response.getData().getTotalSellReward(), BaseConstant.MONEY_FORMAT, null));
            tvAssetCommission.setText(reward);
            //待结算佣金
            String noSettledReward = MathUtils.subZeroAndDot(MathUtils.getRundNumber(response.getData().getNoSettledReward(), BaseConstant.MONEY_FORMAT, null));
            tvAssetCommissionUn.setText("待结算佣金 " + noSettledReward + " " + coinId);
        }
    }

    /**
     * 获取service对象
     */
    private void bindSocketService() {
        mConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                WebSocketService.MyBinder binder = (WebSocketService.MyBinder) service;
                webSocketService = binder.getService();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
            }
        };
        mConnection2 = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                GuardService.MyBinder binder = (GuardService.MyBinder) service;
                guardService = binder.getService();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
            }
        };
        //启动Service
        startAllServices();
    }


    /**
     * 获取唤醒锁
     */
    private void acquireWakeLock() {
        if (mWakeLock == null) {
            PowerManager mPM = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
            mWakeLock = mPM.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, this.getClass().getCanonicalName());
            if (mWakeLock != null) {
                mWakeLock.acquire();
            }
        }
    }

    /**
     * 禁止锁屏
     */
    private void disableKeyguard() {
        //禁止锁屏
        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Activity.KEYGUARD_SERVICE);
        keyguardLock = keyguardManager.newKeyguardLock(KEYGUARD_SERVICE);
        keyguardLock.disableKeyguard();//关闭系统锁屏
    }

    /**
     * 释放锁
     */
    private void releaseWakeLock() {
        if (mWakeLock != null) {
            mWakeLock.release();
            mWakeLock = null;
        }
    }

    /**
     * 释放屏幕
     */
    private void releaseKeyguardLock() {
        try {
            if (keyguardLock != null) {
                keyguardLock.reenableKeyguard();
                keyguardLock = null;
            }
        } catch (Exception e) {

        }
    }

    /**
     * 开启所有Service
     */
    private void startAllServices() {
        Intent intent = new Intent(getApplicationContext(), WebSocketService.class);
        bindService(intent, mConnection, BIND_AUTO_CREATE);

//        Intent guardIntent = new Intent(getApplicationContext(), GuardService.class);
//        bindService(guardIntent, mConnection2, BIND_AUTO_CREATE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Log.d("开启JobWakeUpService", "JobWakeUpService: ");
            //版本必须大于5.0
            startService(new Intent(this, JobWakeUpService.class));
        }
    }

    @Override
    public void releaseOrderSuccess(MessageResult response) {
        if (response != null) {
            //自动放行成功
            openRawMusic(6);
            getMainInfo(false);
        }
    }

    @Override
    public void releaseOrderFail(HttpErrorEntity httpErrorEntity) {
        if (httpErrorEntity != null) {
            //自动放行失败
            openRawMusic(7);
            if (StringUtils.isNotEmpty(httpErrorEntity.getMessage())) {
                ToastUtils.showToast(activity, httpErrorEntity.getMessage());
            }
        }
    }

    /**
     * 风控提醒对话框
     * type 1:状态为11 2：状态为8
     */
    private void showAlerDialog(String message, int type) {
        alertIosDialog = new AlertIosDialog(activity);
        alertIosDialog.withWidthScale(0.8f);
        if (type == 1) {
            alertIosDialog
                    .setImg(0)
                    .setContent(message)
                    .setTag(getString(R.string.str_pay_warn_tag))
                    .setTagColor(getResources().getColor(R.color.font_sec_title));
        }
        if (type == 2) {
            alertIosDialog
                    .setImg(R.mipmap.icon_warm_tag)
                    .setContent(getString(R.string.str_pay_custom_cancel_tag));
        }
        alertIosDialog.setPositiveClickLister(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertIosDialog.dismiss();
            }
        });
        alertIosDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                isShowLoading = false;
                getOrder();
            }
        });
        alertIosDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //跳转通知授权界面
        //openNotification();
    }

    /**
     * 允许安装未知来源的应用
     * 其中的变量：1代表允许，如果换成0则代表禁止
     */
    public static void allowUnKnowSrc(Context context) {
        try {
            Settings.Global.putInt(context.getContentResolver(),
                    Settings.Secure.INSTALL_NON_MARKET_APPS, 1);
        } catch (SecurityException e) {
            //LogUtils.getInstance().d(e);
        }
    }

    /**
     * 是否已授权
     *
     * @return
     */
    private boolean isNotificationServiceEnable() {
        String pkgName = getPackageName();
        final String flat = Settings.Secure.getString(getContentResolver(), "enabled_notification_listeners");
        if (!TextUtils.isEmpty(flat)) {
            final String[] names = flat.split(":");
            for (int i = 0; i < names.length; i++) {
                final ComponentName cn = ComponentName.unflattenFromString(names[i]);
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 跳转通知授权界面
     */
    private void openNotification() {
        boolean isAuthor = isNotificationServiceEnable();
        if (!isAuthor) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                //直接跳转通知授权界面
                //android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS是API 22才加入到Settings里，这里直接写死
                startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
            }
        }
    }

    @Override
    public void checkVersionSuccess(String response) {
        if (StringUtils.isNotEmpty(response)) {
            VisionEntity entity = new Gson().fromJson(response, VisionEntity.class);
            if (entity != null && entity.getData() != null && (AppUtils.compareVersion(entity.getData().getVersion(), AppUtils.getVersionName(activity)) == 1)) {
                if (StringUtils.isNotEmpty(entity.getData().getUrl())) {
                    showVersionDialog(entity);
                } else {
                    ToastUtils.showToast(activity, getString(R.string.update_address_error_tag));
                }
            }
        }
    }

    /**
     * check权限
     *
     * @param position
     */
    private void checkPermission() {
        AndPermission.with(this)
                .runtime()
                .permission(Permission.Group.STORAGE)
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        LogUtils.d("permission: " + data.get(0));
                        presenter.checkVersion();
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        if (AndPermission.hasAlwaysDeniedPermission(activity, data)) {
                            AndPermission.permissionSetting(activity).execute();
                            return;
                        }
                        ToastUtils.showToast(activity, getString(com.spark.modulebase.R.string.str_no_permission));
                    }
                }).start();
    }

    /**
     * 版本更新提示框
     */
    private void showVersionDialog(final VisionEntity visionEntity) {
        appVersionDialog = new AppVersionDialog(activity, visionEntity);
        appVersionDialog.setPositiveOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (visionEntity.getData().getUrl() == null || "".equals(visionEntity.getData().getUrl())) {
                    ToastUtils.showToast(activity, getString(R.string.update_address_error_tag));
                } else {
                    download(visionEntity.getData().getUrl());
                }
                appVersionDialog.dismiss();
            }
        });
        appVersionDialog.show();
    }

    /**
     * 下载
     *
     * @param url
     */
    private void download(String url) {
        OkhttpUtils.get().url(url).build().execute(new FileCallback(FileUtils.getCacheSaveFile(this, "application.apk").getAbsolutePath()) {
            @Override
            public void inProgress(float progress) {
                progressDialog.show();
                progressDialog.setProgress((int) (progress * 100));
            }

            @Override
            public void onError(Request request, HttpErrorEntity e) {
                progressDialog.dismiss();
                String msg = e.getMessage();
                LogUtils.e("下载download===============================================onError===msg==" + msg);
            }

            @Override
            public void onResponse(File response) {
                progressDialog.dismiss();
                AppUtils.installAPk(response, activity);
            }
        });
    }

    /**
     * 初始化进度条
     */
    private void initProgressDialog() {
        //创建进度条对话框
        progressDialog = new ProgressDialog(this);
        //设置标题
        progressDialog.setTitle(getString(R.string.downloading_tag));
        //设置信息
        progressDialog.setMessage(getString(R.string.downloading_crazy_tag));
        progressDialog.setProgress(0);
        //设置显示的格式
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    }

    /**
     * 设置WIFI不休眠并确保黑屏后网络通信正常
     */
    public void setWifiDormancy() {
        int pol = Settings.System.WIFI_SLEEP_POLICY_NEVER;
        Settings.System.putInt(getContentResolver(), Settings.System.WIFI_SLEEP_POLICY, pol);
    }

    /**
     * 利用Android自带的定时器AlarmManager实现
     */
    private void testService() {
        Intent intent = new Intent(activity, WebSocketService.class);
        PendingIntent pi = PendingIntent.getService(activity, 1, intent, 0);
        AlarmManager alarm = (AlarmManager) getSystemService(Service.ALARM_SERVICE);
        if (alarm != null) {
            alarm.cancel(pi);
            // 闹钟在系统睡眠状态下会唤醒系统并执行提示功能
            // pendingIntent 为发送广播
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarm.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), pi);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarm.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), pi);
            } else {
                alarm.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), 1000, pi);
            }
        }
    }

    /**
     * 网络状态监听
     */
    BroadcastReceiver netReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isAvailable()) {
                    int type2 = networkInfo.getType();
                    String typeName = networkInfo.getTypeName();
                    LogUtils.e(type2 + "=========================" + typeName);
                    switch (type2) {
                        case 0://移动 网络    2G 3G 4G 都是一样的 实测 mix2s 联通卡
                            break;
                        case 1: //wifi网络
                            break;
                        case 9:  //网线连接
                            break;
                    }

                    if (isNeedReconnect) {
                        reconnect();
                    }

                } else {// 无网络
                    LogUtils.e("无网络 =========================");
                    isNeedReconnect = true;
                }
            }
        }

    };

    /**
     * 重新连接websocket
     */
    private void reconnect() {
        if (!NetworkUtil.checkNetwork(BaseApplication.getAppContext())) {
            ToastUtils.showToast(getString(R.string.str_please_check_net));
            return;
        }

        if (isNeedReconnect) {
            isNeedReconnect = false;
            if (isWebSocketService) {
                Log.e("", "WebSocketService- 000000000000000");
                if (webSocketService != null)
                    webSocketService.reconnect();
            } else {
                if (guardService != null)
                    guardService.reconnect();
            }
        }
    }

    /**
     * 有倒计时结束订单，刷新第一页
     *
     * @param event
     */
    public void onEvent(MainTimeDownFinishEvent event) {
        if (adapter != null) {
            if (event.randomTime < 0) {//倒计时为负数时，每隔10秒刷新一次数据
                handler_timeCurrent.removeCallbacksAndMessages(null);
                handler_timeCurrent.sendEmptyMessageDelayed(1, tick_flush * 1000);
            } else {
                startFlush();
            }
        }
    }

    //这里很重要，使用Handler的延时效果，每隔一秒刷新一下适配器，以此产生倒计时效果
    private Handler handler_timeCurrent = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    adapter.notifyDataSetChanged();
                    handler_timeCurrent.sendEmptyMessageDelayed(0, tick * 1000);
                    break;
                case 1:
                    startFlush();
                    break;
            }
        }
    };

    /**
     * 开始刷新
     */
    private void startFlush() {
        adapter.setEnableLoadMore(false);
        //查询在途订单
        getOrder();
    }

    /**
     * 停止刷新
     */
    private void stopFlush() {
        handler_timeCurrent.removeCallbacksAndMessages(null);
    }
}
