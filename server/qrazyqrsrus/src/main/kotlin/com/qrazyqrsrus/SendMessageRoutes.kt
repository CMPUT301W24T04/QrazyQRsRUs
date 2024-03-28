package com.qrazyqrsrus

import com.google.firebase.messaging.FirebaseMessaging
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receiveNullable
import io.ktor.server.response.respond
import io.ktor.server.routing.*

//we define a function that will attempt to route the http request, and make the corresponding message to firebase
//this idea was taken from Phillipp Lackner (https://www.youtube.com/@PhilippLackner)
//this was adapted from his video https://www.youtube.com/watch?v=q6TL2RyysV4&ab_channel=PhilippLackner, Accessed Mar. 23rd, 2024
fun Route.sendNotification(){
    route("/send"){
        post{
            val body = call.receiveNullable<SendMessageDto>() ?: kotlin.run{
                println("saddy face")
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
            println("happyy face")
            FirebaseMessaging.getInstance().send(body.toMessage())

            call.respond(HttpStatusCode.OK)
        }
    }
    route("/broadcast"){
        post{
            val body = call.receiveNullable<SendMessageDto>() ?: kotlin.run{
                println("sad face")
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
            println("hapapapap face")
            FirebaseMessaging.getInstance().send(body.toMessage())

            call.respond(HttpStatusCode.OK)
        }
    }
}