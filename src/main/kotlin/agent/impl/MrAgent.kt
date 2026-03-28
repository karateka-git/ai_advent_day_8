package agent.impl

import agent.core.Agent
import agent.core.AgentInfo
import agent.format.ResponseFormat
import agent.format.TextResponseFormat
import agent.storage.JsonConversationStore
import agent.storage.mapper.ChatMessageConversationMapper
import llm.core.LanguageModel
import llm.model.ChatMessage
import llm.model.ChatRole

private const val DEFAULT_CONTEXT_FILE = "config/conversation.json"

class MrAgent(
    private val languageModel: LanguageModel,
    private val systemPrompt: String = DEFAULT_SYSTEM_PROMPT
) : Agent<String> {
    private val conversationMapper = ChatMessageConversationMapper()
    private val conversationStore = JsonConversationStore(java.nio.file.Path.of(DEFAULT_CONTEXT_FILE))

    override val responseFormat: ResponseFormat<String> = TextResponseFormat

    private val conversation = loadConversation().toMutableList()

    override val info = AgentInfo(
        name = "MrAgent",
        description = "CLI-агент для диалога с LLM через HTTP API.",
        model = languageModel.info.model
    )

    override fun ask(userPrompt: String): String {
        conversation += ChatMessage(role = ChatRole.USER, content = userPrompt)
        saveConversation()

        val rawContent = languageModel.complete(conversation)

        conversation += ChatMessage(role = ChatRole.ASSISTANT, content = rawContent)
        saveConversation()

        return responseFormat.parse(rawContent)
    }

    override fun clearContext() {
        conversation.clear()
        conversation += createSystemMessage()
        saveConversation()
    }

    private fun loadConversation(): List<ChatMessage> {
        val savedConversation = conversationStore.load()
            .map(conversationMapper::fromStoredMessage)
        if (savedConversation.isNotEmpty()) {
            return savedConversation
        }

        val initialConversation = listOf(createSystemMessage())
        conversationStore.save(initialConversation.map(conversationMapper::toStoredMessage))
        return initialConversation
    }

    private fun saveConversation() {
        conversationStore.save(conversation.map(conversationMapper::toStoredMessage))
    }

    private fun createSystemMessage(): ChatMessage =
        ChatMessage(
            role = ChatRole.SYSTEM,
            content = buildSystemPrompt()
        )

    private fun buildSystemPrompt(): String =
        "$systemPrompt\n\nТребования к формату ответа:\n${responseFormat.formatInstruction}"

    companion object {
        private const val DEFAULT_SYSTEM_PROMPT =
            "Ты полезный ассистент. Отвечай кратко, если пользователь не просит подробнее."
    }
}
