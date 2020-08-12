package com.twmicro.infochat

import com.vk.api.sdk.client.VkApiClient
import com.vk.api.sdk.httpclient.HttpTransportClient

object Commands {
    val commands: ArrayList<IBotCommand> = ArrayList()
    val vk: VkApiClient = VkApiClient(HttpTransportClient.getInstance())

    val testCommand: IBotCommand = command(object : IBotCommand{
        override fun getName(): String {
            return "/тест"
        }

        override fun handle(peerId: Int?, args: String) {
            vk.messages().send(actor).peerId(peerId).randomId(
                rand.nextInt(999999)).message("Я работаю!").execute()
        }

    })

    private fun command(command: IBotCommand) : IBotCommand {
        commands.add(command)
        return command
    }
}