package ir.kaaveh.localdatasource.test

import ir.kaaveh.localdatasource.dto.LocalNewsDto

val localNewsDto = LocalNewsDto(
    author = "Kaaveh Mohamedi",
    description = "This is a sample description.",
    publishedAt = "2022/02.10",
    source = "My Aunt",
    title = "Title",
    url = "",
    urlToImage = "",
    isFavorite = false,
)

val favoriteLocalNewsDto = LocalNewsDto(
    author = "Kaaveh Mohamedi",
    description = "This is a sample description.",
    publishedAt = "2022/02.10",
    source = "My Aunt",
    title = "Title",
    url = "",
    urlToImage = "",
    isFavorite = true,
)