package com.example.practicafundamentosandroid.ui.main.sharedviewmodel

import com.example.practicafundamentosandroid.MainCoroutineRule
import com.example.practicafundamentosandroid.model.data.Hero
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SharedViewModelTest {


    private val viewModel = SharedViewModel()
    private fun setHeroes() {

        viewModel.setHeroes(
            listOf(
                Hero(
                    "1",
                    "",
                    "Test1"
                ),
                Hero(
                    "2",
                    "",
                    "Test2"
                ),
            )
        )
    }
    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }

    @Test
    fun addCloseable() {
    }

    @Test
    fun onCleared() {
    }

    @Test
    fun clear() {
    }

    @Test
    fun setTagIfAbsent() {
    }

    @Test
    fun getTag() {
    }

    @Test
    fun `getStateHeroes  check if setHeroes sets the heroes correctly`() {
        val expectedValue = listOf(
            Hero(
                "1",
                "",
                "Test1"
            ),
            Hero(
                "2",
                "",
                "Test2"
            ),
        )
        setHeroes()
        val value = viewModel.stateHeroes.value as SharedViewModel.StateHeroes.OnHeroesReceived
        assertArrayEquals(expectedValue.toTypedArray(), value.heroes.toTypedArray())
    }

    @Test
    fun getStateHeroDetails() {
    }

    @Test
    fun heroesSynced() {
    }



    @Test
    fun downloadHeroes() {
    }

    @Test
    fun `selectHero check if the hero is selected correctly`() {


            val expectedValue = Hero(
                "1",
                "",
                "Test1"
            )
            viewModel.selectHero(expectedValue)
            val actualValue = viewModel.stateHeroDetails.value as SharedViewModel.StateHeroDetails.OnHeroSelected
            assertEquals(expectedValue, actualValue.heroSelected)
        }




    @Test
    fun healAllHeroes() {
    }

    @Test
    fun healHero() {
    }



    @Test
    fun damageHero() {






    }
}

