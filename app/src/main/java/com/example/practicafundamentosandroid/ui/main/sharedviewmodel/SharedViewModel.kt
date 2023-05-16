package com.example.practicafundamentosandroid.ui.main.sharedviewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import  com.example.practicafundamentosandroid.model.data.Hero
import  com.example.practicafundamentosandroid.model.dto.HeroDto
import  com.example.practicafundamentosandroid.utils.BASE_URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request

class SharedViewModel : ViewModel() {

    private val _stateHeroes = MutableStateFlow<StateHeroes>(StateHeroes.Loading)
    val stateHeroes: StateFlow<StateHeroes> = _stateHeroes

    private val _stateHeroDetails = MutableStateFlow<StateHeroDetails>(StateHeroDetails.Idle)
    val stateHeroDetails: StateFlow<StateHeroDetails> = _stateHeroDetails

    private var heroList = mutableListOf<Hero>()

    fun heroesSynced() = heroList.isNotEmpty()

    fun setHeroes(heroes: List<Hero>) {
        heroList.clear()
        heroList.addAll(heroes)
        _stateHeroes.value = StateHeroes.OnHeroesReceived(heroes)
    }

    fun downloadHeroes(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            if (heroList.isEmpty()) {
                val client = OkHttpClient()
                val url = "${BASE_URL}heros/all"
                val formBody = FormBody.Builder()
                    .add("name", "")
                    .build()
                val request = Request.Builder()
                    .url(url)
                    .post(formBody)
                    .addHeader("Authorization", "Bearer $token")
                    .build()
                val call = client.newCall(request)

                val response = call.execute()
                if (response.isSuccessful) {
                    try {
                        val heroesDto: Array<HeroDto> =
                            Gson().fromJson(response.body?.string(), Array<HeroDto>::class.java)
                        val heroes = heroesDto.map { Hero(it.id, it.photo, it.name) }
                        setHeroes(heroes)
                    } catch (ex: Exception) {
                        _stateHeroes.value = StateHeroes.Error(ex.message.toString())
                    }
                } else {
                    _stateHeroes.value = StateHeroes.Error(response.message)
                }
            } else {
                _stateHeroes.value = StateHeroes.OnHeroesReceived(heroList)
            }
        }
    }

    fun selectHero(hero: Hero) {
        heroList.forEach {
            it.isSelected = false
        }
        hero.isSelected = true
        _stateHeroes.value = StateHeroes.OnHeroSelected(hero)
        _stateHeroes.value = StateHeroes.Idle
        _stateHeroDetails.value = StateHeroDetails.OnHeroSelected(hero)
    }

    fun healAllHeroes() {
        heroList.forEach {
            it.currentHealth = it.maxHealth
        }
        _stateHeroes.value = StateHeroes.OnHeroesUpdated
    }

    fun healHero() {
        heroList.firstOrNull() {
            it.isSelected
        }?.let {
            it.receiveHeal()
            updateHero(it)
        }
    }

    fun damageHero() {
        heroList.firstOrNull() {
            it.isSelected
        }?.let {
            it.receiveDamage()
            updateHero(it)
        }
    }

    private fun updateHero(hero: Hero) {
        _stateHeroDetails.value = if (hero.isAlive()) {
            _stateHeroDetails.value = StateHeroDetails.OnHeroUpdated(hero)
            StateHeroDetails.Idle
        } else {
            StateHeroDetails.OnHeroDied
        }
    }

    sealed class StateHeroes {
        data class OnHeroSelected(val hero: Hero) : StateHeroes()
        data class OnHeroesReceived(val heroes: List<Hero>) : StateHeroes()
        data class Error(val message: String) : StateHeroes()
        object Loading : StateHeroes()
        object OnHeroesUpdated : StateHeroes()
        object Idle : StateHeroes()
    }

    sealed class StateHeroDetails {
        object Idle : StateHeroDetails()
        data class OnHeroSelected(val heroSelected: Hero) : StateHeroDetails()
        data class OnHeroUpdated(val heroSelected: Hero) : StateHeroDetails()
        object OnHeroDied : StateHeroDetails()
        data class Error(val message: String) : StateHeroDetails()
    }

}