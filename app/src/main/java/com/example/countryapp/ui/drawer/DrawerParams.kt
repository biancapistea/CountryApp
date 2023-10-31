package com.example.countryapp.ui.drawer

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.countryapp.R
import com.example.countryapp.ui.navigation.Destinations

data class AppDrawerItemInfo<T>(
    val drawerOption: T,
    @StringRes val title: Int,
    @DrawableRes val drawableId: Int,
)

// list of the buttons
object DrawerParams {
    val drawerButtons = arrayListOf(
        AppDrawerItemInfo(
            Destinations.Home,
            R.string.drawer_home,
            R.drawable.ic_home
        ),
        AppDrawerItemInfo(
            Destinations.Dashboard,
            R.string.drawer_dashboard,
            R.drawable.ic_quiz
        ),
        AppDrawerItemInfo(
            Destinations.LearnCountries,
            R.string.learn_and_train,
            R.drawable.ic_training
        ),
        AppDrawerItemInfo(
            Destinations.HangmanGameDashboard,
            R.string.play_a_game_learn,
            R.drawable.ic_hangman_game
        )
    )
}