package com.twmicro.infochat

import kotlin.math.roundToLong

class Building(val name: String, val cost: Long, var funds: Long, var level: Int){
    fun upgrade(user: CGameUser) : Boolean {
        if(enoughToUpgrade(user)) {
            user.money -= upgradeCost(level + 1)
            level++
            funds *= 2
            return true
        }
        else return false
    }

    fun upgradeCost(level: Int) : Long{
        return if(level == 1) cost / 2 else (upgradeCost(level - 1) * 1.75).roundToLong()
    }

    fun enoughToUpgrade(user: CGameUser) : Boolean{
        return user.money >= cost && level < 5
    }

    fun enoughToBuy(user: CGameUser) : Boolean{
        return user.money >= cost
    }

    fun buy(user: CGameUser) {
        if(enoughToBuy(user)){
            user.money -= cost
            user.buildings.add(this)
        }
    }
}