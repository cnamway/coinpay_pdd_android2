package com.spark.modulebase.okhttp;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.webkit.WebSettings;
import android.widget.Toast;


import com.spark.modulebase.BaseApplication;
import com.spark.modulebase.R;
import com.spark.modulebase.entity.HttpErrorEntity;
import com.spark.modulebase.okhttp.delete.DeleteBuilder;
import com.spark.modulebase.okhttp.get.GetBuilder;
import com.spark.modulebase.okhttp.post.PostFormBuilder;
import com.spark.modulebase.okhttp.post.PostJsonBuilder;
import com.spark.modulebase.okhttp.store.PersistentCookieStore;
import com.spark.modulebase.utils.LogUtils;
import com.spark.modulebase.utils.StringUtils;
import com.spark.modulebase.utils.ToastUtils;

import java.io.IOException;
import java.net.ConnectException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkhttpUtils {
    private static OkhttpUtils mInstance;
    private OkHttpClient mOkHttpClient;
    private Handler handler;

    /**
     * 初始化
     */
    public OkhttpUtils() {
        mOkHttpClient = new OkHttpClient.Builder().retryOnConnectionFailure(true).connectTimeout(20, TimeUnit.SECONDS).readTimeout(20, TimeUnit.SECONDS)
                .cookieJar(new JavaNetCookieJar(BaseApplication.getAppContext().getCookieManager())).build();
        handler = new Handler(Looper.getMainLooper());
    }

    /**
     * 获取实例
     *
     * @return
     */
    public static OkhttpUtils getInstance() {
        if (mInstance == null) {
            synchronized (OkhttpUtils.class) {//同步代码块
                mInstance = new OkhttpUtils();
            }
        }
        return mInstance;
    }

    /**
     * PostFormBuilder
     *
     * @return
     */
    public static PostFormBuilder post() {
        return new PostFormBuilder();
    }

    /**
     * GetBuilder
     *
     * @return
     */
    public static GetBuilder get() {
        return new GetBuilder();
    }

    /**
     * DeleteBuilder
     */
    public static DeleteBuilder delete() {
        return new DeleteBuilder();
    }

    /**
     * PostJsonBuilder
     *
     * @return
     */
    public static PostJsonBuilder postJson() {
        return new PostJsonBuilder();
    }

    /**
     * 获取OkHttpClient
     *
     * @return
     */
    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    /**
     * 执行请求
     *
     * @param requestCall
     * @param callback
     */
    public void execute(RequestCall requestCall, Callback callback) {
        if (callback == null) callback = Callback.CALLBACK_DEFAULT;
        final Callback finalCallback = callback;
        requestCall.getCall().enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                HttpErrorEntity httpErrorEntity = null;
                if (e instanceof SocketTimeoutException) { // 超时异常
                    httpErrorEntity = new HttpErrorEntity(-1, BaseApplication.getAppContext().getString(R.string.str_connection_out), "", "");
                } else if (e instanceof ConnectException) { // 连接异常
                    httpErrorEntity = new HttpErrorEntity(-1, BaseApplication.getAppContext().getString(R.string.str_connection_error), "", "");
                }
                sendFailResultCallback(call.request(), httpErrorEntity, finalCallback);
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                if (response.isSuccessful()) {
                    Object o = finalCallback.parseNetworkResponse(response);
                    sendSuccessResultCallback(o, finalCallback);
                } else {
                    HttpErrorEntity httpErrorEntity = new HttpErrorEntity(response.code(), response.message().toString(), "", "");
                    sendFailResultCallback(call.request(), httpErrorEntity, finalCallback);
                }
            }
        });
    }

    /**
     * 请求数据失败
     *
     * @param request
     * @param response
     * @param callback
     */
    public void sendFailResultCallback(final Request request, final HttpErrorEntity httpErrorEntity, final Callback callback) {
        if (callback == null) return;
        handler.post(new Runnable() {
            @Override
            public void run() {
                callback.onError(request, httpErrorEntity);
                callback.onAfter();
            }
        });
    }

    /**
     * 请求数据成功
     *
     * @param object
     * @param callback
     */
    public void sendSuccessResultCallback(final Object object, final Callback callback) {
        if (callback == null) return;
        handler.post(new Runnable() {
            @Override
            public void run() {
                callback.onResponse(object);
                callback.onAfter();
            }
        });
    }

    public Handler getHandler() {
        return handler;
    }

    /**
     * Okhttp设置User-Agent
     *
     * @return
     */
    public static String getUserAgent() {
        String userAgent = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            try {
                userAgent = WebSettings.getDefaultUserAgent(BaseApplication.getAppContext());
            } catch (Exception e) {
                userAgent = System.getProperty("http.agent");
            }
        } else {
            userAgent = System.getProperty("http.agent");
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0, length = userAgent.length(); i < length; i++) {
            char c = userAgent.charAt(i);
            if (c <= '\u001f' || c >= '\u007f') {
                sb.append(String.format("\\u%04x", (int) c));
            } else {
                sb.append(c);
            }
        }
        LogUtils.d("User-Agent: " + sb.toString());
        return sb.toString();
    }

}
