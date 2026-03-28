package llm.tokenizer

import ai.djl.huggingface.tokenizers.HuggingFaceTokenizer

class DeepSeekLocalTokenCounter : TokenCounter {
    private val tokenizer: HuggingFaceTokenizer by lazy(::createTokenizer)

    override fun countText(text: String): Int =
        tokenizer.encode(text).ids.size

    private fun createTokenizer(): HuggingFaceTokenizer {
        val localTokenizer = HuggingFaceTokenizer.newInstance(DeepSeekTokenizerAssets.directory)
        Runtime.getRuntime().addShutdownHook(
            Thread {
                localTokenizer.close()
            }
        )
        return localTokenizer
    }
}
