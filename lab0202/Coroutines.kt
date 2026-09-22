package tasks

import kotlinx.coroutines.*

/**
 * Блок задач по корутинам в Kotlin.
 *
 * @author [Artem]
 * @version 1.0
 * @since 2026-09-03
 */
object Coroutines {

    /**
     * Задача 4.1. Асинхронная задержка с выводом сообщений.
     *
     * @param message Сообщение для вывода.
     * @param delayMs Время задержки в миллисекундах.
     */
    suspend fun delayedPrint(message: String, delayMs: Long) {
        println("Начинаем...")
        println(message)
        delay(delayMs)
        println("Готово!")
    }

    /**
     * Задача 4.2. Параллельное выполнение трёх задач.
     *
     * Запускает три асинхронные задачи с задержками 1с, 2с, 3с.
     * Возвращает список результатов в порядке завершения.
     *
     * @return Список строк с результатами.
     */
    suspend fun runParallelTasks() = coroutineScope {
        val deferred1: Deferred<String> = async {
        delay(1000)
        "Результат 1"
        }
        val deferred2: Deferred<String> = async {
        delay(2000)
        "Результат 2"
        }
        val deferred3: Deferred<String> = async {
        delay(3000)
        "Результат 3"
        }
        val result1: String = deferred1.await()
        println(result1)
        val result2: String = deferred2.await()
        println(result2)
        val result3: String = deferred3.await()
        println(result3)
    }
}