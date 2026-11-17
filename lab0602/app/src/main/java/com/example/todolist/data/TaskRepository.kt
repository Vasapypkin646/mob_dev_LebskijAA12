package com.example.todolist.data

import kotlinx.coroutines.flow.Flow

/**
 * Репозиторий для изоляции и абстрагирования слоя работы с данными.
 * Предоставляет чистый API для взаимодействия ViewModel с Room DAO.
 *
 * @property taskDao Объект доступа к данным задач.
 *
 * @author Лебский Артём Александрович
 * @since 2026-11-17
 */
class TaskRepository(
    private val taskDao: TaskDao
) {

    /**
     * Получить поток всех задач.
     */
    fun getAllTasks(): Flow<List<Task>> = taskDao.getAllTasks()

    /**
     * Добавить новую задачу.
     */
    suspend fun insertTask(task: Task): Long = taskDao.insert(task)

    /**
     * Обновить задачу.
     */
    suspend fun updateTask(task: Task) = taskDao.update(task)

    /**
     * Удалить задачу.
     */
    suspend fun deleteTask(task: Task) = taskDao.delete(task)

    /**
     * Переключить статус выполнения задачи.
     */
    suspend fun toggleTaskCompletion(task: Task) {
        val updatedTask = task.copy(isCompleted = !task.isCompleted)
        taskDao.update(updatedTask)
    }
}
