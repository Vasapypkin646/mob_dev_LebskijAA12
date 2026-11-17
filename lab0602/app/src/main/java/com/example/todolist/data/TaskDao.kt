package com.example.todolist.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * DAO (Data Access Object) для выполнения операций над таблицей задач.
 *
 * @author Лебский Артём Александрович
 * @since 2026-11-17
 */
@Dao
interface TaskDao {

    /**
     * Добавить новую задачу в базу данных.
     *
     * @param task Задача для добавления.
     * @return ID добавленной записи.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: Task): Long

    /**
     * Обновить существующую задачу.
     *
     * @param task Задача с обновленными данными.
     */
    @Update
    suspend fun update(task: Task)

    /**
     * Удалить задачу из базы данных.
     *
     * @param task Задача для удаления.
     */
    @Delete
    suspend fun delete(task: Task)

    /**
     * Получить все задачи в виде реактивного потока Flow,
     * отсортированные по дате создания (новые сначала).
     *
     * @return Flow со списком всех задач.
     */
    @Query("SELECT * FROM tasks ORDER BY createdAt DESC")
    fun getAllTasks(): Flow<List<Task>>

    /**
     * Получить задачи по статусу выполнения.
     *
     * @param isCompleted Статус выполнения.
     * @return Flow со списком отфильтрованных задач.
     */
    @Query("SELECT * FROM tasks WHERE isCompleted = :isCompleted")
    fun getTasksByStatus(isCompleted: Boolean): Flow<List<Task>>

    /**
     * Получить задачи по категории.
     *
     * @param category Категория задачи.
     * @return Flow со списком задач заданной категории.
     */
    @Query("SELECT * FROM tasks WHERE category = :category ORDER BY createdAt DESC")
    fun getTasksByCategory(category: String): Flow<List<Task>>
}
