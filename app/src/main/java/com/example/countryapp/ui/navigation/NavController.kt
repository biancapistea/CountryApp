package com.example.countryapp.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import com.example.countryapp.R
import com.example.countryapp.ui.components.dialog.IncorrectQuizResultDialog
import com.example.countryapp.ui.components.dialog.SuccessResultQuizDialog
import com.example.countryapp.ui.dashboard.DashboardQuizType
import com.example.countryapp.ui.dashboard.DashboardScreen
import com.example.countryapp.ui.drawer.AnimatedDrawer
import com.example.countryapp.ui.drawer.AnimatedDrawerState
import com.example.countryapp.ui.drawer.DrawerParams
import com.example.countryapp.ui.drawer.MenuOptions
import com.example.countryapp.ui.drawer.rememberAnimatedDrawerState
import com.example.countryapp.ui.game.GameDashboardScreen
import com.example.countryapp.ui.game.GameScreen
import com.example.countryapp.ui.home.HomeScreen
import com.example.countryapp.ui.learn.LearnScreen
import com.example.countryapp.ui.learn.countrydetails.CountryDetailsScreen
import com.example.countryapp.ui.models.Country
import com.example.countryapp.ui.models.Name
import com.example.countryapp.ui.quiz.QuizScreen
import com.example.countryapp.ui.quiz.QuizViewModel
import com.example.countryapp.ui.quiz.selectregion.RegionQuizScreen
import com.example.countryapp.ui.splash.SplashScreen
import com.example.countryapp.ui.utils.noRippleClickable
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavController(
    navController: NavHostController = rememberNavController(),
    drawerState: AnimatedDrawerState = rememberAnimatedDrawerState(
        drawerWidth = 280.dp,
    ),
    startDestination: String = Destinations.Splash.name
) {
    Box(modifier = Modifier.fillMaxSize()) {
        val scope = rememberCoroutineScope()
        AnimatedDrawer(
            modifier = Modifier.fillMaxSize(),
            state = drawerState,
            drawerContent = {
                MenuOptions(
                    modifier = Modifier.fillMaxSize(),
                    onCloseClick = {
                        scope.launch { drawerState.close() }
                    },
                    drawerState = drawerState,
                    defaultPick = Destinations.Home,
                    menuItems = DrawerParams.drawerButtons,
                    onClick = { onUserPickedOption ->
                        // when user picks the path - navigates to new one
                        navController.navigate(onUserPickedOption.name)
                    }
                )
            },
            content = {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                Surface(Modifier.noRippleClickable { scope.launch { drawerState.close() } }) {
                    if (currentRoute == Destinations.Home.name
                        || currentRoute == Destinations.GameDashboard.name
                        || currentRoute == Destinations.Dashboard.name
                        || currentRoute == Destinations.LearnCountries.name
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .zIndex(2f)
                        ) {
                            Image(painter = painterResource(R.drawable.ic_menu),
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(top = 24.dp, start = 12.dp)
                                    .zIndex(2f)
                                    .clickable {
                                        scope.launch {
                                            drawerState.open()
                                        }
                                    })
                        }
                    }
                    NavHost(
                        navController = navController,
                        startDestination = startDestination
                    ) {
                        composable(Destinations.Splash.name) {
                            SplashScreen(
                                onNavigateToDashboard = { navController.navigate(Destinations.Home.name) },
                                viewModel = hiltViewModel()
                            )
                        }
                        composable(Destinations.Home.name) {
                            HomeScreen(
                                onNavigateToDashboard = { navController.navigate(Destinations.Dashboard.name) },
                                onNavigateToLearnCountries = {
                                    navController.navigate(
                                        Destinations.LearnCountries.name
                                    )
                                },
                                onNavigateToPlayScreen = { navController.navigate(Destinations.GameDashboard.name) }
                            )
                        }
                        composable(Destinations.LearnCountries.name) {
                            LearnScreen(
                                viewModel = hiltViewModel(),
                                onCountryClick = { countryClicked ->
                                    navController.currentBackStackEntry?.savedStateHandle?.set(
                                        key = "countryClicked",
                                        value = countryClicked
                                    )
                                    navController.navigate(Destinations.CountryDetails.name)
                                }
                            )
                        }
                        composable(Destinations.CountryDetails.name) {
                            val countryClicked =
                                navController.previousBackStackEntry?.savedStateHandle?.get<Country>(
                                    "countryClicked"
                                )
                            CountryDetailsScreen(
                                viewModel = hiltViewModel(),
                                country = countryClicked ?: Country(Name("", "")),
                                onBackPressed = {
                                    navController.popBackStack(
                                        route = Destinations.LearnCountries.name,
                                        inclusive = false
                                    )
                                }
                            )
                        }
                        composable(Destinations.Dashboard.name) {
                            DashboardScreen(
                                viewModel = hiltViewModel(),
                                onDashboardTypePressed = { type ->
                                    if (type.name == DashboardQuizType.GENERAL_ASPECTS.name) {
                                        navController.navigate("${Destinations.Quiz.name}/${type.name}/{}")
                                    } else {
                                        navController.navigate("${Destinations.RegionType.name}/${type.name}")
                                    }
                                }
                            )
                        }
                        composable("${Destinations.Game.name}/{dashboardType}") {
                            GameScreen(
                                gameViewModel = hiltViewModel(),
                                onPopBack = { navController.popBackStack() }
                            )
                        }
                        composable(Destinations.GameDashboard.name) {
                            GameDashboardScreen(
                                viewModel = hiltViewModel(),
                                onDashboardTypePressed = { type ->
                                    navController.navigate("${Destinations.Game.name}/${type.name}")
                                }
                            )
                        }
                        composable("${Destinations.RegionType.name}/{dashboardType}") { backStackEntry ->
                            val dashboardType =
                                backStackEntry.arguments?.getString("dashboardType")
                            RegionQuizScreen(
                                viewModel = hiltViewModel(),
                                onRegionTypePressed = { selectedRegionType ->
                                    navController.navigate("${Destinations.Quiz.name}/${dashboardType}/${selectedRegionType.name}")
                                },
                                onBackPressed = { navController.popBackStack() }
                            )
                        }
                        composable("${Destinations.Quiz.name}/{dashboardType}/{selectedRegionType}") { backStackEntry ->
                            val quizViewModel: QuizViewModel = hiltViewModel(backStackEntry)
                            QuizScreen(
                                viewModel = quizViewModel,
                                onExitQuizPressed = { navController.navigate(Destinations.Dashboard.name) },
                                onNavigateToSuccessResultQuizDialog = {
                                    navController.navigate(Destinations.SuccessResultQuizDialog.name)
                                },
                                onNavigateToIncorrectQuizResultDialog = {
                                    navController.navigate(
                                        Destinations.IncorrectQuizResultDialog.name
                                    )
                                }
                            )
                        }
                        dialog(Destinations.SuccessResultQuizDialog.name) {
                            SuccessResultQuizDialog(
                                onGoToDashboardPressed = { navController.navigate(Destinations.Dashboard.name) }
                            )
                        }
                        dialog(Destinations.IncorrectQuizResultDialog.name) {
                            val quizViewModel: QuizViewModel =
                                hiltViewModel(navController.previousBackStackEntry!!)
                            IncorrectQuizResultDialog(
                                onGoToDashboardPressed = { navController.navigate(Destinations.Dashboard.name) },
                                quizViewModel = quizViewModel,
                                onDismissDialog = { navController.popBackStack() }
                            )
                        }
                    }
                }
            })
    }
}