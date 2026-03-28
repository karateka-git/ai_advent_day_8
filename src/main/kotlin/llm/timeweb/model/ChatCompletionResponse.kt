package llm.timeweb.model

import kotlinx.serialization.Serializable
import llm.model.ChatMessage

@Serializable
data class ChatCompletionResponse(
    val choices: List<Choice> = emptyList()
)

@Serializable
data class Choice(
    val message: ChatMessage
)
