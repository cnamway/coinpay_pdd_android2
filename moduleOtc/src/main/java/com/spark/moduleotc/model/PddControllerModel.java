package com.spark.moduleotc.model;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spark.library.acp.api.PddApi;
import com.spark.library.acp.model.MessageResult;
import com.spark.library.acp.model.MessageResultPagePinduoduoGoods;
import com.spark.library.acp.model.MessageResultPagePinduoduoStores;
import com.spark.library.acp.model.PinduoduoGoods;
import com.spark.library.acp.model.PinduoduoStores;
import com.spark.library.acp.model.QueryCondition;
import com.spark.library.acp.model.QueryParamPinduoduoGoods;
import com.spark.library.acp.model.QueryParamPinduoduoStores;
import com.spark.modulebase.callback.ResponseCallBack;
import com.spark.modulebase.entity.HttpErrorEntity;
import com.spark.modulebase.utils.LogUtils;
import com.spark.modulebase.utils.StringUtils;
import com.spark.moduleotc.OtcUrls;
import com.spark.moduleotc.entity.ProductEntity;
import com.spark.moduleotc.entity.StoreEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.spark.modulebase.base.BaseConstant.JSON_ERROR;
import static com.spark.modulebase.base.BaseConstant.SUCCESS_CODE;

/**
 * PDD
 */
public class PddControllerModel {
    private PddApi pddApi;

    public PddControllerModel() {
        this.pddApi = new PddApi();
        this.pddApi.setBasePath(OtcUrls.getInstance().getHost());
    }

