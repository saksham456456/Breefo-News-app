package com.example.briefx.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {
    @Query("SELECT * FROM articles WHERE isSaved = 1")
    fun getSavedArticles(): Flow<List<ArticleEntity>>

    @Query("SELECT * FROM articles WHERE category = :category ORDER BY publishedAt DESC")
    fun getArticlesByCategory(category: String): Flow<List<ArticleEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertArticlesIgnore(articles: List<ArticleEntity>): List<Long>

    @Query("UPDATE articles SET sourceName = :sourceName, author = :author, title = :title, description = :description, urlToImage = :urlToImage, publishedAt = :publishedAt, content = :content, category = :category WHERE url = :url")
    suspend fun updateArticleFields(url: String, sourceName: String, author: String?, title: String, description: String?, urlToImage: String?, publishedAt: String, content: String?, category: String)

    @Transaction
    suspend fun upsertArticles(articles: List<ArticleEntity>) {
        val insertResults = insertArticlesIgnore(articles)
        for (i in insertResults.indices) {
            if (insertResults[i] == -1L) {
                // The item already exists. Update its fields but PRESERVE isSaved status.
                val article = articles[i]
                updateArticleFields(
                    url = article.url,
                    sourceName = article.sourceName,
                    author = article.author,
                    title = article.title,
                    description = article.description,
                    urlToImage = article.urlToImage,
                    publishedAt = article.publishedAt,
                    content = article.content,
                    category = article.category
                )
            }
        }
    }

    @Query("UPDATE articles SET isSaved = :isSaved WHERE url = :url")
    suspend fun updateArticleSavedStatus(url: String, isSaved: Boolean)

    @Query("SELECT EXISTS(SELECT 1 FROM articles WHERE url = :url AND isSaved = 1)")
    suspend fun isArticleSaved(url: String): Boolean

    @Query("DELETE FROM articles WHERE category = :category AND isSaved = 0")
    suspend fun clearCachedArticlesByCategory(category: String)

    @Transaction
    suspend fun updateCachedArticles(category: String, articles: List<ArticleEntity>) {
        clearCachedArticlesByCategory(category)
        upsertArticles(articles)
    }
}
