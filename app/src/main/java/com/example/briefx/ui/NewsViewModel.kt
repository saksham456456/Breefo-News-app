package com.example.briefx.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.briefx.data.model.Article
import com.example.briefx.data.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class NewsViewModel @Inject constructor(
    private val repository: NewsRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isLoadingMore = MutableStateFlow(false)
    val isLoadingMore: StateFlow<Boolean> = _isLoadingMore

    private var currentPage = 1
    private var searchPage = 1
    private var isLastPage = false
    private var isSearchLastPage = false

    private val _selectedCategory = MutableStateFlow("general")
    val selectedCategory: StateFlow<String> = _selectedCategory

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val searchQuery = MutableStateFlow("")
    private val _searchResults = MutableStateFlow<List<Article>>(emptyList())

    val articles: StateFlow<List<Article>> = combine(
        searchQuery,
        _searchResults,
        _selectedCategory.flatMapLatest { category ->
            repository.getCachedArticles(category)
        }
    ) { query, searchResults, cachedArticles ->
        if (query.isNotEmpty()) searchResults else cachedArticles
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val savedArticles: StateFlow<List<Article>> = repository.getSavedArticles()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        fetchNews()

        viewModelScope.launch {
            searchQuery
                .debounce(500)
                .filter { it.length > 2 || it.isEmpty() }
                .distinctUntilChanged()
                .collectLatest { query ->
                    if (query.isEmpty()) {
                        _searchResults.value = emptyList()
                    } else {
                        performSearch(query)
                    }
                }
        }
    }

    fun fetchNews(category: String = _selectedCategory.value) {
        if (category != _selectedCategory.value) {
            currentPage = 1
            isLastPage = false
        } else {
            currentPage = 1
            isLastPage = false
        }
        _selectedCategory.value = category
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                repository.fetchNewsFromApiAndCache(category, page = currentPage)
            } catch (e: Exception) {
                _errorMessage.value = "Network error: ${e.localizedMessage ?: "Unknown error"}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadMoreNews() {
        val query = searchQuery.value
        if (query.isNotEmpty()) {
            if (_isLoadingMore.value || isSearchLastPage) return
            viewModelScope.launch {
                _isLoadingMore.value = true
                try {
                    searchPage++
                    val moreResults = repository.searchNews(query, page = searchPage)
                    if (moreResults.isEmpty()) {
                        isSearchLastPage = true
                    } else {
                        _searchResults.value = _searchResults.value + moreResults
                    }
                } catch (e: Exception) {
                    _errorMessage.value = "Network error: ${e.localizedMessage ?: "Unknown error"}"
                } finally {
                    _isLoadingMore.value = false
                }
            }
        } else {
            if (_isLoadingMore.value || isLastPage) return
            viewModelScope.launch {
                _isLoadingMore.value = true
                try {
                    currentPage++
                    repository.fetchNewsFromApiAndCache(_selectedCategory.value, page = currentPage)
                } catch (e: Exception) {
                    _errorMessage.value = "Network error: ${e.localizedMessage ?: "Unknown error"}"
                    currentPage--
                } finally {
                    _isLoadingMore.value = false
                }
            }
        }
    }

    fun searchNews(query: String) {
        searchQuery.value = query
    }

    private suspend fun performSearch(query: String) {
        _isLoading.value = true
        _errorMessage.value = null
        searchPage = 1
        isSearchLastPage = false
        try {
            _searchResults.value = repository.searchNews(query, page = searchPage)
        } catch (e: Exception) {
            _errorMessage.value = "Network error: ${e.localizedMessage ?: "Unknown error"}"
            _searchResults.value = emptyList()
        } finally {
            _isLoading.value = false
        }
    }

    fun toggleBookmark(article: Article) {
        viewModelScope.launch {
            repository.toggleBookmark(article)
        }
    }
}
