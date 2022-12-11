package ir.kaaveh.remotedatasource.api

import ir.kaaveh.remotedatasource.dto.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("v2/top-headlines")
    suspend fun getNews(
        @Query("country")
        countryCode: String = "us",
        @Query("page")
        pageNumber: Int = 1,
        @Query("apiKey")
        apiKey: String = API_KEY,
    ): NewsResponse

}

internal const val API_KEY = "12028565f22e40729731725bbe3fe2ab"
internal const val BASE_URL = "https://newsapi.org"