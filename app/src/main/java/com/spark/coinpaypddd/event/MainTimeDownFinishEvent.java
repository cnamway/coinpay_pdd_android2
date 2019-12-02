package com.spark.coinpaypddd.event;

/**
 * 倒计时完成通知
 */
public class MainTimeDownFinishEvent {
    public long randomTime;//倒计时为负数时，每隔10秒刷新一次数据

    public MainTimeDownFinishEvent(long t) {
        randomTime = t;
    }
}
