package llm.tokenizer

interface TokenCounter {
    fun countText(text: String): Int
}
