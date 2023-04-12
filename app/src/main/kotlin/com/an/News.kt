package com.an

import android.content.Context
import android.content.res.Resources
import android.util.Log
import android.view.View
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.an.mytopnews.R
import com.google.android.material.snackbar.Snackbar
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.net.URI
import java.net.URISyntaxException
import java.nio.channels.UnresolvedAddressException


data class News (
    var title: String = "",
    var url: String = "",
    var source: String = "",
    var date: String = "",
    var imageUrl : String = "",
    )


class NewsListViewModel: ViewModel() {


    @Serializable
    data class NewsDto(
        val uniquekey: String,
        val title: String,
        val date: String,
        val category: String,
        val author_name: String,
        val url: String,
        val thumbnail_pic_s: String,
        val is_content: String
        )

    @Serializable
    data class Result(val stat: String,
                      val data: List<NewsDto>,
                      val page: String,
                      val pageSize: String)

    @Serializable
    data class NewsResponse(
        val reason: String,
        val result: Result,
        val error_code: Int
        )


    private var client: HttpClient = HttpClient(CIO) {
        expectSuccess = true
//        engine {
//            maxConnectionsCount = 1000
//            requestTimeout = timeOutSeconds
//            endpoint {
//                maxConnectionsPerRoute = 100
//                pipelineMaxSize = 20
//                keepAliveTime = timeOutSeconds
//                connectTimeout = timeOutSeconds
//            }
//        }
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.HEADERS
        }

        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
    }

    private val _newsList = MutableLiveData<List<News>>()
    var newsList: LiveData<List<News>>
        get() = _newsList
        set(value) { // no-op
        }

    @Throws(URISyntaxException::class)
    private fun getDomainName(url: String?): String {
        val uri = URI(url)
        val domain: String = uri.getHost()
        return if (domain.startsWith("www.")) domain.substring(4) else domain
    }

    private fun handleError(se: Throwable, errorHandler: (String) -> Unit) {
        if (se.localizedMessage != null) {
            Log.i("fetchNews", se.localizedMessage!!);
            errorHandler(se.localizedMessage!!);
        } else if (se.message != null) {
            Log.i("fetchNews", se.message!!);
            errorHandler(se.message!!);
        }
    }

    fun fetchNews(category: String, errorHandler: (Throwable) -> Unit) {
        viewModelScope.launch {
            // _books.value = someLongOperationInRepo()

            try {
                val newsResponse: NewsResponse = client
                    .get("http://v.juhe.cn/toutiao/index") {
                        url {
                            parameters.append("key", "f882220b9588c38ed006745b146f4aee")
                            parameters.append("type", category)
                        }
                    }
                    .body()

                val tempNewList = mutableListOf<News>();

                for (newsDto in newsResponse.result.data) {
                    val news = News(
                        title = newsDto.title,
                        url = newsDto.url,
                        source = getDomainName(newsDto.url),
                        date = newsDto.date,
                        imageUrl = newsDto.thumbnail_pic_s)
                    tempNewList.add(news)
                }

                _newsList.postValue(tempNewList)

            } catch (error: Throwable) {
                errorHandler(error);
            }
        }
    }
}
