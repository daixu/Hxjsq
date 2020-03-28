package com.shangame.hxjsq.net;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.reactivestreams.Publisher;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.SingleTransformer;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

import static java.lang.String.format;

/**
 * Created by Eric on 2017/1/16.
 */

/**
 * 网络请求使用开源Retrofit okhttp 框架
 */
public class RetrofitManager {

    private ApiServer apiServer;

    public ApiServer getApiServer() {
        return apiServer;
    }

    private void createApi() {
        createBannerApi(sRetrofit);
    }

    private void createBannerApi(Retrofit retrofit) {
        apiServer = retrofit.create(ApiServer.class);
    }

    private static volatile OkHttpClient sOkHttpClient;

    private static Retrofit sRetrofit;
    private static RetrofitManager retrofitManager;

    final static Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            .serializeNulls()
            .create();

    public RetrofitManager() {
        sRetrofit = new Retrofit.Builder()
                .client(getOkHttpClient())
                .baseUrl("https://asyx.anmaa.com/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        createApi();
    }

    private static final int CONN_TIMEOUT = 15;
    private static final int READ_TIMEOUT = 20;
    private static final int WRITE_TIMEOUT = 20;

    private OkHttpClient getOkHttpClient() {
        if (sOkHttpClient == null) {

            HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor(new HttpLogger());
            logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            sOkHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(logInterceptor)
//                    .cookieJar(new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(App.getContext())))
                    .connectTimeout(CONN_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                    .build();
        }
        return sOkHttpClient;
    }

    public static RetrofitManager getInstance() {
        if (retrofitManager == null) {
            retrofitManager = new RetrofitManager();
        }
        return retrofitManager;
    }

    private static class HttpLogger implements HttpLoggingInterceptor.Logger {

        private StringBuilder mMessage = new StringBuilder();

        @Override
        public void log(String message) {
            // 请求或者响应开始
            if (message.startsWith("--> POST")) {
                mMessage.delete(0, mMessage.length());
            }
            mMessage.append(message.concat("\n"));
            // 请求或者响应结束，打印整条日志
            if (message.startsWith("<-- END HTTP")) {
                Timber.tag("Escort").e(format("mMessage=%s", mMessage));
                mMessage.delete(0, mMessage.length());
            }
        }
    }

    public static final ObservableTransformer ERROR_TRANSFORMER =
            new ObservableTransformer() {
                @Override
                public ObservableSource apply(Observable upstream) {
                    return upstream.onErrorResumeNext(new HttpResponseFunc());
                }
            };

    @SuppressWarnings("unchecked")
    public static <T> ObservableTransformer<T, T> composeError() {
        return (ObservableTransformer<T, T>) ERROR_TRANSFORMER;
    }

    public static final FlowableTransformer ERROR_TRANSFORMER_BACKPRESSURE = new FlowableTransformer() {
        @Override
        public Publisher apply(Flowable upstream) {
            return upstream.onErrorResumeNext(new HttpResponseFuncBackPressure());
        }
    };

    public static final SingleTransformer ERROR_TRANSFORMER_SINGLE = new SingleTransformer() {
        @Override
        public SingleSource apply(Single upstream) {
            return upstream.onErrorResumeNext(new HttpResponseFuncSingle());
        }
    };

    @SuppressWarnings("unchecked")
    public static <T> FlowableTransformer<T, T> composeBackpressureError() {
        return (FlowableTransformer<T, T>) ERROR_TRANSFORMER_BACKPRESSURE;
    }

    @SuppressWarnings("unchecked")
    public static <T> SingleTransformer<T, T> composeSingleError() {
        return (SingleTransformer<T, T>) ERROR_TRANSFORMER_SINGLE;
    }

    public static class HttpResponseFunc implements Function<Throwable, Observable> {
        @Override
        public Observable apply(Throwable throwable) throws Exception {
            return Observable.error(ExceptionHandle.handleException(throwable));
        }
    }

    public static class HttpResponseFuncBackPressure implements Function<Throwable, Flowable> {
        @Override
        public Flowable apply(@NonNull Throwable throwable) throws Exception {
            return Flowable.error(ExceptionHandle.handleException(throwable));
        }
    }

    public static class HttpResponseFuncSingle implements Function<Throwable, Single> {
        @Override
        public Single apply(@NonNull Throwable throwable) throws Exception {
            return Single.error(ExceptionHandle.handleException(throwable));
        }
    }

}