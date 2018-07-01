package com.cxz.sample.rxeasyhttp;

import android.content.Context;
import android.util.Log;

import com.cxz.sample.Bean;
import com.cxz.sample.Constant;
import com.cxz.sample.rxeasyhttp.api.ApiResponse;
import com.cxz.sample.rxeasyhttp.api.ApiService;
import com.cxz.sample.rxeasyhttp.cache.CacheProvider;
import com.cxz.sample.rxeasyhttp.exception.ApiException;
import com.cxz.sample.rxeasyhttp.parser.GsonTSpeaker;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.rx_cache2.EvictProvider;
import io.rx_cache2.internal.RxCache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by chenxz on 2018/7/1.
 */

public class RxHttpManager {

    public static final String TAG = RxHttpManager.class.getSimpleName();
    private static final int DEFAULT_TIMEOUT = 5;
    private Retrofit mRetrofit;
    private ApiService mApiService;
    private final CacheProvider cacheProvider;
    private static Context mContext;
    private volatile static RxHttpManager instance;

    private RxHttpManager() {
        HttpLoggingInterceptor.Level level = HttpLoggingInterceptor.Level.BODY;
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.i("HttpManager", message);
            }
        });
        loggingInterceptor.setLevel(level);
        //拦截请求和响应日志并输出，其实有很多封装好的日志拦截插件，大家也可以根据个人喜好选择。
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(loggingInterceptor);

        OkHttpClient okHttpClient = builder.build();

        mRetrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(Constant.BASE_URL)
                .client(okHttpClient)
                .build();

        cacheProvider = new RxCache.Builder()
                .persistence(mContext.getFilesDir(), new GsonTSpeaker())
                .using(CacheProvider.class);

        mApiService = mRetrofit.create(ApiService.class);
    }

    public static RxHttpManager getInstance() {
        if (instance == null) {
            synchronized (RxHttpManager.class) {
                if (instance == null) {
                    instance = new RxHttpManager();
                }
            }
        }
        return instance;
    }

    public static void init(Context context) {
        mContext = context;
    }

    private <T> void toSubscribe(Observable<ApiResponse<T>> o, Observer<T> s) {
        o.subscribeOn(Schedulers.io())
                .map(new Function<ApiResponse<T>, T>() {
                    @Override
                    public T apply(@NonNull ApiResponse<T> response) throws Exception {
                        int code = Integer.parseInt(response.getErrorCode());
                        if (code != Constant.SUCCESS_CODE) {
                            throw new ApiException(code, response.getErrorMsg());
                        } else {
                            return response.getData();
                        }
                    }
                })
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }

    public void getArticlesWithCache(Observer<Bean> subscriber, boolean update) {
        toSubscribe(cacheProvider.getArticles(mApiService.getArticles(), new EvictProvider(update)), subscriber);
    }

    public void getArticlesNoCache(Observer<Bean> subscriber) {
        toSubscribe(mApiService.getArticles(), subscriber);
    }

}
