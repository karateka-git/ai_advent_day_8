package llm.core

data class LanguageModelResponse(
    val content: String,
    val usage: TokenUsage? = null
)
