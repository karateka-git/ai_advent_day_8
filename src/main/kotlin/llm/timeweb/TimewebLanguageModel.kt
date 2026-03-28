package llm.timeweb

import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.charset.StandardCharsets
import kotlinx.serialization.json.Json
import llm.core.LanguageModel
import llm.core.LanguageModelInfo
import llm.model.ChatMessage
import llm.timeweb.model.ChatCompletionRequest
import llm.timeweb.model.ChatCompletionResponse

private const val MODEL = "DeepSeek V3.2"
private const val MAX_TOKENS = 400
private const val TEMPERATURE = 0.7
private const val API_URL_TEMPLATE =
    "https://agent.timeweb.cloud/api/v1/cloud-ai/agents/%s/v1/chat/completions"

class TimewebLanguageModel(
    private val httpClient: HttpClient,
    private val agentId: String,
    private val userToken: String
) : LanguageModel {
    private val json = Json { ignoreUnknownKeys = true }

    override val info = LanguageModelInfo(
        name = "TimewebLanguageModel",
        model = MODEL
    )

    override fun complete(messages: List<ChatMessage>): String {
        val requestBody = json.encodeToString(
            ChatCompletionRequest(
                model = MODEL,
                messages = messages,
                temperature = TEMPERATURE,
                maxTokens = MAX_TOKENS
            )
        )

        val request = HttpRequest.newBuilder()
            .uri(URI.create(API_URL_TEMPLATE.format(agentId)))
            .header("Content-Type", "application/json; charset=UTF-8")
            .header("Authorization", "Bearer $userToken")
            .POST(HttpRequest.BodyPublishers.ofString(requestBody, StandardCharsets.UTF_8))
            .build()

        val response = httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8))

        if (response.statusCode() !in 200..299) {
            error("API вернул статус ${response.statusCode()}: ${response.body()}")
        }

        val completion = json.decodeFromString<ChatCompletionResponse>(response.body())
        return completion.choices.firstOrNull()?.message?.content
            ?: error("Ответ API не содержит choices[0].message.content")
    }
}
