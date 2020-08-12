package com.twmicro.infochat

import com.twmicro.infochat.Commands.vk
import com.vk.api.sdk.client.actors.GroupActor
import com.vk.api.sdk.exceptions.ApiException
import com.vk.api.sdk.exceptions.ClientException
import com.vk.api.sdk.objects.messages.Message
import com.vk.api.sdk.queries.messages.MessagesGetLongPollHistoryQuery
import java.lang.Thread.sleep
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.ServerSocket
import java.net.Socket
import java.util.*
import java.util.concurrent.Executors



var maxMsgId = 0
const val token = "6a89aab2232142462e2ac7ee310caf9445ac22cffabd46713e426a3d1a2a9683e4bba510640dfc2a56ef3"
const val groupId = 158048496
val actor = GroupActor(groupId, token)
var ts: Int = vk.messages().getLongPollServer(actor).execute().ts
val rand = Random()
var port = System.getenv("PORT") // Heroku
var socket: ServerSocket = ServerSocket(port.toInt())
var aliveCount: Int = 0

fun main() {
    println("Running server...")
    while(true) {
        try
        {
            if(aliveCount == 100) {
                aliveCount = 0
                socket.accept()
                InetAddress.getByName("127.0.0.1").isReachable(100)
            }
            val message: Message? = getMessage()
            if(message != null){
                println("Received message: ${message.text}")
                if(ControlPanel.handleCommand(message.text, message.peerId))
                    println("Handled command!")
            }
        }
        catch(e: Exception){
            e.printStackTrace()
        }
        sleep(300)
        aliveCount++
    }
}
fun getMessage(): Message? {
    val eventsQuery: MessagesGetLongPollHistoryQuery = vk.messages()
        .getLongPollHistory(actor)
        .ts(ts)
    if (maxMsgId > 0) {
        eventsQuery.maxMsgId(maxMsgId)
    }
    val messages: List<Message> = eventsQuery
        .execute()
        .messages.items
    if (messages.isNotEmpty()) {
        try {
            ts = vk.messages()
                .getLongPollServer(actor)
                .execute()
                .ts
        } catch (e: ClientException) {
            e.printStackTrace()
        }
    }
    if (messages.isNotEmpty() && !messages[0].isOut) {
        val messageId: Int = messages[0].id
        if (messageId > maxMsgId) {
            maxMsgId = messageId
        }
        return messages[0]
    }
    return null
}