package ir.kaaveh.domain.use_case

import ir.kaaveh.domain.model.News
import ir.kaaveh.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNewsUseCase @Inject constructor(
    private val repository: NewsRepository,
) {

    suspend operator fun invoke(): Flow<List<News>> {
        repository.syncNews()
        return repository.getNews()
    }

}