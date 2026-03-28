package llm.timeweb.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import llm.model.ChatMessage

@Serializable
data class ChatCompletionRequest(
    val model: String,
    val messages: List<ChatMessage>,
    val temperature: Double,
    @SerialName("max_tokens")
    val maxTokens: Int
)
