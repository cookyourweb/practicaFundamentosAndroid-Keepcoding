package com.example.practicafundamentosandroid.ui.main.fight

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import  com.example.practicafundamentosandroid.R
import  com.example.practicafundamentosandroid.databinding.FragmentHeroDetailsBinding
import  com.example.practicafundamentosandroid.model.data.Hero
import  com.example.practicafundamentosandroid.ui.main.heroes.HeroesFragment
import  com.example.practicafundamentosandroid.ui.main.sharedviewmodel.SharedViewModel
import kotlinx.coroutines.launch

class HeroDetailsFragment : Fragment() {

    private val viewModel: SharedViewModel by activityViewModels()
    private lateinit var binding: FragmentHeroDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHeroDetailsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        setObservers()
    }


    private fun setObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.stateHeroDetails.collect{ stateHeroDetails ->
                when(stateHeroDetails) {
                    is SharedViewModel.StateHeroDetails.Error -> {
                        Toast.makeText(requireContext(), stateHeroDetails.message, Toast.LENGTH_LONG).show()
                        parentFragmentManager.popBackStack()
                    }
                    is SharedViewModel.StateHeroDetails.OnHeroUpdated -> {
                        displayHero(stateHeroDetails.heroSelected)
                    }
                    is SharedViewModel.StateHeroDetails.OnHeroDied -> {
                        Toast.makeText(requireContext(), getString(R.string.fight_end), Toast.LENGTH_LONG).show()
                        parentFragmentManager.popBackStack()
                    }
                    SharedViewModel.StateHeroDetails.Idle -> Unit
                    is SharedViewModel.StateHeroDetails.OnHeroSelected -> displayHero(stateHeroDetails.heroSelected)
                }
            }
        }
    }

    private fun displayHeroCounter(hero: Hero) {
        activity?.getPreferences(Context.MODE_PRIVATE)?.let{
            binding.tvCounter.text = (it.getInt("${HeroesFragment.TAG_HERO_SELECTED_}${hero.id}", 0)).toString()
        }
    }

    private fun displayHero(heroSelected: Hero) {
        with(binding) {
            Glide
                .with(root)
                .load(heroSelected.photo)
                .centerCrop()
                .placeholder(R.drawable.background_element)
                .into(ivHeroSelected)
            progressbarHealthHeroSelected.progress = heroSelected.currentHealth
            displayHeroCounter(heroSelected)
        }

    }

    private fun setListeners() {
        binding.btnHeal.setOnClickListener {
            viewModel.healHero()
        }
        binding.btnDamage.setOnClickListener {
            viewModel.damageHero()
        }
    }
}