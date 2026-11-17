package com.example.todolist

import com.example.todolist.data.Task
import com.example.todolist.data.TaskDao
import com.example.todolist.data.TaskRepository
import com.example.todolist.viewmodel.SortOrder
import com.example.todolist.viewmodel.TaskViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Фейковый DAO для тестирования ViewModel без запуска реальной SQLite базы данных.
 */
class FakeTaskDao : TaskDao {
    private val taskList = mutableListOf<Task>()
    private val flow = MutableStateFlow<List<Task>>(emptyList())

    private fun emit() {
        flow.value = taskList.toList()
    }

    override suspend fun insert(task: Task): Long {
        val newTask = if (task.id == 0) task.copy(id = taskList.size + 1) else task
        taskList.add(newTask)
        emit()
        return newTask.id.toLong()
    }

    override suspend fun update(task: Task) {
        val index = taskList.indexOfFirst { it.id == task.id }
        if (index != -1) {
            taskList[index] = task
            emit()
        }
    }

    override suspend fun delete(task: Task) {
        taskList.removeAll { it.id == task.id }
        emit()
    }

    override fun getAllTasks(): Flow<List<Task>> = flow

    override fun getTasksByStatus(isCompleted: Boolean): Flow<List<Task>> {
        return MutableStateFlow(taskList.filter { it.isCompleted == isCompleted })
    }

    override fun getTasksByCategory(category: String): Flow<List<Task>> {
        return MutableStateFlow(taskList.filter { it.category == category })
    }
}

/**
 * Модульные тесты для проверки бизнес-логики [TaskViewModel].
 *
 * @author Лебский Артём Александрович
 */
@OptIn(ExperimentalCoroutinesApi::class)
class TaskViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var fakeDao: FakeTaskDao
    private lateinit var repository: TaskRepository
    private lateinit var viewModel: TaskViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeDao = FakeTaskDao()
        repository = TaskRepository(fakeDao)
        viewModel = TaskViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testAddTask() = runTest {
        backgroundScope.launch { viewModel.tasks.collect() }
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.addTask("Купить продукты", "Покупки")
        testDispatcher.scheduler.advanceUntilIdle()

        val list = viewModel.tasks.value
        assertEquals(1, list.size)
        assertEquals("Купить продукты", list[0].title)
        assertEquals("Покупки", list[0].category)
    }

    @Test
    fun testToggleTaskCompletion() = runTest {
        backgroundScope.launch { viewModel.tasks.collect() }
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.addTask("Сдать ЛР6", "Учёба")
        testDispatcher.scheduler.advanceUntilIdle()

        val task = viewModel.tasks.value.first()
        viewModel.toggleTaskCompletion(task)
        testDispatcher.scheduler.advanceUntilIdle()

        val updated = viewModel.tasks.value.first()
        assertTrue(updated.isCompleted)
    }

    @Test
    fun testDeleteTask() = runTest {
        backgroundScope.launch { viewModel.tasks.collect() }
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.addTask("Удалить меня", "Общие")
        testDispatcher.scheduler.advanceUntilIdle()

        val task = viewModel.tasks.value.first()
        viewModel.deleteTask(task)
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.tasks.value.isEmpty())
    }

    @Test
    fun testSearchFilter() = runTest {
        backgroundScope.launch { viewModel.tasks.collect() }
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.addTask("Математика", "Учёба")
        viewModel.addTask("Физика", "Учёба")
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.setSearchQuery("мат")
        testDispatcher.scheduler.advanceUntilIdle()

        val result = viewModel.tasks.value
        assertEquals(1, result.size)
        assertEquals("Математика", result[0].title)
    }

    @Test
    fun testCategoryFilter() = runTest {
        backgroundScope.launch { viewModel.tasks.collect() }
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.addTask("Отчёт", "Работа")
        viewModel.addTask("Книга", "Личное")
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.setCategory("Работа")
        testDispatcher.scheduler.advanceUntilIdle()

        val result = viewModel.tasks.value
        assertEquals(1, result.size)
        assertEquals("Отчёт", result[0].title)
    }
}
