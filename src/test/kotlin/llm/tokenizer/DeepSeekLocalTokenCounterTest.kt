package llm.tokenizer

import kotlin.test.Test
import kotlin.test.assertTrue

class DeepSeekLocalTokenCounterTest {
    @Test
    fun `countText returns positive token count for non empty text`() {
        val counter = DeepSeekLocalTokenCounter()

        val tokenCount = counter.countText("Привет! Это проверка локального токенизатора.")

        assertTrue(tokenCount > 0)
    }
}
