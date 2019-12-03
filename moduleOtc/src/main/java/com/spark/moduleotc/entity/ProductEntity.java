package com.spark.moduleotc.entity;

import java.io.Serializable;
import java.util.Date;

public class ProductEntity implements Serializable {

    private Integer adminUid = null;
    private Integer cid = null;
    private Date ctime = null;
    private Integer did = null;
    private Integer errorCount = null;
    private Long goodsId = null;
    private String goodsName = null;
    private String goodsUrl = null;
    private Long groupId = null;
    private Long id = null;
    private Integer isStoreLimit = null;
    private Integer isUpper = null;
    private Integer lastUseTime = null;
    private Integer mallId = null;
    private Integer normalPrice = null;
    private Long skuId = null;
    private Integer status = null;
    private Integer storesId = null;

    public Integer getAdminUid() {
        return adminUid;
    }

    public void setAdminUid(Integer adminUid) {
        this.adminUid = adminUid;
    }

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public Date getCtime() {
        return ctime;
    }

    public void setCtime(Date ctime) {
        this.ctime = ctime;
    }

    public Integer getDid() {
        return did;
    }

    public void setDid(Integer did) {
        this.did = did;
    }

    public Integer getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(Integer errorCount) {
        this.errorCount = errorCount;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsUrl() {
        return goodsUrl;
    }

    public void setGoodsUrl(String goodsUrl) {
        this.goodsUrl = goodsUrl;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIsStoreLimit() {
        return isStoreLimit;
    }

    public void setIsStoreLimit(Integer isStoreLimit) {
        this.isStoreLimit = isStoreLimit;
    }

    public Integer getIsUpper() {
        return isUpper;
    }

    public void setIsUpper(Integer isUpper) {
        this.isUpper = isUpper;
    }

    public Integer getLastUseTime() {
        return lastUseTime;
    }

    public void setLastUseTime(Integer lastUseTime) {
        this.lastUseTime = lastUseTime;
    }

    public Integer getMallId() {
        return mallId;
    }

    public void setMallId(Integer mallId) {
        this.mallId = mallId;
    }

    public Integer getNormalPrice() {
        return normalPrice;
    }

    public void setNormalPrice(Integer normalPrice) {
        this.normalPrice = normalPrice;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getStoresId() {
        return storesId;
    }

    public void setStoresId(Integer storesId) {
        this.storesId = storesId;
    }
}
