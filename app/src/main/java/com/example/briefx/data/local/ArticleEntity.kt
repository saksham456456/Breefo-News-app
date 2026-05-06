package com.example.briefx.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.briefx.data.model.Article
import com.example.briefx.data.model.Source

@Entity(tableName = "articles")
data class ArticleEntity(
    @PrimaryKey val url: String,
    val sourceName: String,
    val author: String?,
    val title: String,
    val description: String?,
    val urlToImage: String?,
    val publishedAt: String,
    val content: String?,
    val isSaved: Boolean = false,
    val category: String = "general"
)

fun ArticleEntity.toArticle(): Article {
    return Article(
        source = Source(id = null, name = sourceName),
        author = author,
        title = title,
        description = description,
        url = url,
        urlToImage = urlToImage,
        publishedAt = publishedAt,
        content = content
    )
}

fun Article.toEntity(isSaved: Boolean = false, category: String = "general"): ArticleEntity {
    return ArticleEntity(
        url = url,
        sourceName = source.name,
        author = author,
        title = title,
        description = description,
        urlToImage = urlToImage,
        publishedAt = publishedAt,
        content = content,
        isSaved = isSaved,
        category = category
    )
}
