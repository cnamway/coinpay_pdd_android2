package com.spark.moduleotc.entity;

public class OnlineStatus {
    int orderTakingStatus;//0未接单 1兑入 2兑出 3全部
    int isOnline;

    public int getOrderTakingStatus() {
        return orderTakingStatus;
    }

    public void setOrderTakingStatus(int orderTakingStatus) {
        this.orderTakingStatus = orderTakingStatus;
    }

    public int getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(int isOnline) {
        this.isOnline = isOnline;
    }
}
