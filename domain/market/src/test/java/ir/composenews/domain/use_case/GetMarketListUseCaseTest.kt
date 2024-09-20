

package ir.composenews.domain.use_case

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.ints.shouldBeExactly
import ir.composenews.domain.repository.MarketRepository
import ir.composenews.domain.test.favoriteMarket
import ir.composenews.domain.test.notFavoriteMarket
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

class GetMarketListUseCaseTest : StringSpec({

    lateinit var mockRepository: MarketRepository
    lateinit var getMarketListUseCase: GetMarketListUseCase

    beforeSpec {
        mockRepository = mock {
            on { getMarketList() } doReturn flow {
                emit(listOf(favoriteMarket, notFavoriteMarket))
            }
        }
        getMarketListUseCase = GetMarketListUseCase(repository = mockRepository)
    }

    "Get market list" {
        runTest {
            val markets = getMarketListUseCase().first()
            markets.size shouldBeExactly 2
        }
    }
})
