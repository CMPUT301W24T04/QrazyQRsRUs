package com.example.qrazyqrsrus;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface FcmApi {
    @POST("/send")
    public Call<MessageSentResponse> sendMessage(@Body SendMessageDto body);

    @POST("/broadcast")
    public Call<MessageSentResponse> broadcast(@Body SendMessageDto body);
}
