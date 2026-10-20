import tasks.*
import kotlinx.coroutines.runBlocking

/**
 * Точка входа в приложение.
 * Содержит тесты для всех задач.
 *
 * @author [ВАШЕ ИМЯ]
 * @version 1.0
 * @since 2026-09-03
 */
fun main() {
    println("=== Тестирование лабораторной работы №2 ===\n")

    // Тестирование задач блока 1 (Базовый синтаксис)
    println("--- Блок 1. Базовый синтаксис ---")
    println(BasicSyntax.greetUser("Анна", 25)) // TODO: ожидается "Привет, Анна! Через 10 лет тебе будет 35 лет."
    println(BasicSyntax.getSeason(3))         // TODO: ожидается "Весна"
    println(BasicSyntax.factorial(5))         // TODO: ожидается 120
    println()

    // Тестирование задач блока 2 (Null-безопасность)
    println("--- Блок 2. Null-безопасность ---")
    println(NullSafety.parseIntSafe("123"))   // TODO: ожидается 123
    println(NullSafety.parseIntSafe("abc"))   // TODO: ожидается null
    println(NullSafety.filterNonNullAndDouble(listOf(1, null, 3, null, 5))) // TODO: ожидается [2, 6, 10]
    println()

    // Тестирование задач блока 3 (Коллекции)
    println("--- Блок 3. Коллекции ---")
    val people = listOf(Person("Анна", 25), Person("Иван", 30), Person("Мария", 35))
    println(Collections.averageAge(people))   // TODO: ожидается 30.0
    println(Collections.groupByFirstLetter(listOf("apple", "apricot", "banana"))) // TODO: ожидается {'A' -> [...], 'B' -> [...]}
    println(Collections.findLongestWord(listOf("apple", "banana", "pineapple"))) // TODO: ожидается "pineapple"
    println()

    // Тестирование задач блока 4 (Корутины)
    println("--- Блок 4. Корутины ---")
    runBlocking {
        Coroutines.delayedPrint("Начинаем...", 2000L)
        println(Coroutines.runParallelTasks()) // TODO: ожидается ["Результат 1", "Результат 2", "Результат 3"]
    }
    // Тестирование доп задачи
    println("--- Дополнительное задание ---")
    println(ExtraTask.processUserInput("1,2,3,4,5"))
    // {sum=15, average=3.0, count=5, sorted=[1, 2, 3, 4, 5]}
    println(ExtraTask.processUserInput("Hello Kotlin World"))
    // {wordCount=3, longestWord=Kotlin, charCount=16}
    println(ExtraTask.processUserInput(null))
}