package com.example.briefx.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.briefx.data.model.Article
import com.example.briefx.data.remote.NewsApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NewsViewModel : ViewModel() {

    private val apiService: NewsApiService = Retrofit.Builder()
        .baseUrl("https://newsapi.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(NewsApiService::class.java)

    private val _articles = MutableStateFlow<List<Article>>(emptyList())
    val articles: StateFlow<List<Article>> = _articles

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _selectedCategory = MutableStateFlow("general")
    val selectedCategory: StateFlow<String> = _selectedCategory

    init {
        fetchNews()
    }

    fun fetchNews(category: String = _selectedCategory.value) {
        _selectedCategory.value = category
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = apiService.getTopHeadlines(category = if (category == "general") null else category)
                if (response.isSuccessful) {
                    _articles.value = response.body()?.articles ?: emptyList()
                }
            } catch (e: Exception) {
                // Handle error
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun searchNews(query: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = apiService.searchNews(query = query)
                if (response.isSuccessful) {
                    _articles.value = response.body()?.articles ?: emptyList()
                }
            } catch (e: Exception) {
                // Handle error
            } finally {
                _isLoading.value = false
            }
        }
    }
}