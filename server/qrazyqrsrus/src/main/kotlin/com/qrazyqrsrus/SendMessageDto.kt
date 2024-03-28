package com.qrazyqrsrus

import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification

//we define the data transfer object that will represent the data passed to the server that encodes the notification
//this idea was taken from Phillipp Lackner (https://www.youtube.com/@PhilippLackner)
//this was adapted from his video https://www.youtube.com/watch?v=q6TL2RyysV4&ab_channel=PhilippLackner, Accessed Mar. 23rd, 2024

data class SendMessageDto(
    val to: String?,
    val notification: NotificationBody,
    val topic: String?
)


data class NotificationBody(
    val title: String,
    val body: String,
    val topic: String
)

//this function is used to convert a data transfer obejct into a Message object used by the firebase admin sdk to send push notifications
//this idea was taken from Phillipp Lackner (https://www.youtube.com/@PhilippLackner)
//this was adapted from his video https://www.youtube.com/watch?v=q6TL2RyysV4&ab_channel=PhilippLackner, Accessed Mar. 23rd, 2024
fun SendMessageDto.toMessage(): Message {
    return Message.builder()
        .setNotification(
            Notification.builder()
                .setTitle(notification.title)
                .setBody(notification.body)
                .build()
        )
        .apply {
            if(to == null) {
                setTopic(topic)
            } else {
                setToken(to)
            }
        }
        .build()
}