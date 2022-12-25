package com.example.doctorapp.SetNotification;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiService {

    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAT5_5-Kg:APA91bFXb-JrBSaRusm-vc19w8vJk3y35f7WEJDshiSNtNtuxrFXdeUpljkhNN70CRS40M5HZLr5r6z5gU-A_AyuJn4DQ_cB5JM5ZYFlBN6ptJztPc6uUqT-wnjsV1hdZoagwwalzWbS"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
