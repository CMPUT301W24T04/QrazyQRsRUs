package com.example.qrazyqrsrus;

import retrofit2.http.Body;
import retrofit2.http.POST;

public interface FcmApi {
    @POST("/send")
    public void sendMessage(@Body SendMessageDto body);

    @POST("/broadcast")
    public void broadcast(@Body SendMessageDto body);
}
