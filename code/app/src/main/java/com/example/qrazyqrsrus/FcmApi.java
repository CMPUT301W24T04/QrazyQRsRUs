package com.example.qrazyqrsrus;
// This fragment holds the API used for sending notifications to users
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

//we use Retrofit to create our Java interface out of the HTTP Api defined on the backend
//this definition was taken from Phillipp Lackner (https://www.youtube.com/@PhilippLackner)
//this was adapted from his video https://www.youtube.com/watch?v=q6TL2RyysV4&ab_channel=PhilippLackner, Accessed Mar. 23rd, 2024
public interface FcmApi {
    @POST("/send")
    public Call<MessageSentResponse> sendMessage(@Body SendMessageDto body);

    @POST("/broadcast")
    public Call<MessageSentResponse> broadcast(@Body SendMessageDto body);
}
