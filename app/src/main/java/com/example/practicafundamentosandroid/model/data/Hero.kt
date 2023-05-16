package com.example.practicafundamentosandroid.model.data

import kotlin.random.Random

data class Hero(
    val id: String,
    val photo: String,
    val name: String,
    var currentHealth: Int = 100,
    val maxHealth: Int = 100,
) {

    var isSelected = false

    fun isAlive() = currentHealth > 0

    fun receiveDamage() {
        currentHealth -= Random.nextInt(10,60)
    }

    fun receiveHeal() {
        currentHealth += 20
    }
}
