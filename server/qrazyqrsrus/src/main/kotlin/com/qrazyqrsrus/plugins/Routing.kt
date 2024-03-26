package com.qrazyqrsrus.plugins

import com.qrazyqrsrus.sendNotification
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        sendNotification()
    }
}
