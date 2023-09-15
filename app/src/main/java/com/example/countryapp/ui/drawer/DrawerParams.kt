package com.example.countryapp.ui.drawer

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.countryapp.R
import com.example.countryapp.ui.navigation.Destinations
import kotlinx.coroutines.launch

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
            R.drawable.ic_home
        ),
        AppDrawerItemInfo(
            Destinations.LearnCountries,
            R.string.learn_and_train,
            R.drawable.ic_home
        ),
        AppDrawerItemInfo(
            Destinations.GameDashboard,
            R.string.play_a_game_learn,
            R.drawable.ic_home
        )
    )
}