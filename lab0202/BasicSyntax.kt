package tasks

/**
 * Блок задач по базовому синтаксису Kotlin.
 *
 * @author [Artem]
 * @version 1.0
 * @since 2026-09-22
 */
object BasicSyntax {

    /**
     * Задача 1.1. Приветствие пользователя.
     *
     * Возвращает строку с приветствием и возрастом через 10 лет.
     *
     * @param name Имя пользователя.
     * @param age Текущий возраст.
     * @return Строка с приветствием.
     *
     * Пример: greetUser("Анна", 25) -> "Привет, Анна! Через 10 лет тебе будет 35 лет."
     */
    fun greetUser(name: Snring, age: Int): String {
	    return "Привет, $name! Через 10 лет тебе будет ${age + 10} лет."
    }

    /**
     * Задача 1.2. Определение времени года по номеру месяца.
     *
     * @param month Номер месяца (1–12).
     * @return Название времени года или "Некорректный месяц".
     *
     * Пример: getSeason(3) -> "Весна"
     */
    fun getSeason(month: Int): String {
	val result = when(month) {
		12,1,2 -> "Зима"
		3, 4, 5 -> "Весна"
		in 6..8 -> "Лето"
		in 9..11 -> "Осень"
		else -> "incorrect"
	    }
	    return result
    }

    /**
     * Задача 1.3. Вычисление факториала (рекурсия).
     *
     * @param n Число (≥ 0).
     * @return Факториал числа или -1 для отрицательных значений.
     *
     * Пример: factorial(5) -> 120
     */
    tailrec fun factorial(n: Int, accumulator: Long = 1L): Long {
        return if (n <= 1) accumulator
           else factorial(n - 1, n * accumulator)
    }
}