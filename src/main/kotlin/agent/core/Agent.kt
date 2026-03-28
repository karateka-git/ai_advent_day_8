package agent.core

import agent.format.ResponseFormat

interface Agent<T> {
    val info: AgentInfo
    val responseFormat: ResponseFormat<T>

    fun previewTokenStats(userPrompt: String): AgentTokenStats

    fun ask(userPrompt: String): AgentResponse<T>

    fun clearContext()
}
