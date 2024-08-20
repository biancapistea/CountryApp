package com.example.countryapp.ui.home

import com.example.countryapp.R
import com.example.countryapp.ui.models.HomeSectionItem

object HomeScreenData {
    fun getImages(
        onNavigateToDashboard: () -> Unit,
        onNavigateToLearnCountries: () -> Unit,
        onNavigateToPlayScreen: () -> Unit
    ): List<HomeSectionItem> {
        return listOf(
            HomeSectionItem(
                headerImage = R.drawable.img_discover_countries,
                description = "Would you like to test your knowledge about all the countries in the world? You can select the quiz type and answer the quiz questions to test your knowledge about the world's countries. You can also select from which region you want to receive questions.",
                actionText = "Go to quiz game",
                title = "Play & Discover new countries",
                onClickOnActionText = onNavigateToDashboard
            ),
            HomeSectionItem(
                headerImage = R.drawable.img_play_hangman_guess,
                description = "Do you feel like you know every capital or flag of all the countries in the world? Then, you can challenge yourself by playing the hangman game. You will need to guess the capital of the country or the country based on the flag. What do you say?",
                actionText = "Go to hangman game",
                title = "Play & Learn",
                onClickOnActionText = onNavigateToPlayScreen
            ),
            HomeSectionItem(
                headerImage = R.drawable.img_header_dashboard,
                description = "Do you feel like you need to learn/ find out more details about the countries in the world? You can try the Training Mode and then challenge yourself by taking the quiz or playing hangman!",
                actionText = "Go to training",
                title = "Learn & Train",
                onClickOnActionText = onNavigateToLearnCountries
            ),
            HomeSectionItem(
                headerImage = R.drawable.img_berlin,
                description = "You don't know where to travel next? Berlin, capital of Germany can be the best choice. Berlin has been the stage for a lot of world history and not just the fall of the Berlin Wall. You can still discover the traces of history in countless places around the capital. You can also explore traces of history in the modernist housing estates, which are UNESCO World Heritage Sites.",
                actionText = "Learn about Germany",
                title = "Visit Berlin",
                onClickOnActionText = onNavigateToLearnCountries // Replace with specific Berlin navigation if needed
            ),
            HomeSectionItem(
                headerImage = R.drawable.img_rome,
                description = "You don't know where to travel next? Rome, capital of Italy can be the best choice. Visiting Rome is a life-changing experience thanks to its history, monuments and beauty. The centre boasts legendary sites such as the Colosseum, the Fori Imperiali, the Pantheon or the Fountain of Trevi, but also charming neighbourhoods where you can breathe all the authentic Romanness, full of local culture and good food. Without forgetting about the Vatican and Piazza San Pietro, among the most visited religious destinations in the world.",
                actionText = "Learn about Italy",
                title = "Visit Rome",
                onClickOnActionText = onNavigateToLearnCountries // Replace with specific Rome navigation if needed
            ),
            HomeSectionItem(
                headerImage = R.drawable.img_bangkok,
                description = "Bangkok is the most visited city in the world. It's the jumping-off point for many trips to Thailand and the rest of Southeast Asia, with an important international airport, but there are so many more reasons to visit. Lively traditional markets and exquisite golden temples - where you'll be immersed in the daily life of locals as it has existed for centuries - are found side by side with soaring glass skyscrapers, fashionable rooftop bars, and immense modern malls.",
                actionText = "Learn about Thailand",
                title = "Visit Bangkok",
                onClickOnActionText = onNavigateToLearnCountries // Replace with specific Bangkok navigation if needed
            ),
            HomeSectionItem(
                headerImage = R.drawable.img_dubai,
                description = "Beautiful beaches, record-breaking attractions and experiences like no other – Dubai is the place to be in 2023. It's no wonder it has been named Tripadvisor's #1 Most Popular Destination in the World for the second year running. Let's explore!",
                actionText = "Learn about United Arab Emirates",
                title = "Visit Dubai",
                onClickOnActionText = onNavigateToLearnCountries // Replace with specific Dubai navigation if needed
            ),
        )
    }
}
