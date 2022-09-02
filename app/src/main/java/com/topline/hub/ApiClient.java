package com.topline.hub;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    //private static final String BASE_URL = "http://geojakltd.co.ke/impulse/mobile/v1/";
    private static final String BASE_URL = "https://dialadrinkltdco.ke/dial/mobile/customer_mobile/";

    public static Retrofit retrofit;


    public static Retrofit getApiClient(){

        if (retrofit==null){

            retrofit = new  Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;

    }

}
