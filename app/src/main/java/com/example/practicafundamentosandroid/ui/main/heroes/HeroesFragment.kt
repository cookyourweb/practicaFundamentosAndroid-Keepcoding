package com.example.practicafundamentosandroid.ui.main.heroes

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import  com.example.practicafundamentosandroid.R
import  com.example.practicafundamentosandroid.data.repository.User
import  com.example.practicafundamentosandroid.databinding.FragmentHeroesBinding
import  com.example.practicafundamentosandroid.model.data.Hero
import  com.example.practicafundamentosandroid.ui.main.MainActivity
import  com.example.practicafundamentosandroid.ui.main.LoadingManager
import  com.example.practicafundamentosandroid.ui.main.heroes.adapter.HeroesAdapter
import  com.example.practicafundamentosandroid.ui.main.heroes.adapter.MainAdapterCallback
import  com.example.practicafundamentosandroid.ui.main.sharedviewmodel.SharedViewModel
import kotlinx.coroutines.launch

class HeroesFragment : Fragment(), MainAdapterCallback {

    private val viewModel: SharedViewModel by activityViewModels()
    private lateinit var binding: FragmentHeroesBinding
    private val adapter = HeroesAdapter(this)

    companion object {
        const val TAG_HEROES_LIST = "TAG_HEROES_LIST"
        const val TAG_HERO_SELECTED_ = "TAG_HERO_SELECTED_"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHeroesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        setObservers()
        setListeners()
        loadHeroes()
    }

    private fun setListeners() {
        binding.btnHealth.setOnClickListener{
            viewModel.healAllHeroes()
            Toast.makeText(requireContext(), getString(R.string.healed), Toast.LENGTH_LONG).show()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
    }

    private fun setAdapter() {
        with(binding){
            rvHeroes.layoutManager = LinearLayoutManager(requireContext())
            rvHeroes.adapter = adapter
        }
    }

    private fun showError() {
        Toast.makeText(requireContext(), getString(R.string.error_no_input), Toast.LENGTH_LONG).show()
        activity?.finish()
    }

    private fun setObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.stateHeroes.collect{ state ->
                when (state) {
                    is SharedViewModel.StateHeroes.OnHeroesReceived -> {
                        showLoading(false)
                        showHeroes(state.heroes)
                        storeHeroesOnSharedPreferences(state.heroes)
                    }
                    is SharedViewModel.StateHeroes.Error -> {
                        showLoading(false)
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_LONG).show()
                    }
                    is SharedViewModel.StateHeroes.Loading -> {
                        showLoading(true)
                    }
                    is SharedViewModel.StateHeroes.OnHeroSelected -> {
                        increaseHeroCounter(state.hero)
                        goToHeroDetails()
                    }
                    is SharedViewModel.StateHeroes.OnHeroesUpdated -> {
                        showLoading(false)
                        adapter.notifyDataSetChanged()
                    }
                    SharedViewModel.StateHeroes.Idle -> Unit
                }
            }
        }
    }

    private fun increaseHeroCounter(hero: Hero) {
        activity?.getPreferences(Context.MODE_PRIVATE)?.let{
            var counter = it.getInt("${TAG_HERO_SELECTED_}${hero.id}",0)
            counter++
            val preferencesEditable = it.edit()
            preferencesEditable.putInt("${TAG_HERO_SELECTED_}${hero.id}", counter)
            preferencesEditable.apply()
        }
    }

    private fun goToHeroDetails() {
        (activity as? MainActivity)?.goToHeroDetailsFragment()
    }

    private fun showHeroes(heroes: List<Hero>) {
        adapter.updateList(heroes)
    }

    private fun showLoading(loading: Boolean) {
        (activity as? LoadingManager)?.showLoading(loading)
    }

    override fun onHeroClicked(hero: Hero) {
        viewModel.selectHero(hero)
    }

    private fun storeHeroesOnSharedPreferences(heroes: List<Hero>) {
        activity?.getPreferences(Context.MODE_PRIVATE)?.let{
            val preferencesEditable = it.edit()
            preferencesEditable.putString(TAG_HEROES_LIST, Gson().toJson(heroes))
            preferencesEditable.apply()
        }
    }

    private fun loadHeroes() {
        if (!viewModel.heroesSynced())
            activity?.getPreferences(Context.MODE_PRIVATE)?.let{
                try {
                    val json = it.getString(TAG_HEROES_LIST, "")
                    val heroes: Array<Hero> = Gson().fromJson(json, Array<Hero>::class.java)
                    viewModel.setHeroes(heroes.toList())
                } catch (_: Exception) {
                    User.getToken(requireContext())?.let { token ->
                        viewModel.downloadHeroes(token)
                    } ?: showError()
                }
            }
    }
}