package com.doubleclick.b_safe.notification.Api;


import com.doubleclick.b_safe.model.MyResponse;
import com.doubleclick.b_safe.model.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAJlIvPfU:APA91bEUnQT9teB54QYH0JEaLjgHjBnu3VRzB67zywVQI9j0HOAhNXvPrv1HN6uGtumzHB_BvB7aMv-ZPUrZNxYR6sf0ebDADaXk6EEdpHlwj122ON1ljicv2G0673I8OcVcK670mNs2"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}

