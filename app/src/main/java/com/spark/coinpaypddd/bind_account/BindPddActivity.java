package com.spark.coinpaypddd.bind_account;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.spark.coinpaypddd.GlobalConstant;
import com.spark.coinpaypddd.MyApplication;
import com.spark.coinpaypddd.R;
import com.spark.coinpaypddd.base.BaseActivity;
import com.spark.coinpaypddd.entity.PayWaySetting;
import com.spark.coinpaypddd.view.DeleteDialog;
import com.spark.library.acp.model.MessageResult;
import com.spark.modulebase.entity.User;
import com.spark.modulebase.utils.MathUtils;
import com.spark.modulebase.utils.StringUtils;
import com.spark.modulebase.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 绑定拼多多
 */
public class BindPddActivity extends BaseActivity implements BindAccountContract.PaypalView {
    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.etAccount)
    EditText etAccount;
    @BindView(R.id.etAccountPwd)
    EditText etAccountPwd;
    @BindView(R.id.etPwd)
    EditText etPwd;
    @BindView(R.id.etWarn)
    EditText etWarn;
    @BindView(R.id.etDay)
    EditText etDay;
    @BindView(R.id.tvConfirm)
    TextView tvConfirm;

    private BindPaypalPresenterImpl presenter;
    private PayWaySetting payWaySetting;
    private boolean isUpdate = false;//添加或者更新

    String name = "";
    String account = "";
    String accountPass = "";
    String dayLimit = "";
    private DeleteDialog deleteDialog;

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_bind_pdd;
    }

    @Override
    protected void initView(Bundle saveInstance) {
        super.initView(saveInstance);
        ivBack.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.VISIBLE);
        User user = MyApplication.getAppContext().getCurrentUser();
        if (user != null && StringUtils.isNotEmpty(user.getRealName())) {
            etName.setText(user.getRealName());
        }
    }

    @Override
    protected void initData() {
        super.initData();
        presenter = new BindPaypalPresenterImpl(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            payWaySetting = (PayWaySetting) bundle.getSerializable("data");
            if (payWaySetting != null) {
                isUpdate = true;
                tvConfirm.setText(getResources().getString(R.string.str_text_change));
                etAccount.setText(payWaySetting.getPayAddress());
                etAccountPwd.setText(payWaySetting.getExpandField());
                etWarn.setText(payWaySetting.getPayNotes());
                etDay.setText(MathUtils.subZeroAndDot(payWaySetting.getDayLimit() + ""));
                etName.setText(payWaySetting.getRealName());
                etAccount.setEnabled(false);
                etAccountPwd.setEnabled(false);
                etAccount.setBackgroundColor(Color.WHITE);
                etAccountPwd.setBackgroundColor(Color.WHITE);
            }
        }
        if (isUpdate) {
            tvTitle.setText(getString(R.string.str_text_change) + getString(R.string.str_pdd));
        } else {
            tvTitle.setText(getString(R.string.str_account_seting) + getString(R.string.str_pdd));
        }
    }

    @OnClick({R.id.tvConfirm})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.tvConfirm:
                confirm();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destory();
    }

    private void confirm() {
        name = etName.getText().toString().trim();
        account = etAccount.getText().toString().trim();
        accountPass = etAccountPwd.getText().toString().trim();
        dayLimit = etDay.getText().toString();

        if (StringUtils.isEmpty(account, accountPass, dayLimit, name)) {
            ToastUtils.showToast(this, R.string.incomplete_information);
        } else {
            showReleaseDialog();
        }
    }

    /**
     * 提示框-请输入币交易密码
     */
    private void showReleaseDialog() {
        deleteDialog = new DeleteDialog(activity);
        deleteDialog.setPositiveOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pwd = StringUtils.getText(deleteDialog.getPwdEditText());
                if (StringUtils.isEmpty(pwd)) {
                    ToastUtils.showToast(activity, getString(R.string.str_please_input_assets_pwd));
                } else if (pwd.length() != 6) {
                    ToastUtils.showToast(activity, getString(R.string.text_money_pwd_tag));
                } else {
                    if (isUpdate) {
                        presenter.doUpdatePaypal(payWaySetting.getId(), payWaySetting.getPayType(), account, getString(R.string.str_pdd), "", pwd, "", dayLimit, name, accountPass);
                    } else {
                        presenter.doBindPaypal(GlobalConstant.PDD, account, getString(R.string.str_pdd), "", pwd, "", dayLimit, name, accountPass);
                    }
                    deleteDialog.dismiss();
                }
            }
        });
        deleteDialog.setTitle(getResources().getString(R.string.str_please_input_assets_pwd));
        deleteDialog.show();
    }


    @Override
    public void doBindPaypalSuccess(MessageResult obj) {
        if (StringUtils.isNotEmpty(obj.getMessage())) {
            if (obj.getMessage().contains("false")) {
                ToastUtils.showToast(this, "币交易密码不正确!");
            } else {
                ToastUtils.showToast(this, obj.getMessage());
                setResult(RESULT_OK);
                finish();
            }
        }
    }

    @Override
    public void doUpdatePaypalSuccess(MessageResult obj) {
        if (StringUtils.isNotEmpty(obj.getMessage())) {
            if (obj.getMessage().contains("false")) {
                ToastUtils.showToast(this, "币交易密码不正确!");
            } else {
                ToastUtils.showToast(this, obj.getMessage());
                setResult(RESULT_OK);
                finish();
            }
        }
    }

}