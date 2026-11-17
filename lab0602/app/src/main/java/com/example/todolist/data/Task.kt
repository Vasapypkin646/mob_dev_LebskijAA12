package com.example.todolist.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity, представляющий задачу в локальной базе данных Room.
 *
 * @property id Уникальный автогенерируемый идентификатор задачи.
 * @property title Заголовок / описание задачи.
 * @property isCompleted Флаг выполнения задачи.
 * @property createdAt Временная метка создания задачи в миллисекундах.
 * @property category Категория задачи (например, "Общие", "Работа", "Учёба", "Личное").
 *
 * @author Лебский Артём Александрович
 * @since 2026-11-17
 */
@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val category: String = "Общие"
)
