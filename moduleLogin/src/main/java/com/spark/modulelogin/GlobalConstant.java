package com.spark.coinpaypddd;

/**
 * 配置文件
 */
public class GlobalConstant {

    /**
     * 是否是测试环境:false线上环境/true测试环境
     */
    public static boolean isTest = false;

    public static String CUR_HOST = "oznex.com";

    public static String CUR_HOST_UC = "";
    public static String CUR_HOST_AC = "";
    public static String CUR_HOST_ACP = "";

    public static String getHost() {
        return isTest ? "http://192.168.2.245" : CUR_HOST.contains("bizhuanex") || CUR_HOST.contains("bikeshangmao") ? "http://api." + CUR_HOST : "http://api.bitaccept." + CUR_HOST;
    }

    public static String getHostLogin() {
        return isTest ? "http://192.168.2.245:8446" : CUR_HOST.contains("bizhuanex") || CUR_HOST.contains("bikeshangmao") ? "http://cas." + CUR_HOST : "http://cas.bitaccept." + CUR_HOST;
    }

    public static String getHostUC() {
        return isTest ? "http://" + CUR_HOST_UC + ":48888/uc" : CUR_HOST.contains("bizhuanex") || CUR_HOST.contains("bikeshangmao") ? "http://api." + CUR_HOST + "/uc" : "http://api.bitaccept." + CUR_HOST + "/uc";
    }

    public static String getHostAC() {
        return isTest ? "http://" + CUR_HOST_AC + ":48881/ac" : CUR_HOST.contains("bizhuanex") || CUR_HOST.contains("bikeshangmao") ? "http://api." + CUR_HOST + "/ac" : "http://api.bitaccept." + CUR_HOST + "/ac";
    }

    public static String getHostOTC() {
        return isTest ? "http://" + CUR_HOST_ACP + ":38081/acceptance" : CUR_HOST.contains("bizhuanex") || CUR_HOST.contains("bikeshangmao") ? "http://api." + CUR_HOST + "/acceptance" : "http://api.bitaccept." + CUR_HOST + "/acceptance";
    }

    public static String getHostAgent() {
        if (CUR_HOST.contains("bizhuanex") || CUR_HOST.contains("bikeshangmao")) {
            return "http://api.agent." + CUR_HOST + "/agentApi";
        } else {
            return isTest ? "http://192.168.2.183:48891/agentApi" : CUR_HOST.contains("bench") ? "http://api.agent." + CUR_HOST + "/agentApi" : "http://api.agent.bitaccept." + CUR_HOST + "/agentApi";
        }
    }

    /**
     * AC,UC,OTC模块地址后缀
     */
    public static final String TYPE_AC = "ac";
    public static final String TYPE_UC = "uc";
    public static final String TYPE_OTC = "acceptance";

    /**
     * 上传文件来源
     */
    public static final int TAKE_PHOTO = 0;
    public static final int CHOOSE_ALBUM = 1;

    /**
     * 支付方式类型
     */
    public static final String ALIPAY = "alipay"; // 支付宝
    public static final String WECHAT = "wechat"; // 微信
    public static final String BANK = "card"; // 银行
    public static final String PAYPAL = "PAYPAL"; // PAYPAL
    public static final String PDD_ALIPAY = "pdd-alipay"; // 拼多多-支付宝
    public static final String PDD_WECHAT= "pdd-wechat"; // 拼多多-微信
    public static final String PDD= "pdd"; // 拼多多

    public static final String CNY = "CNY";

    /**
     * 分页
     */
    public static final int PageSize = 10;
    public static final int PageSizeMax = 20;

    /**
     * webview参数
     */
    public static final String URL = "url";
    public static final String NEEDTITLE = "needTitle";
    public static final String TITLE = "title";

    //    public static final String KEEP_ALIVE = "http://manual.bp.wxmarket.cn/#/manual/device?id=";
//    public static final String HELP = "http://manual.1688up.net/manual/q&a.html";

    public static final String KEEP_ALIVE = "http://baohuo." + CUR_HOST + "/baohuo/#/manual/device?id=";
    public static final String HELP = "http://manual." + CUR_HOST + "/manual/q&a.html";

}
