package com.shangame.hxjsq.net;

import com.shangame.hxjsq.pojo.SetLoginLogResp;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiServer {

    @GET("api/comPuting/set-LoginLog")
    Flowable<SetLoginLogResp> setLoginLog(@Query("device") String device, @Query("channel") int channel);
}
