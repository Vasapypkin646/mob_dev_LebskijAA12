package tasks

object DopTask {

    /**
     * Обрабатывает пользовательский ввод.
     *
     * Если строка содержит числа через запятую (например, "1,2,3,4,5"),
     * возвращает Map с ключами:
     *  - "sum" — сумма чисел
     *  - "average" — среднее значение
     *  - "count" — количество чисел
     *  - "sorted" — отсортированный список
     *
     * Если строка содержит текст, возвращает Map с ключами:
     *  - "wordCount" — количество слов
     *  - "longestWord" — самое длинное слово
     *  - "charCount" — общее количество символов без пробелов
     *
     * @param input Входная строка (может быть null).
     * @return Map с результатами обработки. Для null/пустой строки — пустая Map.
     *
     * Пример: processUserInput("1,2,3") ->
     *         {"sum": 6, "average": 2.0, "count": 3, "sorted": [1, 2, 3]}
     */
    fun processUserInput(input: String?): Map<String, Any> {
        
        if (input == null) return emptyMap()

    
        val parts = input.split(",").map { it.trim() }
        val numbers = parts.mapNotNull { it.toIntOrNull() }

    
        if (parts.isNotEmpty() && numbers.size == parts.size) {
            return mapOf(
                "sum" to numbers.sum(),
                "average" to numbers.average(),  
                "count" to numbers.size,
                "sorted" to numbers.sorted()  
            )
        }

        
        val words = input.trim().split(Regex("\\s+")).filter { it.isNotEmpty() }

        return mapOf(
            "wordCount" to words.size,
            "longestWord" to (words.maxByOrNull { it.length } ?: ""),
            "charCount" to input.replace(Regex("\\s"), "").length
        )
    }
}