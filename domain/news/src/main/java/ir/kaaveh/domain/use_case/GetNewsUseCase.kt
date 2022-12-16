package ir.kaaveh.domain.use_case

import ir.kaaveh.domain.model.News
import ir.kaaveh.domain.model.Resource
import ir.kaaveh.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetNewsUseCase @Inject constructor(
    private val repository: NewsRepository,
) {

    operator fun invoke(): Flow<Resource<List<News>>> = flow {
        try {
            emit(Resource.Loading())
            val articles =
                repository.getNews().map { it.copy(isFavorite = repository.isFavoriteNews(it)) }
            emit(Resource.Success(articles))
        } catch (e: Exception) {
            emit(Resource.Error(e))
        }
    }

}