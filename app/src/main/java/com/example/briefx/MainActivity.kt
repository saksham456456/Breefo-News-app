package com.example.briefx

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.briefx.data.model.Article
import com.example.briefx.ui.NewsViewModel
import com.example.briefx.ui.screens.DetailScreen
import com.example.briefx.ui.screens.HomeScreen
import com.example.briefx.ui.theme.BriefXTheme
import android.util.Base64
import com.google.gson.Gson

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BriefXTheme {
                val navController = rememberNavController()
                val newsViewModel: NewsViewModel = viewModel()

                NavHost(navController = navController, startDestination = "home") {
                    composable("home") {
                        HomeScreen(
                            viewModel = newsViewModel,
                            onArticleClick = { article ->
                                val articleJson = Gson().toJson(article)
                                val base64Encoded = Base64.encodeToString(articleJson.toByteArray(Charsets.UTF_8), Base64.URL_SAFE or Base64.NO_WRAP)
                                navController.navigate("detail/$base64Encoded")
                            }
                        )
                    }
                    composable("detail/{articleJson}") { backStackEntry ->
                        val articleJson = backStackEntry.arguments?.getString("articleJson")
                        val article = articleJson?.let {
                            val decodedString = String(Base64.decode(it, Base64.URL_SAFE or Base64.NO_WRAP), Charsets.UTF_8)
                            Gson().fromJson(decodedString, Article::class.java)
                        }
                        article?.let {
                            DetailScreen(
                                article = it,
                                onBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}