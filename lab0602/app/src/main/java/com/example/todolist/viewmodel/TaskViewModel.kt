package com.example.todolist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.todolist.data.Task
import com.example.todolist.data.TaskRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Варианты сортировки списка задач.
 */
enum class SortOrder(val displayName: String) {
    DATE_DESC("Сначала новые"),
    DATE_ASC("Сначала старые"),
    TITLE_ASC("По алфавиту (А-Я)"),
    STATUS("Сначала невыполненные")
}

/**
 * ViewModel для управления состоянием экрана списка задач,
 * фильтрацией, поиском, сортировкой и операциями с базой данных Room.
 *
 * @property repository Репозиторий для доступа к локальным данным.
 * @property autoPopulate Флаг автоматического добавления начальных демо-задач при пустой базе.
 *
 * @author Лебский Артём Александрович
 * @since 2026-11-17
 */
class TaskViewModel(
    private val repository: TaskRepository,
    private val autoPopulate: Boolean = false
) : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("Все")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _sortOrder = MutableStateFlow(SortOrder.DATE_DESC)
    val sortOrder: StateFlow<SortOrder> = _sortOrder.asStateFlow()

    // Исходный список всех задач из базы данных
    private val _allTasks = repository.getAllTasks()
        .onEach { _isLoading.value = false }
        .catch { emit(emptyList()) }

    // Результирующий реактивный поток с учётом поиска, фильтрации по категориям и сортировки
    val tasks: StateFlow<List<Task>> = combine(
        _allTasks,
        _searchQuery,
        _selectedCategory,
        _sortOrder
    ) { all, query, category, sort ->
        all
            .filter { task ->
                (category == "Все" || task.category.equals(category, ignoreCase = true)) &&
                        (query.isBlank() || task.title.contains(query, ignoreCase = true))
            }
            .let { filtered ->
                when (sort) {
                    SortOrder.DATE_DESC -> filtered.sortedByDescending { it.createdAt }
                    SortOrder.DATE_ASC -> filtered.sortedBy { it.createdAt }
                    SortOrder.TITLE_ASC -> filtered.sortedBy { it.title.lowercase() }
                    SortOrder.STATUS -> filtered.sortedWith(
                        compareBy<Task> { it.isCompleted }.thenByDescending { it.createdAt }
                    )
                }
            }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    init {
        if (autoPopulate) {
            viewModelScope.launch {
                val existing = repository.getAllTasks().first()
                if (existing.isEmpty()) {
                    repository.insertTask(Task(title = "Изучить Room Persistence Library", isCompleted = true, category = "Учёба"))
                    repository.insertTask(Task(title = "Выполнить ЛР6 по мобильной разработке", isCompleted = false, category = "Учёба"))
                    repository.insertTask(Task(title = "Подготовить отчёт к защите", isCompleted = false, category = "Работа"))
                    repository.insertTask(Task(title = "Купить кофе и продукты", isCompleted = false, category = "Покупки"))
                }
            }
        }
    }

    /**
     * Установить поисковый запрос по названию задач.
     */
    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    /**
     * Выбрать категорию для фильтрации.
     */
    fun setCategory(category: String) {
        _selectedCategory.value = category
    }

    /**
     * Изменить порядок сортировки задач.
     */
    fun setSortOrder(order: SortOrder) {
        _sortOrder.value = order
    }

    /**
     * Добавить новую задачу в базу данных.
     *
     * @param title Название задачи.
     * @param category Категория задачи.
     */
    fun addTask(title: String, category: String = "Общие") {
        if (title.isBlank()) return
        viewModelScope.launch {
            val task = Task(
                title = title.trim(),
                category = category.ifBlank { "Общие" }
            )
            repository.insertTask(task)
        }
    }

    /**
     * Обновить название и категорию существующей задачи (редактирование).
     *
     * @param task Исходная задача.
     * @param newTitle Новое название задачи.
     * @param newCategory Новая категория.
     */
    fun updateTask(task: Task, newTitle: String, newCategory: String) {
        if (newTitle.isBlank()) return
        viewModelScope.launch {
            val updated = task.copy(
                title = newTitle.trim(),
                category = newCategory.ifBlank { "Общие" }
            )
            repository.updateTask(updated)
        }
    }

    /**
     * Удалить задачу из базы данных.
     *
     * @param task Задача для удаления.
     */
    fun deleteTask(task: Task) {
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }

    /**
     * Переключить статус выполнения задачи (выполнена / активна).
     *
     * @param task Задача для переключения статуса.
     */
    fun toggleTaskCompletion(task: Task) {
        viewModelScope.launch {
            repository.toggleTaskCompletion(task)
        }
    }
}

/**
 * Фабрика для создания экземпляра [TaskViewModel] с внедрением [TaskRepository].
 */
class TaskViewModelFactory(
    private val repository: TaskRepository,
    private val autoPopulate: Boolean = true
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TaskViewModel(repository, autoPopulate) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
