package com.example.counter.data

/**
 * Неизменяемое состояние экрана счётчика.
 *
 * @property count Текущее числовое значение счётчика.
 * @property step Величина шага изменения счётчика при нажатии на кнопки (1, 5, 10, 25).
 * @property maxValue Максимально допустимое значение счётчика (по умолчанию 100).
 * @property minValue Минимально допустимое значение счётчика (по умолчанию 0).
 * @property history Список предыдущих зафиксированных значений счётчика.
 * @property isLoading Флаг фоновой асинхронной загрузки данных.
 *
 * @author Лебский Артём Александрович
 * @since 2026-11-02
 */
data class CounterState(
    val count: Int = 0,
    val step: Int = 1,
    val maxValue: Int = 100,
    val minValue: Int = 0,
    val history: List<Int> = emptyList(),
    val isLoading: Boolean = false
)
