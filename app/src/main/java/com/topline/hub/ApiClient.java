package com.topline.hub;

import static com.topline.hub.api.util.AppConstants.BASE_URL;
import static com.topline.hub.api.util.AppConstants.CONNECT_TIMEOUT;
import static com.topline.hub.api.util.AppConstants.READ_TIMEOUT;
import static com.topline.hub.api.util.AppConstants.WRITE_TIMEOUT;

import com.topline.hub.api.interceptor.AccessTokenInterceptor;
import com.topline.hub.api.interceptor.AuthInterceptor;
import com.topline.hub.api.services.STKPushService;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static final String BASE_URLS = "https://dialadrinkltdco.ke/dial/mobile/customer_mobile/";

    public static Retrofit retrofit;
    //private Retrofit retrofit;
    private boolean isDebug;
    private boolean isGetAccessToken;
    private String mAuthToken;
    private HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();



    public static Retrofit getApiClient(){

        if (retrofit==null){

            retrofit = new  Retrofit.Builder()
                    .baseUrl(BASE_URLS)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;

    }

    public ApiClient setIsDebug(boolean isDebug) {
        this.isDebug = isDebug;
        return this;
    }
    /**
     * Helper method used to set the authenication Token
     *
     * @param authToken token from api
     */
    public ApiClient setAuthToken(String authToken) {
        mAuthToken = authToken;
        return this;
    }

    /**
     * Helper method used to determine if get token enpoint has been invoked. This should be called
     * only when requesting of an accessToken
     *
     * @param getAccessToken {@link Boolean}
     */
    public ApiClient setGetAccessToken(boolean getAccessToken) {
        isGetAccessToken = getAccessToken;
        return this;
    }
    /**
     * Configure OkHttpClient
     *
     * @return OkHttpClient
     */
    private OkHttpClient.Builder okHttpClient() {
        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
        okHttpClient
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(httpLoggingInterceptor);

        return okHttpClient;
    }

    /**
     * Return the current {@link Retrofit} instance. If none exists (first call, API key changed),
     * builds a new one.
     * <p/>
     * When building, sets the endpoint and a {@link HttpLoggingInterceptor} which adds the API key as query param.
     */

    private Retrofit getRestAdapter() {

        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(BASE_URL);
        builder.addConverterFactory(GsonConverterFactory.create());

        if (isDebug) {
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        }

        OkHttpClient.Builder okhttpBuilder = okHttpClient();

        if (isGetAccessToken) {
            okhttpBuilder.addInterceptor(new AccessTokenInterceptor());
        }

        if (mAuthToken != null && !mAuthToken.isEmpty()) {
            okhttpBuilder.addInterceptor(new AuthInterceptor(mAuthToken));
        }

        builder.client(okhttpBuilder.build());

        retrofit = builder.build();

        return retrofit;
    }

    /**
     * Create service instance.
     *
     * @return STKPushService Service.
     */
    public STKPushService mpesaService() {
        return getRestAdapter().create(STKPushService.class);
    }

}
