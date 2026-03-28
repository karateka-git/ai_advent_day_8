package llm.core

import llm.model.ChatMessage
import llm.tokenizer.TokenCounter

interface LanguageModel {
    val info: LanguageModelInfo
    val tokenCounter: TokenCounter?

    fun complete(messages: List<ChatMessage>): LanguageModelResponse
}
