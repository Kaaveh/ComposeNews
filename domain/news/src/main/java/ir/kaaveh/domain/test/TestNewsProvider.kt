package ir.kaaveh.domain.test

import ir.kaaveh.domain.model.News

val notFavoriteNews = News(
    author = "Kaaveh Mohamedi",
    description = "This is a sample description.",
    publishedAt = "2022/02.10",
    source = "My Aunt",
    title = "Title",
    url = "",
    urlToImage = "",
    isFavorite = false,
)

val favoriteNews = News(
    author = "Kaaveh Mohamedi",
    description = "This is a sample description.",
    publishedAt = "2022/02.10",
    source = "My Aunt",
    title = "Title",
    url = "",
    urlToImage = "",
    isFavorite = true,
)