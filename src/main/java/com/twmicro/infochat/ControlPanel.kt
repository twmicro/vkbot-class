package com.twmicro.infochat

object ControlPanel {
    fun handleCommand(signature: String, peerId: Int?) : Boolean {
        for(c in Commands.commands) {
            if(signature.contains(c.getName())) {
                c.handle(peerId, signature.substring(signature.indexOf(c.getName()) + c.getName().count()))
                return true
            }
        }
        return false
    }
}