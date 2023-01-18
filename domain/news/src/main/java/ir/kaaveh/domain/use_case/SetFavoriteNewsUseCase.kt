package ir.kaaveh.domain.use_case

import ir.kaaveh.domain.model.News
import ir.kaaveh.domain.repository.NewsRepository
import javax.inject.Inject

class SetFavoriteNewsUseCase @Inject constructor(
    private val repository: NewsRepository,
) {

    suspend operator fun invoke(news: News) =
        repository.setFavoriteNews(news.copy(isFavorite = !news.isFavorite))

}