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
import com.google.gson.Gson
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

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
                                val articleJson = URLEncoder.encode(Gson().toJson(article), StandardCharsets.UTF_8.toString())
                                navController.navigate("detail/$articleJson")
                            }
                        )
                    }
                    composable("detail/{articleJson}") { backStackEntry ->
                        val articleJson = backStackEntry.arguments?.getString("articleJson")
                        val article = articleJson?.let {
                            Gson().fromJson(URLDecoder.decode(it, StandardCharsets.UTF_8.toString()), Article::class.java)
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