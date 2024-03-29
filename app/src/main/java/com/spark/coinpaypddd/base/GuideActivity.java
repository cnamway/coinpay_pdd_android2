package com.spark.coinpaypddd.base;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.spark.coinpaypddd.MyApplication;
import com.spark.coinpaypddd.R;
import com.spark.coinpaypddd.login.LoginActivity;
import com.spark.coinpaypddd.main.MainActivity;
import com.spark.modulebase.entity.User;
import com.spark.modulebase.utils.AppUtils;
import com.spark.modulebase.utils.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.spark.modulebase.utils.SharedPreferencesUtil.SP_KEY_FIRST_USE;


public class GuideActivity extends BaseActivity {

    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.ll)
    LinearLayout ll;
    @BindView(R.id.rl)
    RelativeLayout rl;
    @BindView(R.id.goToMain)
    ImageView goToMain;

    private PagerAdapter mAdapter;
    private List<View> mViews = new ArrayList<View>();
    //圆点与圆点之间的边距
    private int left;
    //红色圆点ImageView
    private ImageView red_Iv;

    protected int getActivityLayoutId() {
        return R.layout.activity_guide;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }


    private void initView() {
        LayoutInflater mInflater = LayoutInflater.from(this);

        View tab01 = mInflater.inflate(R.layout.view_guide, null);
        View guide_no_01 = tab01.findViewById(R.id.guide_no_01);
        AppUtils.scaleImage(this, guide_no_01, R.mipmap.guide_no_01);

        View tab02 = mInflater.inflate(R.layout.view_guide, null);
        View guide_no_02 = tab02.findViewById(R.id.guide_no_01);
        AppUtils.scaleImage(this, guide_no_02, R.mipmap.guide_no_02);

        View tab03 = mInflater.inflate(R.layout.view_guide, null);
        View guide_no_03 = tab03.findViewById(R.id.guide_no_01);
        AppUtils.scaleImage(this, guide_no_03, R.mipmap.guide_no_03);

        mViews.add(tab01);
        mViews.add(tab02);
        mViews.add(tab03);

        //初始化导航页面和灰色圆点
        for (int i = 0; i < mViews.size(); i++) {
            //动态加载灰色圆点
            ImageView gray_Iv = new ImageView(this);
            gray_Iv.setImageResource(R.mipmap.xtt);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            //从第二个开始有边距
            if (i > 0) {
                layoutParams.leftMargin = 40;   //注意单位是px
            }
            gray_Iv.setLayoutParams(layoutParams);
            ll.addView(gray_Iv);
        }
        red_Iv = new ImageView(this);
        red_Iv.setImageResource(R.mipmap.xto);
        rl.addView(red_Iv);
        //任何一个组件都可以得到视图树
        red_Iv.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            //视图完成绘制的时候调用
            @Override
            public void onGlobalLayout() {
                left = ll.getChildAt(1).getLeft() - ll.getChildAt(0).getLeft();
                System.out.println(left);
                //移除视图树的监听
                red_Iv.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });


        mAdapter = new PagerAdapter() {

            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                container.removeView(mViews.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View view = mViews.get(position);
                container.addView(view);
                return view;
            }

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            @Override
            public int getCount() {
                return mViews.size();
            }
        };
        viewpager.setAdapter(mAdapter);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            //导航页被选择的时候调用
            @Override
            public void onPageSelected(int position) {
                if (position == mViews.size() - 1) {
                    rl.setVisibility(View.GONE);
                    ll.setVisibility(View.GONE);
                    goToMain.setVisibility(View.VISIBLE);
                } else {
                    rl.setVisibility(View.VISIBLE);
                    ll.setVisibility(View.VISIBLE);
                    goToMain.setVisibility(View.GONE);
                }
            }

            //导航页滑动的时候调用
            //positionOffset:滑动的百分比（[0,1}）
            @Override
            public void onPageScrolled(int position, float positionOffset, int arg2) {
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) red_Iv.getLayoutParams();
                layoutParams.leftMargin = (int) (left * positionOffset + position * left);
                red_Iv.setLayoutParams(layoutParams);
            }

            //导航页滑动的状态改变的时候调用
            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    @OnClick({R.id.goToMain})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.goToMain://立即体验
                goToMain();
                break;
        }
    }

    /**
     * 跳转
     */
    private void goToMain() {
        SharedPreferencesUtil.getInstance(activity).setParam(activity, SP_KEY_FIRST_USE, false);
        User user = MyApplication.getAppContext().getCurrentUser();
        if (user.isLogin()) {
            showActivity(MainActivity.class, null);
        } else {
            showActivity(LoginActivity.class, null);
        }
        finish();
    }

}
