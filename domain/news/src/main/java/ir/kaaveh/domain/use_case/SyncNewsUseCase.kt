package ir.kaaveh.domain.use_case

import ir.kaaveh.domain.repository.NewsRepository
import javax.inject.Inject

class SyncNewsUseCase @Inject constructor(
    private val repository: NewsRepository
) {

    suspend operator fun invoke(): Boolean = repository.syncNews()

}