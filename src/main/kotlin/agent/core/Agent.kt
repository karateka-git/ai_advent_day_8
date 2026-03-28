package agent.core

import agent.format.ResponseFormat
import llm.core.TokenUsage

interface Agent<T> {
    val info: AgentInfo
    val responseFormat: ResponseFormat<T>
    val lastUserPromptTokens: Int?
    val lastTokenUsage: TokenUsage?

    fun ask(userPrompt: String): T

    fun clearContext()
}
