package com.spark.coinpaypddd.event;

/**
 * 列表刷新通知
 */
public class OrderListFrushEvent {
    public int type;

    public OrderListFrushEvent(int t) {
        type = t;
    }

}
