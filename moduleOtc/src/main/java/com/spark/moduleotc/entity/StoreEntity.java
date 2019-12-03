package com.spark.moduleotc.entity;

import java.io.Serializable;
import java.util.Date;

public class StoreEntity implements Serializable {

    private Integer adminUid = null;
    private Integer cid = null;
    private Date ctime = null;
    private Long curTotal = null;
    private Long dealTotal = null;
    private Integer did = null;
    private Long id = null;
    private Long mallId = null;
    private Date mtime = null;
    private String name = null;
    private Date orderTime = null;
    private Long orderTotal = null;
    private String remark = null;
    private Integer status = null;
    private String storeAddress = null;
    private Long storeRemainTotal = null;

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

    public Long getCurTotal() {
        return curTotal;
    }

    public void setCurTotal(Long curTotal) {
        this.curTotal = curTotal;
    }

    public Long getDealTotal() {
        return dealTotal;
    }

    public void setDealTotal(Long dealTotal) {
        this.dealTotal = dealTotal;
    }

    public Integer getDid() {
        return did;
    }

    public void setDid(Integer did) {
        this.did = did;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMallId() {
        return mallId;
    }

    public void setMallId(Long mallId) {
        this.mallId = mallId;
    }

    public Date getMtime() {
        return mtime;
    }

    public void setMtime(Date mtime) {
        this.mtime = mtime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public Long getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(Long orderTotal) {
        this.orderTotal = orderTotal;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }

    public Long getStoreRemainTotal() {
        return storeRemainTotal;
    }

    public void setStoreRemainTotal(Long storeRemainTotal) {
        this.storeRemainTotal = storeRemainTotal;
    }
}
