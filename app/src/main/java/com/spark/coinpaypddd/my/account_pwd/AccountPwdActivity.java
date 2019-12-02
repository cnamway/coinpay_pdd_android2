package com.spark.coinpaypddd.my.account_pwd;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.spark.coinpaypddd.MyApplication;
import com.spark.coinpaypddd.R;
import com.spark.coinpaypddd.base.BaseActivity;
import com.spark.coinpaypddd.my.account_pwd_reset.AccountPwdResetActivity;
import com.spark.modulebase.entity.User;
import com.spark.modulebase.utils.StringUtils;
import com.spark.modulebase.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 设置/修改资金密码
 */
public class AccountPwdActivity extends BaseActivity implements AccountPwdContract.View {
    @BindView(R.id.etOldPwd)
    TextView etOldPwd;
    @BindView(R.id.etAccountPwd)
    EditText etAccountPwd;
    @BindView(R.id.etRepeatPwd)
    EditText etRepeatPwd;
    @BindView(R.id.tvSet)
    TextView tvSet;
    @BindView(R.id.llOldPwd)
    LinearLayout llOldPwd;
    @BindView(R.id.tvPwdTag)
    TextView tvPwdTag;
    @BindView(R.id.tvRePwdTag)
    TextView tvRePwdTag;
    @BindView(R.id.tvTag)
    TextView tvTag;
    @BindView(R.id.tvForgetPas)
    TextView tvForgetPas;

    private AccountPwdPresenterImpl presenter;
    private boolean isSet = false;//是否已设置资金密码

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_account_pwd;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destory();
    }

    @Override
    protected void initView(Bundle saveInstance) {
        super.initView(saveInstance);
        ivBack.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.set_money_pwd);
    }

    @Override
    protected void initData() {
        super.initData();
        presenter = new AccountPwdPresenterImpl(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            isSet = bundle.getBoolean("isSet");
            if (isSet) {
                tvTitle.setText(getString(R.string.change_money_pwd));
                tvTag.setText(getString(R.string.edit_money_pwd_tag));
                llOldPwd.setVisibility(View.VISIBLE);
                tvPwdTag.setText(getString(R.string.new_money_pwd));
                etAccountPwd.setHint(getString(R.string.text_money_pwd_tag));
                tvRePwdTag.setText(getString(R.string.confirm_money_pwd));
                etRepeatPwd.setHint(getString(R.string.confirm_money_pwd));
                tvSet.setText(getString(R.string.str_sure));
                tvForgetPas.setVisibility(View.VISIBLE);
            } else {
                tvForgetPas.setVisibility(View.GONE);
            }
        }
    }

    @OnClick(R.id.tvSet)
    void accountPwd() {
        String jyPassword = etAccountPwd.getText().toString().trim();
        String repeatPWd = etRepeatPwd.getText().toString().trim();
        String oldPwd = etOldPwd.getText().toString().trim();
        if ((StringUtils.isEmpty(jyPassword, repeatPWd) && !isSet)) {
            ToastUtils.showToast(this, R.string.incomplete_information);
        } else if (StringUtils.isEmpty(jyPassword, repeatPWd, oldPwd) && isSet) {
            ToastUtils.showToast(this, R.string.incomplete_information);
        } else if (jyPassword.length() != 6) {
            ToastUtils.showToast(this, R.string.text_money_pwd_tag);
        } else if (!jyPassword.equals(repeatPWd)) {
            ToastUtils.showToast(this, R.string.str_pwd_diff);
        } else {
            if (!isSet) {
                presenter.accountPwd(jyPassword);
            } else {
                presenter.editAccountPwd(oldPwd, jyPassword);
            }
        }
    }

    @OnClick(R.id.tvForgetPas)
    void skipResetActivity() {
        String phone = MyApplication.getAppContext().getCurrentUser().getMobilePhone();
        if (StringUtils.isEmpty(phone)) {
            ToastUtils.showToast(activity, getString(R.string.binding_phone_first));
        } else {
            if (phone.startsWith("86")) {
                phone = phone.substring(2, phone.length());
            }
            Bundle bundle = new Bundle();
            bundle.putString("phone", phone);
            showActivity(AccountPwdResetActivity.class, bundle, 1);
        }
    }


    @Override
    public void accountPwdSuccess(String obj) {
        User user = MyApplication.getAppContext().getCurrentUser();
        user.setFundsVerified(1);
        MyApplication.getAppContext().setCurrentUser(user);

        ToastUtils.showToast(this, obj);
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void editAccountPwdSuccess(String obj) {
        ToastUtils.showToast(this, obj);
        finish();
    }

    @Override
    public void doPostFail(Integer code, String toastMessage) {
        ToastUtils.showToast(this, toastMessage);
    }
}
