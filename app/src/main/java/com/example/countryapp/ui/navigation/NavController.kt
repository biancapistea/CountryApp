package com.example.countryapp.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import com.example.countryapp.ui.dashboard.DashboardQuizType
import com.example.countryapp.ui.dashboard.DashboardScreen
import com.example.countryapp.ui.drawer.AppDrawerContent
import com.example.countryapp.ui.drawer.DrawerParams
import com.example.countryapp.ui.game.GameDashboardScreen
import com.example.countryapp.ui.game.GameScreen
import com.example.countryapp.ui.home.HomeScreen
import com.example.countryapp.ui.learn.LearnScreen
import com.example.countryapp.ui.learn.countrydetails.CountryDetailsScreen
import com.example.countryapp.ui.models.Country
import com.example.countryapp.ui.models.Name
import com.example.countryapp.ui.quiz.IncorrectQuizResultDialog
import com.example.countryapp.ui.quiz.QuizScreen
import com.example.countryapp.ui.quiz.QuizViewModel
import com.example.countryapp.ui.quiz.SuccessResultQuizDialog
import com.example.countryapp.ui.quiz.selectregion.RegionQuizScreen
import com.example.countryapp.ui.splash.SplashScreen

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavController(
    navController: NavHostController = rememberNavController(),
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    startDestination: String = Destinations.Splash.name
) {
    Scaffold(
        content = { paddingValues ->
            Surface {
                ModalNavigationDrawer(
                    drawerState = drawerState,
                    modifier = Modifier.padding(paddingValues),
                    drawerContent = {
                        AppDrawerContent(
                            drawerState = drawerState,
                            menuItems = DrawerParams.drawerButtons,
                            defaultPick = Destinations.Home
                        ) { onUserPickedOption ->
                            // when user picks the path - navigates to new one
                            navController.navigate(onUserPickedOption.name)
                        }
                    }
                ) {
                    NavHost(navController = navController, startDestination = startDestination) {
                        composable(Destinations.Splash.name) {
                            SplashScreen(
                                onNavigateToDashboard = { navController.navigate(Destinations.Home.name) },
                                viewModel = hiltViewModel()
                            )
                        }
                        composable(Destinations.Home.name) {
                            HomeScreen(
                                drawerState = drawerState,
                                onNavigateToDashboard = { navController.navigate(Destinations.Dashboard.name) },
                                onNavigateToLearnCountries = { navController.navigate(Destinations.LearnCountries.name) },
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
                                },
                                drawerState = drawerState
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
                                },
                                drawerState = drawerState
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
                                drawerState = drawerState,
                                onDashboardTypePressed = { type ->
                                    navController.navigate("${Destinations.Game.name}/${type.name}")
                                }
                            )
                        }
                        composable("${Destinations.RegionType.name}/{dashboardType}") { backStackEntry ->
                            val dashboardType = backStackEntry.arguments?.getString("dashboardType")
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
            }
        }
    )
}