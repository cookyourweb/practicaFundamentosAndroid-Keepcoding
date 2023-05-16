package com.example.practicafundamentosandroid.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import  com.example.practicafundamentosandroid.databinding.ActivityMainBinding
import  com.example.practicafundamentosandroid.ui.main.fight.HeroDetailsFragment
import  com.example.practicafundamentosandroid.ui.main.heroes.HeroesFragment


interface LoadingManager {
    fun showLoading(show: Boolean)
}
class MainActivity : AppCompatActivity(), LoadingManager {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            goToHeroesFragment()
        }
    }

    override fun showLoading(show: Boolean){
        binding.layoutLoading.root.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun goToHeroesFragment(){
        supportFragmentManager.beginTransaction()
            .replace(binding.mainContainer.id, HeroesFragment())
            .commit()
    }

    fun goToHeroDetailsFragment(){
        supportFragmentManager.beginTransaction()
            .replace(binding.mainContainer.id, HeroDetailsFragment())
            .addToBackStack(null)
            .commit()
    }
}