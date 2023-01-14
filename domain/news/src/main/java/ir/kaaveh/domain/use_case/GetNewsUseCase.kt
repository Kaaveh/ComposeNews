package ir.kaaveh.domain.use_case

import ir.kaaveh.domain.model.News
import ir.kaaveh.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetNewsUseCase @Inject constructor(
    private val repository: NewsRepository,
) {

    operator fun invoke(): Flow<List<News>> =
        repository.getNews()
            .map { list ->
                list.map { news ->
                    news.copy(
                        isFavorite = repository.isFavoriteNews(news)
                    )
                }
            }

}