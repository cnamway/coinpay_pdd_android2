package com.spark.coinpaypddd.entity;

import java.util.List;

/**
 * 系统配置信息
 */

public class SystemEntity {

    /**
     * appName : 配置承兑
     * logoUrl :
     * apiHost : api.bitaccept.bittopbian.work
     * casHost : cas.bitaccept.bittopbian.work
     * agentHost : api.agent.bitaccept.bittopbian.work
     * ws : ws://ws.bitaccept.bittopbian.work/ws
     * enableOtcBuy : true
     * enableRandomOtcMoney : true
     * randomOtcMoneyRange : [1,50]
     * forceRecommendRegister : true
     */

    private String appName;
    private String logoUrl;
    private String apiHost;
    private String casHost;
    private String agentHost;
    private String ws;
    private boolean enableOtcBuy;
    private boolean enableRandomOtcMoney;
    private boolean forceRecommendRegister;
    private List<Integer> randomOtcMoneyRange;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getApiHost() {
        return apiHost;
    }

    public void setApiHost(String apiHost) {
        this.apiHost = apiHost;
    }

    public String getCasHost() {
        return casHost;
    }

    public void setCasHost(String casHost) {
        this.casHost = casHost;
    }

    public String getAgentHost() {
        return agentHost;
    }

    public void setAgentHost(String agentHost) {
        this.agentHost = agentHost;
    }

    public String getWs() {
        return ws;
    }

    public void setWs(String ws) {
        this.ws = ws;
    }

    public boolean isEnableOtcBuy() {
        return enableOtcBuy;
    }

    public void setEnableOtcBuy(boolean enableOtcBuy) {
        this.enableOtcBuy = enableOtcBuy;
    }

    public boolean isEnableRandomOtcMoney() {
        return enableRandomOtcMoney;
    }

    public void setEnableRandomOtcMoney(boolean enableRandomOtcMoney) {
        this.enableRandomOtcMoney = enableRandomOtcMoney;
    }

    public boolean isForceRecommendRegister() {
        return forceRecommendRegister;
    }

    public void setForceRecommendRegister(boolean forceRecommendRegister) {
        this.forceRecommendRegister = forceRecommendRegister;
    }

    public List<Integer> getRandomOtcMoneyRange() {
        return randomOtcMoneyRange;
    }

    public void setRandomOtcMoneyRange(List<Integer> randomOtcMoneyRange) {
        this.randomOtcMoneyRange = randomOtcMoneyRange;
    }

    @Override
    public String toString() {
        return "SystemEntity{" +
                "appName='" + appName + '\'' +
                ", logoUrl='" + logoUrl + '\'' +
                ", apiHost='" + apiHost + '\'' +
                ", casHost='" + casHost + '\'' +
                ", agentHost='" + agentHost + '\'' +
                ", ws='" + ws + '\'' +
                ", enableOtcBuy=" + enableOtcBuy +
                ", enableRandomOtcMoney=" + enableRandomOtcMoney +
                ", forceRecommendRegister=" + forceRecommendRegister +
                ", randomOtcMoneyRange=" + randomOtcMoneyRange +
                '}';
    }
}
