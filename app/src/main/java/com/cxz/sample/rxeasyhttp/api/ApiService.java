package com.cxz.sample.rxeasyhttp.api;

import com.cxz.sample.Bean;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created by chenxz on 2018/7/1.
 */

public interface ApiService {

    @GET("article/list/0/json")
    Observable<ApiResponse<Bean>> getArticles();

}
