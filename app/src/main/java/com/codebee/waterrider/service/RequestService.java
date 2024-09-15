package com.codebee.waterrider.service;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RequestService {

    public static Retrofit retrofit;

    public static WaterAppService getRequestService() {

        if (retrofit == null) {

            retrofit = new Retrofit.Builder()
                    .baseUrl("http://192.168.8.145:8080/pureWaterAdmin/").addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        WaterAppService waterAppService = retrofit.create(WaterAppService.class);
        return waterAppService;
    }

    public static String getContectPath(){

        return "http://192.168.8.145:8080/pureWaterAdmin";
    }
}
