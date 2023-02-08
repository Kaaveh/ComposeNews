package ir.kaaveh.domain.use_case

import ir.kaaveh.domain.model.News
import ir.kaaveh.domain.repository.NewsRepository
import javax.inject.Inject

class ToggleFavoriteNewsUseCase @Inject constructor(
    private val repository: NewsRepository,
) {

    suspend operator fun invoke(news: News) =
        repository.toggleFavoriteNews(news)

}