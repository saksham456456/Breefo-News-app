package com.example.briefx

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.briefx.data.model.Article
import com.example.briefx.ui.NewsViewModel
import com.example.briefx.ui.screens.DetailScreen
import com.example.briefx.ui.screens.HomeScreen
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.briefx.ui.screens.SavedScreen
import com.example.briefx.ui.theme.BriefXTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.hilt.navigation.compose.hiltViewModel
import android.util.Base64
import com.google.gson.Gson

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            BriefXTheme {
                val navController = rememberNavController()
                val newsViewModel: NewsViewModel = hiltViewModel()

                Scaffold(
                    bottomBar = {
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val currentRoute = navBackStackEntry?.destination?.route
                        if (currentRoute == "home" || currentRoute == "saved") {
                            NavigationBar {
                                NavigationBarItem(
                                    selected = currentRoute == "home",
                                    onClick = { navController.navigate("home") },
                                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                                    label = { Text("Home") }
                                )
                                NavigationBarItem(
                                    selected = currentRoute == "saved",
                                    onClick = { navController.navigate("saved") },
                                    icon = { Icon(Icons.Default.Bookmark, contentDescription = "Saved") },
                                    label = { Text("Saved") }
                                )
                            }
                        }
                    }
                ) { padding ->
                    NavHost(navController = navController, startDestination = "home", modifier = Modifier.padding(padding)) {
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
                        composable("saved") {
                            SavedScreen(
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
}
