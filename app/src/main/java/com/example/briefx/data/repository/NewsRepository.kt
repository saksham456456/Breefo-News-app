package com.example.briefx.data.repository

import com.example.briefx.data.local.ArticleDao
import com.example.briefx.data.local.toArticle
import com.example.briefx.data.local.toEntity
import com.example.briefx.data.model.Article
import com.example.briefx.data.remote.NewsApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val apiService: NewsApiService,
    private val articleDao: ArticleDao
) {
    fun getSavedArticles(): Flow<List<Article>> = articleDao.getSavedArticles().map { entities ->
        entities.map { it.toArticle() }
    }

    fun getCachedArticles(category: String): Flow<List<Article>> = articleDao.getArticlesByCategory(category).map { entities ->
        entities.map { it.toArticle() }
    }

    suspend fun fetchNewsFromApiAndCache(category: String, page: Int = 1) {
        val response = apiService.getTopHeadlines(category = if (category == "general") null else category, page = page)
        if (response.isSuccessful) {
            val articles = response.body()?.articles ?: emptyList()
            val entities = articles.map { it.toEntity(category = category) }
            if (page == 1) {
                articleDao.updateCachedArticles(category, entities)
            } else {
                articleDao.upsertArticles(entities)
            }
        } else {
            throw Exception(response.message())
        }
    }

    suspend fun searchNews(query: String, page: Int = 1): List<Article> {
        val response = apiService.searchNews(query = query, page = page)
        if (response.isSuccessful) {
            return response.body()?.articles ?: emptyList()
        } else {
            throw Exception(response.message())
        }
    }

    suspend fun toggleBookmark(article: Article) {
        val isSaved = articleDao.isArticleSaved(article.url)
        if (isSaved) {
            articleDao.updateArticleSavedStatus(article.url, false)
        } else {
            articleDao.upsertArticles(listOf(article.toEntity(isSaved = true)))
            articleDao.updateArticleSavedStatus(article.url, true)
        }
    }

    suspend fun isArticleSaved(url: String): Boolean {
        return articleDao.isArticleSaved(url)
    }
}
