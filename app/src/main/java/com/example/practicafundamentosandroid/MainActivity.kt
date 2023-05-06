package com.example.practicafundamentosandroid

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.practicafundamentosandroid.R.dimen
import com.example.practicafundamentosandroid.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private val numRandom = Random.nextInt()

    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.textViewLogin.text ="Bonjour"
        binding.loginButton.text = "Acceder"


        println("Esto es un log")
        val miTag="MiTag"
        Log.v(miTag, "Esto es un log con verbose")//verbose son los mas simples y cutres
        Log.d(miTag, "Esto es un log con debug")//debug
        Log.i(miTag, "Esto es un log ocn information")//information
        Log.w(miTag, "Esto es un log con warning")

        loadFromPreferences()
        val text = getString(R.string.toast_text)
        val size = resources.getDimension(R.dimen.icon)
        val foto = ContextCompat.getDrawable(this, R.drawable.dragonballtransparentlogo)
        val color = ContextCompat.getColor(this, R.color.red)
        Toast.makeText(this, text , Toast.LENGTH_LONG).show()
        //opcion clasica pero poco efectiva
       // setContentView(R.layout.activity_main)
        //  val textView = findViewById<TextView>(R.id.textViewLogin)
       // textView.text ="Hey!"
    }

    private fun loadFromPreferences() {

        // Equivale a línea de abajo
        // val shared = getPreferences(Context.MODE_PRIVATE)
        // val numOld = shared.getInt("MiInteger", 0)
        //Log.w("Tag", "$numOld")

        // Revisar: let, apply, with, run
        // let ej
        // var a = 5
        // a.let {it}
        // el it es el 5
        //El apply es igual que el let pero sin escribir el it
        // getPreferences(Context.MODE_PRIVATE).let {
        //            Log.w("Tag", "${it.getInt("MiInteger", 0)}")
        //        }


        getPreferences(Context.MODE_PRIVATE).apply {
            Log.w("Tag", "${getInt("MiInteger", 0)}")
        }

    }


    override fun onStop() {
        saveFromPreferences()
        super.onStop()
    }

    private fun saveFromPreferences() {
    // Equivale a línea de abajo
    // val shared = getPreferences(Context.MODE_PRIVATE)
    // val sharedPreferencesEditable = shared.edit()
    // sharedPreferencesEditable.putInt("MiInteger", numRandom)
    // sharedPreferencesEditable.apply()
    getPreferences((Context.MODE_PRIVATE)).edit().apply {
        putInt("MiInteger", numRandom)
        apply()
    }
}
}