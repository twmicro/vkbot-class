package com.twmicro.infochat

interface IBotCommand {
    fun getName() : String
    fun handle(peerId:Int?, args: String)
}