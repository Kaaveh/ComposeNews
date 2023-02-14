package ir.kaaveh.remotedatasource.test

import ir.kaaveh.remotedatasource.dto.NewsDto
import ir.kaaveh.remotedatasource.dto.SourceDto

val newsDto = NewsDto(
    author = "Kaaveh Mohamedi",
    description = "This is a sample description.",
    publishedAt = "2022/02.10",
    source = SourceDto(id = null, name = "My Aunt"),
    title = "Title",
    url = "",
    urlToImage = "",
)