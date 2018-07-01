package com.cxz.sample.rxeasyhttp.cache;

import com.cxz.sample.Bean;
import com.cxz.sample.rxeasyhttp.api.ApiResponse;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.rx_cache2.EvictProvider;
import io.rx_cache2.LifeCache;

/**
 * Created by chenxz on 2018/7/1.
 */

public interface CacheProvider {

    @LifeCache(duration = 5, timeUnit = TimeUnit.MINUTES)
    Observable<ApiResponse<Bean>> getArticles(Observable<ApiResponse<Bean>> oRepos, EvictProvider evictDynamicKey);

}
