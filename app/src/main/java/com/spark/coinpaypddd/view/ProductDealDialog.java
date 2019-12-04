package com.spark.coinpaypddd.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.spark.coinpaypddd.R;

/**
 *
 */
public class ProductDealDialog extends Dialog {

    TextView tvLeft;
    TextView tvRight;

    private Context mContext;
    private int type;
    private float mWidthScale;
    private float mHeightScale;

    public ProductDealDialog(@NonNull Context context, int type) {
        super(context);
        this.mContext = context;
        this.type = type;
        setDialogTheme();
        initView();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        int width = mContext.getResources().getDisplayMetrics().widthPixels;
        int height = mContext.getResources().getDisplayMetrics().heightPixels;
        if (mWidthScale > 0) {
            attributes.width = (int) (width * mWidthScale);
        }
        if (mHeightScale > 0) {
            attributes.height = (int) (height * mHeightScale);
        }
        window.setAttributes(attributes);
        window.setGravity(Gravity.CENTER);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
    }

    private void initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_dialog_product_deal, null);
        setContentView(view);
        tvLeft = view.findViewById(R.id.tvLeft);
        tvRight = view.findViewById(R.id.tvRight);
        if (type == 1) {//isUpper是否上架(0否,1是)
            tvLeft.setText("下架");
        } else {
            tvLeft.setText("重新上架");
        }
    }

    public ProductDealDialog withWidthScale(float mWidthScale) {
        this.mWidthScale = mWidthScale;
        return this;
    }

    public ProductDealDialog withHeightScale(float mHeightScale) {
        this.mHeightScale = mHeightScale;
        return this;
    }

    /**
     * set dialog theme(设置对话框主题)
     */
    private void setDialogTheme() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);// android:windowNoTitle
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));// android:windowBackground
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);// android:backgroundDimEnabled默认是true的
    }

    public interface ClickLister {
        void onCancel(int type);

        void onConfirm();
    }


    public void setClickListener(final ClickLister listener) {
        if (tvLeft != null) {
            tvLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onCancel(type);
                    }
                }
            });
        }

        if (tvRight != null) {
            tvRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onConfirm();
                    }
                }
            });
        }
    }

}