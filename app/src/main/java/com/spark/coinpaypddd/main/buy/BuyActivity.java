package com.spark.coinpaypddd.main.buy;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.flyco.dialog.widget.NormalListDialog;
import com.spark.coinpaypddd.GlobalConstant;
import com.spark.coinpaypddd.MyApplication;
import com.spark.coinpaypddd.R;
import com.spark.coinpaypddd.base.BaseActivity;
import com.spark.coinpaypddd.entity.SystemEntity;
import com.spark.coinpaypddd.event.CheckLoginSuccessEvent;
import com.spark.coinpaypddd.main.buy.order.MyOrderActivity;
import com.spark.moduleassets.entity.Wallet;
import com.spark.modulebase.utils.KeyboardUtils;
import com.spark.modulebase.utils.LogUtils;
import com.spark.modulebase.utils.MathUtils;
import com.spark.modulebase.utils.StringUtils;
import com.spark.modulebase.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;

import static com.spark.moduleassets.AssetsConstants.ACP;
import static com.spark.moduleassets.AssetsConstants.COMMON;

/**
 * 一键买币
 */
public class BuyActivity extends BaseActivity implements BuyContract.View {

    @BindView(R.id.tvRate)
    TextView tvRate;
    @BindView(R.id.tvRateChange)
    TextView tvRateChange;
    @BindView(R.id.tvCoinName1)
    TextView tvCoinName1;
    @BindView(R.id.etCount1)
    EditText etCount1;
    @BindView(R.id.tvCoinName2)
    TextView tvCoinName2;
    @BindView(R.id.etCount2)
    EditText etCount2;
    @BindView(R.id.llWarn)
    LinearLayout llWarn;