    public void findList(HashMap<String, String> params, final ResponseCallBack.SuccessListener<List<ProductEntity>> successListener, final ResponseCallBack.ErrorListener errorListener) {
        final QueryParamPinduoduoGoods queryParamOrderInTransit = new QueryParamPinduoduoGoods();
        queryParamOrderInTransit.setPageIndex(Integer.parseInt(params.get("pageIndex")));
        queryParamOrderInTransit.setPageSize(Integer.parseInt(params.get("pageSize")));
        //queryParamOrderInTransit.setSortFields("ctime_d");
        List<QueryCondition> queryConditions = new ArrayList<>();
        QueryCondition queryCondition = new QueryCondition();
        String name = params.get("name");
        if (StringUtils.isNotEmpty(name)) {
            queryCondition = new QueryCondition();
            queryCondition.setJoin("or");
            queryCondition.setOper(":");
            queryCondition.setKey("goodsName");
            queryCondition.setValue(name);
            queryConditions.add(queryCondition);

            queryCondition = new QueryCondition();
            queryCondition.setJoin("or");
            queryCondition.setOper(":");
            queryCondition.setKey("mallId");
            queryCondition.setValue(name);
            queryConditions.add(queryCondition);
        }
        queryParamOrderInTransit.setQueryList(queryConditions);
        new Thread(new Runnable() {
            @Override
            public void run() {
                pddApi.listGoodsUsingPOST(queryParamOrderInTransit, new Response.Listener<MessageResultPagePinduoduoGoods>() {
                    @Override
                    public void onResponse(MessageResultPagePinduoduoGoods response) {
                        int code = response.getCode();
                        if (code == SUCCESS_CODE) {
                            if (successListener != null) {
                                Gson gson = new Gson();
                                List<ProductEntity> list = gson.fromJson(gson.toJson(response.getData().getRecords()), new TypeToken<List<ProductEntity>>() {
                                }.getType());
                                successListener.onResponse(list);
                            }
                        } else {
                            if (errorListener != null) {
                                errorListener.onErrorResponse(new HttpErrorEntity(response.getCode(), response.getMessage(), response.getUrl(), response.getCid()));
                            }
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (errorListener != null) {
                            errorListener.onErrorResponse(error);
                        }
                    }
                });
            }
        }).start();

    }

    public void deleteProduct(final Long id, final ResponseCallBack.SuccessListener<String> listener, final ResponseCallBack.ErrorListener errorListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                pddApi.deleteGoodsUsingGET(id, new Response.Listener<MessageResult>() {
                    @Override
                    public void onResponse(MessageResult response) {
                        LogUtils.i("response==" + response.toString());
                        int code = response.getCode();
                        if (code == SUCCESS_CODE) {
                            if (listener != null) {
                                listener.onResponse(response.getMessage());
                            }
                        } else {
                            if (errorListener != null) {
                                errorListener.onErrorResponse(new HttpErrorEntity(response.getCode(), response.getMessage(), response.getUrl(), response.getCid()));
                            }
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (errorListener != null) {
                            errorListener.onErrorResponse(error);
                        }
                    }
                });
            }
        }).start();
    }

    public void updateProduct(Long id, Integer isUpper, final ResponseCallBack.SuccessListener<String> listener, final ResponseCallBack.ErrorListener errorListener) {
        final PinduoduoGoods pinduoduoGoodsDTO = new PinduoduoGoods();
        pinduoduoGoodsDTO.setId(id.intValue());
        pinduoduoGoodsDTO.setIsUpper(isUpper);
        new Thread(new Runnable() {
            @Override
            public void run() {
                pddApi.updateGoodsUsingPOST(pinduoduoGoodsDTO, new Response.Listener<MessageResult>() {
                    @Override
                    public void onResponse(MessageResult response) {
                        LogUtils.i("response==" + response.toString());
                        int code = response.getCode();
                        if (code == SUCCESS_CODE) {
                            if (listener != null) {
                                listener.onResponse(response.getMessage());
                            }
                        } else {
                            if (errorListener != null) {
                                errorListener.onErrorResponse(new HttpErrorEntity(response.getCode(), response.getMessage(), response.getUrl(), response.getCid()));
                            }
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (errorListener != null) {
                            errorListener.onErrorResponse(error);
                        }
                    }
                });
            }
        }).start();
    }


    public void findListStore(HashMap<String, String> params, final ResponseCallBack.SuccessListener<List<StoreEntity>> successListener, final ResponseCallBack.ErrorListener errorListener) {
        final QueryParamPinduoduoStores queryParamOrderInTransit = new QueryParamPinduoduoStores();
        queryParamOrderInTransit.setPageIndex(Integer.parseInt(params.get("pageIndex")));
        queryParamOrderInTransit.setPageSize(Integer.parseInt(params.get("pageSize")));
        //queryParamOrderInTransit.setSortFields("ctime_d");
        List<QueryCondition> queryConditions = new ArrayList<>();
//        QueryCondition queryCondition = new QueryCondition();
//        String name = params.get("name");
//        if (StringUtils.isNotEmpty(name)) {
//            queryCondition = new QueryCondition();
//            queryCondition.setJoin("or");
//            queryCondition.setOper(":");
//            queryCondition.setKey("goodsName");
//            queryCondition.setValue(name);
//            queryConditions.add(queryCondition);
//
//            queryCondition = new QueryCondition();
//            queryCondition.setJoin("or");
//            queryCondition.setOper(":");
//            queryCondition.setKey("mallId");
//            queryCondition.setValue(name);
//            queryConditions.add(queryCondition);
//        }
        queryParamOrderInTransit.setQueryList(queryConditions);
        new Thread(new Runnable() {
            @Override
            public void run() {
                pddApi.listStoresUsingPOST(queryParamOrderInTransit, new Response.Listener<MessageResultPagePinduoduoStores>() {
                    @Override
                    public void onResponse(MessageResultPagePinduoduoStores response) {
                        int code = response.getCode();
                        if (code == SUCCESS_CODE) {
                            if (successListener != null) {
                                Gson gson = new Gson();
                                List<StoreEntity> list = gson.fromJson(gson.toJson(response.getData().getRecords()), new TypeToken<List<StoreEntity>>() {
                                }.getType());
                                successListener.onResponse(list);
                            }
                        } else {
                            if (errorListener != null) {
                                errorListener.onErrorResponse(new HttpErrorEntity(response.getCode(), response.getMessage(), response.getUrl(), response.getCid()));
                            }
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (errorListener != null) {
                            errorListener.onErrorResponse(error);
                        }
                    }
                });
            }
        }).start();

    }

    public void deleteStore(final Long id, final ResponseCallBack.SuccessListener<String> listener, final ResponseCallBack.ErrorListener errorListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                pddApi.deleteStoreUsingGET(id, new Response.Listener<MessageResult>() {
                    @Override
                    public void onResponse(MessageResult response) {
                        LogUtils.i("response==" + response.toString());
                        int code = response.getCode();
                        if (code == SUCCESS_CODE) {
                            if (listener != null) {
                                listener.onResponse(response.getMessage());
                            }
                        } else {
                            if (errorListener != null) {
                                errorListener.onErrorResponse(new HttpErrorEntity(response.getCode(), response.getMessage(), response.getUrl(), response.getCid()));
                            }
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (errorListener != null) {
                            errorListener.onErrorResponse(error);
                        }
                    }
                });
            }
        }).start();
    }

    public void updateStore(final PinduoduoStores pinduoduoGoodsDTO, final ResponseCallBack.SuccessListener<String> listener, final ResponseCallBack.ErrorListener errorListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                pddApi.updateStoreUsingPOST(pinduoduoGoodsDTO, new Response.Listener<MessageResult>() {
                    @Override
                    public void onResponse(MessageResult response) {
                        LogUtils.i("response==" + response.toString());
                        int code = response.getCode();
                        if (code == SUCCESS_CODE) {
                            if (listener != null) {
                                listener.onResponse(response.getMessage());
                            }
                        } else {
                            if (errorListener != null) {
                                errorListener.onErrorResponse(new HttpErrorEntity(response.getCode(), response.getMessage(), response.getUrl(), response.getCid()));
                            }
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (errorListener != null) {
                            errorListener.onErrorResponse(error);
                        }
                    }
                });
            }
        }).start();
    }
}
