package com.example.qrazyqrsrus;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

//we use Retrofit to create our Java interface out of the HTTP Api defined on the backend
//this definition was taken from Phillipp Lackner (https://www.youtube.com/@PhilippLackner)
//this was adapted from his video https://www.youtube.com/watch?v=q6TL2RyysV4&ab_channel=PhilippLackner, Accessed Mar. 23rd, 2024
/**
 * Interface for defining HTTP operations using Retrofit for Firebase Cloud Messaging (FCM).
 * This interface provides methods for sending messages directly to a specific device or broadcasting
 * messages to multiple devices.
 *
 * Definitions and usage examples are adapted from educational content by Philipp Lackner.
 * See more at Philipp Lackner's YouTube channel: https://www.youtube.com/@PhilippLackner.
 */
public interface FcmApi {

    /**
     * Sends a message to a specific device or to a group of devices subscribed to a topic.
     * This method uses the POST HTTP method to send the message to the "/send" endpoint.
     *
     * @param body The {@link SendMessageDto} object containing the message details such as
     *             the recipient's token or topic, message title, and message body.
     * @return A {@link Call} object with a {@link MessageSentResponse} indicating the result of
     *         the send operation.
     */
    @POST("/send")
    public Call<MessageSentResponse> sendMessage(@Body SendMessageDto body);


    /**
     * Broadcasts a message to all devices subscribed to a specific topic.
     * This method uses the POST HTTP method to send the broadcast message to the "/broadcast" endpoint.
     *
     * @param body The {@link SendMessageDto} object containing the message details such as
     *             the topic, message title, and message body for the broadcast.
     * @return A {@link Call} object with a {@link MessageSentResponse} indicating the result of
     *         the broadcast operation.
     */
    @POST("/broadcast")
    public Call<MessageSentResponse> broadcast(@Body SendMessageDto body);
}