    private BuyPresenterImpl presenter;
    private String coinName = "";//单位
    private ArrayList<Wallet> wallets = new ArrayList<>();
    private int buyType = 2;//1按数量购买  2按金额购买
    private String[] coinArrays;
    private Wallet wallet;
    private String actualPayment;//支付方式
    private ArrayList<Wallet> walletsBase = new ArrayList<>();
    private int randomAdd = 0;//一键买币随机立减
    private boolean isRandom = true;//是否开启随机立减
    private int MAX = 100;//随机限额MAX
//    private int MAX = 50;//随机限额（币付、币安）
//    private int MAX = 10;//随机限额（搬搬赚）
    private int MIN = 1;//随机限额MAX

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    break;
            }
        }
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_buy;
    }

    @Override
    protected void initView(Bundle saveInstance) {
        super.initView(saveInstance);
        ivBack.setVisibility(View.VISIBLE);
        tvTitle.setText("");
        ivMessage.setVisibility(View.VISIBLE);
        ivMessage.setImageResource(R.mipmap.icon_my_order);
        setEditText(etCount1, 2);
        SystemEntity entity = MyApplication.getMyApplication().getSystemEntity();
        if (entity != null) {
            //是否开启随机立减
            isRandom = entity.isEnableRandomOtcMoney();
            if (isRandom) {
                List<Integer> randomOtcMoneyRange = entity.getRandomOtcMoneyRange();
                if (randomOtcMoneyRange != null && randomOtcMoneyRange.size() > 1) {
                    MIN = randomOtcMoneyRange.get(0);
                    MAX = randomOtcMoneyRange.get(1);
                }
            }
        } else {
            isRandom = true;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destory();
    }

    @Override
    protected void initData() {
        super.initData();
        presenter = new BuyPresenterImpl(this);
        //是否开启随机立减
        if (isRandom) {
            llWarn.setVisibility(View.VISIBLE);
            getRandomAdd();
        } else {
            llWarn.setVisibility(View.GONE);
        }
    }

    protected void getRandomAdd() {
        Random rand = new Random();
        randomAdd = rand.nextInt(MAX - MIN + 1) + MIN;
        LogUtils.e("一键买币随机立减randomAdd==" + randomAdd);
    }

    @Override
    protected void loadData() {
        super.loadData();
        presenter.getWallet(COMMON);
    }

    @OnClick({R.id.tvBuy, R.id.ivMessage, R.id.ivChange, R.id.tvCoinName2})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.tvBuy://确认购买
                if (StringUtils.isEmpty(coinName)) {
                    ToastUtils.showToast("未找到币种");
                    return;
                }
                showOrderDialog();
                break;
            case R.id.ivMessage://我的订单
                showActivity(MyOrderActivity.class, null, 1);
                break;
            case R.id.ivChange:
                if (buyType == 2) {//1按数量购买  2按金额购买
                    buyType = 1;
                    tvCoinName1.setText(coinName);
                    tvCoinName2.setText("CNY");
                    etCount1.setHint("请输入货币数量");
                    etCount2.setHint("金额");
                    String text1 = etCount1.getText().toString();
                    String text2 = etCount2.getText().toString();
                    etCount1.setText(text2);
                    etCount2.setText(text1);
                } else {
                    buyType = 2;
                    tvCoinName1.setText("CNY");
                    tvCoinName2.setText(coinName);
                    etCount1.setHint("请输入金额");
                    etCount2.setHint("货币数量");
                    String text1 = etCount1.getText().toString();
                    String text2 = etCount2.getText().toString();
                    etCount1.setText(text2);
                    etCount2.setText(text1);
                }
                break;
            case R.id.tvCoinName2://我的订单
                if (coinArrays == null) return;
                showCoinDialog();
                break;
        }
    }

    @Override
    protected void setListener() {
        super.setListener();
        etCount1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (StringUtils.isEmpty(coinName)) {
                    ToastUtils.showToast("未找到币种");
                    return;
                }
                String amount = StringUtils.getText(etCount1);
                if (amount.startsWith(".")) {
                    etCount1.setText("0.");
                    etCount1.setSelection(etCount1.length());
                } else {
                    if (StringUtils.isNotEmpty(amount)) {
                        double money = Double.parseDouble(amount);
                        if (buyType == 1) {//1按数量购买  2按金额购买
                            etCount2.setText(MathUtils.subZeroAndDot(MathUtils.getRundNumber(Double.valueOf(MathUtils.getBigDecimalMultiply(money + "", wallet.getLegalRate() + "")), 8, null)));
                        } else {
                            etCount2.setText(MathUtils.subZeroAndDot(MathUtils.getRundNumber(money / wallet.getLegalRate(), 8, null)));
                        }
                    } else {
                        etCount2.setText("");
                    }
                }

            }
        });
    }

    private ArrayList<Wallet> getUsdt() {
        ArrayList<Wallet> list = new ArrayList<>();
        boolean isUsdt = false;
        for (Wallet wallet : wallets) {
            if (wallet.getCoinId().equals("USDT")) {
                isUsdt = true;
            }
        }
        if (isUsdt) {
            for (Wallet wallet : wallets) {
                if (wallet.getCoinId().equals("USDT")) {
                    list.add(wallet);
                }
            }
        } else {
            return wallets;
        }
        return list;
    }

    @Override
    public void getWalletSuccess(String type, List<Wallet> list) {
        if (type.equals(COMMON)) {
            this.wallets.clear();
            this.wallets.addAll(list);
            wallets = getUsdt();
            if (wallets != null && wallets.size() > 0) {
                wallet = wallets.get(0);
                coinName = wallet.getCoinId();
                tvCoinName2.setText(coinName);
                tvRateChange.setText(wallet.getLegalRate() + " CNY");
                coinArrays = new String[wallets.size()];
                for (int i = 0; i < wallets.size(); i++) {
                    Wallet w = wallets.get(i);
                    coinArrays[i] = w.getCoinId();
                }
            }
            //查询交易账户
            //presenter.getWallet(ACP);
        } else if (type == ACP) {
            this.wallets.clear();
            this.wallets.addAll(list);
            if (wallets != null && wallets.size() > 0) {
                wallet = wallets.get(0);
                coinName = wallet.getCoinId();
                tvCoinName2.setText(coinName);
                tvRateChange.setText(wallet.getLegalRate() + " CNY");
                coinArrays = new String[wallets.size()];
                for (int i = 0; i < wallets.size(); i++) {
                    Wallet w = wallets.get(i);
                    coinArrays[i] = w.getCoinId();
                }
            }
        }
        if (StringUtils.isEmpty(coinName)) {
            ToastUtils.showToast("未找到币种");
        }
    }

    /**
     * 展示币种
     */
    private void showCoinDialog() {
        NormalListDialog normalDialog = null;
        normalDialog = new NormalListDialog(activity, coinArrays);
        normalDialog.titleBgColor(getResources().getColor(R.color.bg_btn_normal));
        normalDialog.title(getString(R.string.str_select_coin));
        final NormalListDialog finalNormalDialog = normalDialog;
        normalDialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                wallet = wallets.get(position);
                coinName = wallet.getCoinId();
                tvRateChange.setText(wallet.getLegalRate() + " CNY");
                if (buyType == 2) {//1按数量购买  2按金额购买
                    tvCoinName1.setText("CNY");
                    tvCoinName2.setText(coinName);
                    etCount1.setHint("请输入金额");
                    etCount2.setHint("货币数量");
                    etCount1.setText(etCount1.getText().toString());
                    etCount2.setText(etCount2.getText().toString());
                } else {
                    tvCoinName1.setText(coinName);
                    tvCoinName2.setText("CNY");
                    etCount1.setHint("货币数量");
                    etCount2.setHint("请输入金额");
                    etCount1.setText(etCount2.getText().toString());
                    etCount2.setText(etCount1.getText().toString());
                }
                etCount1.setText(etCount1.getText().toString());
                finalNormalDialog.dismiss();
            }
        });
        normalDialog.show();
    }


    /**
     * 选择交易类型
     */
    private void showOrderDialog() {
        final String[] stringItems = getResources().getStringArray(R.array.pay_type);
        final ActionSheetDialog dialog = new ActionSheetDialog(activity, stringItems, null);
        dialog.isTitleShow(false).itemTextColor(getResources().getColor(R.color.font_main_title))
                .cancelText(getResources().getColor(R.color.font_main_content))
                .cancelText(getResources().getString(R.string.str_cancel)).show();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.dismiss();
                selectType(position);
                checkInput();
            }
        });
    }

    private void selectType(int type) {
        switch (type) {
            case 0:
                actualPayment = GlobalConstant.ALIPAY;
                break;
            case 1:
                actualPayment = GlobalConstant.WECHAT;
                break;
            case 2:
                actualPayment = GlobalConstant.BANK;
                break;
        }
    }

    protected void checkInput() {
        String count1 = StringUtils.getText(etCount1);
        if (StringUtils.isEmpty(count1)) {
            if (buyType == 1) {
                ToastUtils.showToast("请输入货币数量");
            } else {
                ToastUtils.showToast("请输入金额");
            }
        } else if (StringUtils.isEmpty(actualPayment)) {
            showOrderDialog();
        } else {
            KeyboardUtils.hideSoftInput(activity);
            Bundle bundle = new Bundle();


            if (buyType == 1) {//1按数量购买  2按金额购买
                String money = etCount2.getText().toString();
                String amount = etCount1.getText().toString();

                double money2 = Double.parseDouble(money) + randomAdd;
                amount = MathUtils.subZeroAndDot(MathUtils.getRundNumber(money2 / wallet.getLegalRate(), 8, null));

                bundle.putString("money", money2 + "");
                bundle.putString("actualPayment", actualPayment);
                bundle.putString("price", wallet.getLegalRate() + "");
                bundle.putString("coinName", coinName);
                bundle.putString("amount", amount);
                bundle.putInt("buyType", buyType);
                bundle.putString("address", wallet.getAddress());
                bundle.putString("memberId", wallet.getMemberId() + "");

                LogUtils.e("随机立减金额money==" + money2);
                LogUtils.e("随机立减数量amount==" + amount);
            } else {
                String money = etCount1.getText().toString();
                String amount = etCount2.getText().toString();

                double money2 = Double.parseDouble(money) + randomAdd;//随机后金额
                amount = MathUtils.subZeroAndDot(MathUtils.getRundNumber(money2 / wallet.getLegalRate(), 8, null));

                bundle.putString("money", money2 + "");
                bundle.putString("actualPayment", actualPayment);
                bundle.putString("price", wallet.getLegalRate() + "");
                bundle.putString("coinName", coinName);
                bundle.putString("amount", amount);
                bundle.putInt("buyType", buyType);
                bundle.putString("address", wallet.getAddress());
                bundle.putString("memberId", wallet.getMemberId() + "");

                LogUtils.e("随机立减金额money==" + money2);
                LogUtils.e("随机立减数量amount==" + amount);
            }

            showActivity(BuyConfirmActivity.class, bundle, 1);
        }

    }

    /**
     * check uc、ac、acp成功后，通知刷新界面
     */
    public void onEvent(CheckLoginSuccessEvent event) {
        loadData();
    }

}
