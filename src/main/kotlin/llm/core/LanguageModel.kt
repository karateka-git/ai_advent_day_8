package llm.core

import llm.model.ChatMessage

interface LanguageModel {
    val info: LanguageModelInfo

    fun complete(messages: List<ChatMessage>): String
}
