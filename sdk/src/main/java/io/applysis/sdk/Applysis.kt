package io.applysis.sdk

import io.applysis.sdk.utils.await
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import java.io.IOException

/**
 * @param apiKey key for authentication for the API.
 * @param debugMode prints requests/responses
 */
class Applysis(
    private val apiKey: String,
    private val debugMode: Boolean = false,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) {

    private val client by lazy {
        OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().setLevel(
                    if (debugMode) {
                        HttpLoggingInterceptor.Level.BODY
                    } else {
                        HttpLoggingInterceptor.Level.NONE
                    }
                )
            )
            .build()
    }

    suspend fun submitFeedback(feedback: Feedback): FeedbackResult {
        if (feedback.text.isEmpty()) {
            return FeedbackResult.Error(ApplysisError.BadRequest)
        }
        return submitFeedbacks(listOf(feedback))
    }

    suspend fun submitFeedbacks(feedbacks: List<Feedback>): FeedbackResult {
        if (feedbacks.size > FEEDBACK_BATCH_LIMIT) {
            return FeedbackResult.Error(ApplysisError.NonValid)
        }

        val body = feedbacks.toJson()
            .toRequestBody(MEDIA_TYPE_JSON)
        val request = Request.Builder()
            .url(BASE_URL)
            .addHeader("x-api-key", apiKey)
            .post(body)
            .build()

        return client.newCall(request).awaitFeedbackResult()
    }

    private suspend fun Call.awaitFeedbackResult(): FeedbackResult {
        val result = kotlin.runCatching {
            val response = await(ioDispatcher)

            if (response.code == 200) {
                FeedbackResult.Success
            } else {
                val error = when (response.code) {
                    403 -> ApplysisError.Forbidden
                    400 -> ApplysisError.BadRequest
                    else -> ApplysisError.Unknown(IOException(response.message))
                }
                FeedbackResult.Error(error)
            }

        }
        return result.getOrNull()
            ?: FeedbackResult.Error(ApplysisError.Unknown(result.exceptionOrNull()))
    }

    companion object {
        private val MEDIA_TYPE_JSON = "application/json; charset=utf-8".toMediaType()
        private const val BASE_URL = "https://api-public.applysis.io"
        private const val FEEDBACK_BATCH_LIMIT = 50
    }
}
