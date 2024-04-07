package com.qrazyqrsrus.notifications;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

//we define a function that will attempt to route the http request, and make the corresponding message to firebase
//this idea was taken from Phillipp Lackner (https://www.youtube.com/@PhilippLackner) and adapted to Java code
//this was adapted from his video https://www.youtube.com/watch?v=q6TL2RyysV4&ab_channel=PhilippLackner, Accessed Mar. 23rd, 2024
@RestController
public class SendMessageController {

    @PostMapping("/send")
    public void send(@RequestBody SendMessageDto sendMessageDto) {
        try {
            FirebaseMessaging.getInstance().send(sendMessageDto.toMessage());
        } catch (FirebaseMessagingException e) {

        }

    }

    @PostMapping("/broadcast")
    public void broadcast(@RequestBody SendMessageDto sendMessageDto) {
        try {
            FirebaseMessaging.getInstance().send(sendMessageDto.toMessage());
        } catch (FirebaseMessagingException e) {

        }

    }
}
