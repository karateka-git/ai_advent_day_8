package agent.core

import agent.format.ResponseFormat

interface Agent<T> {
    val info: AgentInfo
    val responseFormat: ResponseFormat<T>

    fun ask(userPrompt: String): T

    fun clearContext()
}
