package com.example.counter

import com.example.counter.viewmodel.CounterViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Модульные тесты для проверки бизнес-логики [CounterViewModel].
 *
 * @author Лебский Артём Александрович
 */
class CounterViewModelTest {

    private lateinit var viewModel: CounterViewModel

    @Before
    fun setUp() {
        viewModel = CounterViewModel()
    }

    @Test
    fun testInitialState() {
        val state = viewModel.state.value
        assertEquals(0, state.count)
        assertEquals(1, state.step)
        assertEquals(100, state.maxValue)
        assertEquals(0, state.minValue)
    }

    @Test
    fun testIncrement() {
        viewModel.setStep(5)
        viewModel.increment()
        assertEquals(5, viewModel.state.value.count)
        assertTrue(viewModel.state.value.history.contains(5))
    }

    @Test
    fun testDecrementNotBelowZero() {
        viewModel.decrement()
        assertEquals(0, viewModel.state.value.count)
    }

    @Test
    fun testMaxLimit() {
        viewModel.setStep(25)
        repeat(5) {
            viewModel.increment()
        }
        assertEquals(100, viewModel.state.value.count)
    }

    @Test
    fun testReset() {
        viewModel.increment()
        viewModel.reset()
        assertEquals(0, viewModel.state.value.count)
    }

    @Test
    fun testSetStep() {
        viewModel.setStep(10)
        assertEquals(10, viewModel.state.value.step)
    }
}
