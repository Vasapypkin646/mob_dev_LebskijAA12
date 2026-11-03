package com.example.counter.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.counter.data.CounterState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel для управления состоянием и бизнес-логикой экрана счётчика.
 * Поддерживает сохранение состояния в [SavedStateHandle], реактивную публикацию
 * через [StateFlow] и асинхронные операции через корутины.
 *
 * @param savedStateHandle Механизм сохранения состояния для восстановления после уничтожения процесса.
 *
 * @author Лебский Артём Александрович
 * @since 2026-11-02
 */
class CounterViewModel(
    private val savedStateHandle: SavedStateHandle = SavedStateHandle()
) : ViewModel() {

    companion object {
        private const val KEY_COUNT = "counter_count"
        private const val KEY_STEP = "counter_step"
        private const val KEY_HISTORY = "counter_history"
    }

    // Восстановление сохранённых значений при пересоздании процесса
    private val restoredCount: Int = savedStateHandle[KEY_COUNT] ?: 0
    private val restoredStep: Int = savedStateHandle[KEY_STEP] ?: 1
    private val restoredHistory: List<Int> = savedStateHandle.get<ArrayList<Int>>(KEY_HISTORY)?.toList() ?: listOf(restoredCount)

    // Приватный изменяемый поток состояния (инкапсуляция)
    private val _state = MutableStateFlow(
        CounterState(
            count = restoredCount,
            step = restoredStep,
            history = restoredHistory
        )
    )

    // Публичный неизменяемый поток для подписки из UI
    val state: StateFlow<CounterState> = _state.asStateFlow()

    /**
     * Синхронизация ключевых полей состояния с SavedStateHandle.
     *
     * @param currentState Текущее обновлённое состояние.
     */
    private fun saveToHandle(currentState: CounterState) {
        savedStateHandle[KEY_COUNT] = currentState.count
        savedStateHandle[KEY_STEP] = currentState.step
        savedStateHandle[KEY_HISTORY] = ArrayList(currentState.history)
    }

    /**
     * Увеличить счётчик на значение текущего шага.
     * Значение не может превысить [CounterState.maxValue].
     * Новое значение фиксируется в истории.
     */
    fun increment() {
        _state.update { current ->
            val newCount = minOf(current.count + current.step, current.maxValue)
            if (newCount != current.count) {
                current.copy(
                    count = newCount,
                    history = current.history + newCount
                )
            } else {
                current
            }
        }
        saveToHandle(_state.value)
    }

    /**
     * Уменьшить счётчик на значение текущего шага.
     * Значение не может опуститься ниже [CounterState.minValue].
     * Новое значение фиксируется в истории.
     */
    fun decrement() {
        _state.update { current ->
            val newCount = maxOf(current.count - current.step, current.minValue)
            if (newCount != current.count) {
                current.copy(
                    count = newCount,
                    history = current.history + newCount
                )
            } else {
                current
            }
        }
        saveToHandle(_state.value)
    }

    /**
     * Сбросить значение счётчика в 0 и очистить историю изменений.
     */
    fun reset() {
        _state.update { current ->
            current.copy(
                count = 0,
                history = listOf(0)
            )
        }
        saveToHandle(_state.value)
    }

    /**
     * Установить новый шаг изменения счётчика.
     *
     * @param newStep Новое значение шага (например, 1, 5, 10, 25).
     */
    fun setStep(newStep: Int) {
        if (newStep <= 0) return
        _state.update { current ->
            current.copy(step = newStep)
        }
        saveToHandle(_state.value)
    }

    /**
     * Имитация асинхронной загрузки начального значения из внешнего источника (сети или БД).
     * Выполняется с задержкой в 1 секунду с отображением индикатора загрузки.
     */
    fun loadInitialValue() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            delay(1000)
            val initial = 42
            _state.update { current ->
                current.copy(
                    count = initial,
                    isLoading = false,
                    history = current.history + initial
                )
            }
            saveToHandle(_state.value)
        }
    }
}
