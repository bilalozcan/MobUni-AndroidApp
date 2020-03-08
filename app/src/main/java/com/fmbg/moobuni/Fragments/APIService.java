package com.fmbg.moobuni.Fragments;

import com.fmbg.moobuni.Notifications.MyResponse;
import com.fmbg.moobuni.Notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAMC3g0gY:APA91bGkmMyZiRTB-wJv8LBbJCiYuGSlAmjaZ3YM0zlPJtSSuQukztr9mE8cTFqT2XzhE96L-5fkwAQ67UqBenBb6dhwUqSdzN6u58-dCX8AxZLu9N2TGRZxAbyYaaCiOY2xQ-7lGQrj"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
