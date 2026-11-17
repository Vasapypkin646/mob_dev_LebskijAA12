# Отчёт по лабораторной работе №6

## «Локальное хранилище данных. Room Persistence Library»

## Сведения о студенте

- **ФИО:** Лебский Артём Александрович
- **Группа:** Пин-б-о-24-1
- **Курс:** 3
- **Семестр:** 1
- **Дата выполнения:** 17.11.2026
- **Дисциплина:** Программирование мобильных устройств
- **ОС разработки:** Windows 11
- **Целевое устройство тестирования:** Xiaomi Redmi Note 9 Pro (Android)

---

## 1. ЦЕЛЬ РАБОТЫ

1. Изучить библиотеку **Room** для работы с локальной реляционной базой данных SQLite на платформе Android.
2. Освоить создание ключевых компонентов архитектуры Room: **Entity**, **DAO** (Data Access Object) и **Database**.
3. Научиться использовать **Kotlin Coroutines** (`suspend` функции) для выполнения асинхронных операций записи, обновления и удаления.
4. Реализовать реактивное обновление пользовательского интерфейса через **Flow** и **StateFlow**.
5. Реализовать паттерн **Repository** для изоляции доступа к данным.
6. Создать полноценное мобильное приложение «Список задач» (To-Do List) с сохранением состояния, поиском, категориями и сортировкой.

---

## 2. ЗАДАЧИ РАБОТЫ

Выполнены все базовые требования и задачи повышенной сложности на оценку **«Отлично»**:

1. Подключены зависимости Room (`room-runtime`, `room-ktx`, `room-compiler` через KSP), Lifecycle, Coroutines и Jetpack Compose в `build.gradle.kts`.
2. Создан класс сущности `Task` (@Entity) с автогенерируемым первичным ключом (`id`), полями названия (`title`), статуса (`isCompleted`), даты создания (`createdAt`) и категории (`category`).
3. Разработан интерфейс `TaskDao` (@Dao) с методами вставки, обновления, удаления и реактивной выборки задач через `Flow`.
4. Реализован синглтон базы данных `AppDatabase` (@Database) с поддержкой миграции.
5. Реализован репозиторий `TaskRepository` для абстрагирования источника данных от бизнес-логики.
6. Разработан класс `TaskViewModel` с реактивным объединением потоков (`combine`) для динамической фильтрации и сортировки.
7. Реализован удобный интерфейс `TaskListScreen` на Jetpack Compose с отображением задач через `LazyColumn`.
8. Добавлено диалоговое окно добавления новых задач (`AddTaskDialog`) с выбором категории.
9. Реализовано интерактивное переключение статуса выполнения задачи (Checkbox + зачёркивание текста).
10. Реализовано **редактирование задач** через диалоговое окно `EditTaskDialog` (по клику / долгому клику).
11. Реализована **категоризация** задач ("Общие", "Работа", "Учёба", "Личное", "Покупки") с фильтрацией через чипы `FilterChip`.
12. Реализован динамический **поиск задач** по ключевым словам в названии в реальном времени.
13. Реализована гибкая **сортировка задач**: по дате (новые/старые), по алфавиту и по статусу выполнения.
14. Все классы и публичные методы снабжены полной KDoc-документацией.
15. Написаны модульные тесты `TaskViewModelTest` для проверки бизнес-логики (CRUD, поиск, фильтрация).
16. Выполнена сборка и тестирование приложения на физическом устройстве Xiaomi Redmi Note 9 Pro.

---

## 3. АРХИТЕКТУРА ПРИЛОЖЕНИЯ

В приложении реализована чистая архитектура с разделением ответственности по шаблону MVVM + Repository + Room:

```
┌────────────────────────────────────────────────────────┐
│                        VIEW                            │
│  TaskListScreen (Jetpack Compose)                      │
│  - Отображает карточки задач (TaskItem)                │
│  - Строка поиска (OutlinedTextField)                   │
│  - Горизонтальные чипы категорий (LazyRow)             │
│  - Меню выбора сортировки (DropdownMenu)               │
│  - Диалоги добавления и редактирования задач           │
└───────────────────────┬──────────────▲─────────────────┘
                        │              │
        События (Events)│              │ Реактивное состояние
        onClick(), etc. │              │ StateFlow<List<Task>>
                        ▼              │
┌──────────────────────────────────────┴─────────────────┐
│                      VIEWMODEL                         │
│  TaskViewModel                                         │
│  - Управляет поисковым запросом, категорией, порядком  │
│  - Реактивно объединяет потоки через combine()         │
│  - Запускает корутины viewModelScope.launch            │
└───────────────────────┬──────────────▲─────────────────┘
                        │              │
             Вызовы CRUD│              │ Flow<List<Task>>
                        ▼              │
┌──────────────────────────────────────┴─────────────────┐
│                     REPOSITORY                         │
│  TaskRepository                                        │
│  - Изолирует базу данных от UI слоя                    │
└───────────────────────┬──────────────▲─────────────────┘
                        │              │
                        ▼              │
┌──────────────────────────────────────┴─────────────────┐
│                    ROOM DATABASE                       │
│  AppDatabase (Синглтон SQLite)                         │
│  TaskDao (@Insert, @Update, @Delete, @Query)           │
│  Task Entity (@Entity tableName = "tasks")             │
└────────────────────────────────────────────────────────┘
```

---

## 4. СТРУКТУРА ПРОЕКТА

```
lab0602/
├── app/
│   ├── build.gradle.kts
│   └── src/
│       ├── main/
│       │   ├── AndroidManifest.xml
│       │   └── java/com/example/todolist/
│       │       ├── MainActivity.kt               # Точка входа в приложение
│       │       ├── data/
│       │       │   ├── Task.kt                   # Entity задачи
│       │       │   ├── TaskDao.kt                # Data Access Object
│       │       │   ├── AppDatabase.kt            # Room Database синглтон
│       │       │   └── TaskRepository.kt         # Репозиторий
│       │       ├── viewmodel/
│       │       │   └── TaskViewModel.kt          # ViewModel с фильтрацией и сортировкой
│       │       └── ui/
│       │           ├── TaskListScreen.kt         # Экран со списком, поиском и диалогами
│       │           ├── components/
│       │           │   └── TaskItem.kt           # Компонент элемента задачи
│       │           └── theme/                    # Material3 тема приложения
│       │               ├── Color.kt
│       │               ├── Theme.kt
│       │               └── Type.kt
│       └── test/
│           └── java/com/example/todolist/
│               └── TaskViewModelTest.kt          # Модульные тесты ViewModel
├── img/
│   └── img.jpg                                   # Скриншот работы на смартфоне
├── 0602_lab.md                                   # Методическое пособие
├── build.gradle.kts
└── settings.gradle.kts
```

---

## 5. ИСХОДНЫЙ КОД КЛЮЧЕВЫХ КОМПОНЕНТОВ

### 5.1. Сущность `Task.kt`

```kotlin
package com.example.todolist.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity, представляющий задачу в локальной базе данных Room.
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
```

### 5.2. Интерфейс доступа `TaskDao.kt`

```kotlin
package com.example.todolist.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * DAO для выполнения операций над таблицей задач.
 */
@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: Task): Long

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)

    @Query("SELECT * FROM tasks ORDER BY createdAt DESC")
    fun getAllTasks(): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE isCompleted = :isCompleted")
    fun getTasksByStatus(isCompleted: Boolean): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE category = :category ORDER BY createdAt DESC")
    fun getTasksByCategory(category: String): Flow<List<Task>>
}
```

### 5.3. База данных `AppDatabase.kt`

```kotlin
package com.example.todolist.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Основной класс базы данных приложения (RoomDatabase).
 */
@Database(
    entities = [Task::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "task_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
```

### 5.4. Репозиторий `TaskRepository.kt`

```kotlin
package com.example.todolist.data

import kotlinx.coroutines.flow.Flow

class TaskRepository(
    private val taskDao: TaskDao
) {
    fun getAllTasks(): Flow<List<Task>> = taskDao.getAllTasks()
    suspend fun insertTask(task: Task): Long = taskDao.insert(task)
    suspend fun updateTask(task: Task) = taskDao.update(task)
    suspend fun deleteTask(task: Task) = taskDao.delete(task)
    suspend fun toggleTaskCompletion(task: Task) {
        val updatedTask = task.copy(isCompleted = !task.isCompleted)
        taskDao.update(updatedTask)
    }
}
```

### 5.5. ViewModel `TaskViewModel.kt`

```kotlin
package com.example.todolist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.todolist.data.Task
import com.example.todolist.data.TaskRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class SortOrder(val displayName: String) {
    DATE_DESC("Сначала новые"),
    DATE_ASC("Сначала старые"),
    TITLE_ASC("По алфавиту (А-Я)"),
    STATUS("Сначала невыполненные")
}

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

    private val _allTasks = repository.getAllTasks()
        .onEach { _isLoading.value = false }
        .catch { emit(emptyList()) }

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

    fun setSearchQuery(query: String) { _searchQuery.value = query }
    fun setCategory(category: String) { _selectedCategory.value = category }
    fun setSortOrder(order: SortOrder) { _sortOrder.value = order }

    fun addTask(title: String, category: String = "Общие") {
        if (title.isBlank()) return
        viewModelScope.launch {
            repository.insertTask(Task(title = title.trim(), category = category.ifBlank { "Общие" }))
        }
    }

    fun updateTask(task: Task, newTitle: String, newCategory: String) {
        if (newTitle.isBlank()) return
        viewModelScope.launch {
            repository.updateTask(task.copy(title = newTitle.trim(), category = newCategory.ifBlank { "Общие" }))
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch { repository.deleteTask(task) }
    }

    fun toggleTaskCompletion(task: Task) {
        viewModelScope.launch { repository.toggleTaskCompletion(task) }
    }
}
```

---

## 6. РЕЗУЛЬТАТЫ ТЕСТИРОВАНИЯ НА УСТРОЙСТВЕ

Приложение успешно установлено и запущено на смартфоне **Xiaomi Redmi Note 9 Pro**:
1. **Установка:** Сборка и отправка APK выполнены через команду `adb install -r`.
2. **Проверка сохранения в SQLite:** При перезапуске приложения или смене ориентации экрана все созданные задачи, их категории и статусы сохраняются без потерь.
3. **Проверка реактивности (Flow):** При добавлении или удалении задачи список мгновенно перерисовывается в `LazyColumn`.
4. **Проверка фильтрации и поиска:** При вводе символов в поле поиска или переключении категорий ("Работа", "Учёба", "Покупки") отображаются только соответствующие записи.
5. **Проверка сортировки:** Меню в шапке экрана позволяет мгновенно упорядочить задачи по дате, названию или выполнению.

![Скриншот экрана приложения](img/img.jpg)

---

## 7. ОТВЕТЫ НА КОНТРОЛЬНЫЕ ВОПРОСЫ

### 1. Что такое Room и для чего он используется?
**Ответ:** Room — это библиотека объектно-реляционного отображения (ORM) от компании Google, входящая в состав Android Jetpack. Она предоставляет удобный абстрактный слой поверх низкоуровневой базы данных SQLite. Room используется для надёжного локального хранения структурированных данных приложения, обеспечивая типобезопасность SQL-запросов во время компиляции, интеграцию с корутинами и реактивными потоками Flow.

### 2. Какие три основных компонента есть в Room?
**Ответ:**
1. **Entity (Сущность):** Модельный класс данных, помеченный аннотацией `@Entity`, который определяет структуру таблицы в базе данных (колонки, первичные ключи, индексы).
2. **DAO (Data Access Object):** Интерфейс с аннотацией `@Dao`, содержащий методы для выполнения операций с базой данных (`@Insert`, `@Update`, `@Delete`, `@Query`).
3. **Database (База данных):** Абстрактный класс, наследующий `RoomDatabase` с аннотацией `@Database`, который служит главной точкой доступа к соединению и связывает сущности с DAO.

### 3. Что такое Entity и как её создать?
**Ответ:** Entity — это класс Kotlin, представляющий одну строку таблицы базы данных. Для её создания класс помечается аннотацией `@Entity(tableName = "название_таблицы")`, а хотя бы одно поле объявляется первичным ключом с помощью `@PrimaryKey(autoGenerate = true)`. Поля класса соответствуют колонкам таблицы.

### 4. Что такое DAO и какие методы в нём бывают?
**Ответ:** DAO (Data Access Object) — это шаблон проектирования и интерфейс Room, который инкапсулирует низкоуровневую работу с SQL-запросами. В нём используются стандартные аннотации:
- `@Insert` — вставка записей;
- `@Update` — обновление существующих записей по первичному ключу;
- `@Delete` — удаление записей;
- `@Query("SQL")` — выполнение произвольных SQL-запросов на выборку, группировку, агрегацию или фильтрацию.

### 5. Как использовать корутины с Room?
**Ответ:** Методы DAO, выполняющие однократные операции записи, обновления или удаления, объявляются со спецификатором `suspend`:
```kotlin
@Insert suspend fun insert(task: Task): Long
```
Room автоматически переключает выполнение таких методов в фоновый диспетчер потоков (`Dispatchers.IO`), гарантируя, что основной поток пользовательского интерфейса (Main Thread) не заблокируется.

### 6. В чём преимущество Flow перед LiveData?
**Ответ:**
1. **Поддержка чистым Kotlin:** `Flow` — часть стандартной библиотеки `kotlinx.coroutines`, что делает код переносимым в Kotlin Multiplatform (KMP).
2. **Полноценная асинхронность и холодные потоки:** Flow начинает производить значения только тогда, когда появляется активный сборщик (subscriber).
3. **Мощный набор операторов:** Flow поддерживает цепочки преобразований (`map`, `filter`, `combine`, `debounce`, `flatMapLatest`).
4. **Безопасная работа с жизненным циклом:** Подписка через `collectAsState()` или `collectAsStateWithLifecycle()` автоматически управляет жизненным циклом в Jetpack Compose.

### 7. Как создать синглтон базы данных?
**Ответ:** Класс базы данных объявляется абстрактным с аннотацией `@Database`, а в его `companion object` реализуется синглтон с ключевым словом `@Volatile` и блоком `synchronized(this)`:
```kotlin
companion object {
    @Volatile private var INSTANCE: AppDatabase? = null
    fun getInstance(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "task_database"
            ).build()
            INSTANCE = instance
            instance
        }
    }
}
```
Это предотвращает создание нескольких экземпляров базы данных, что экономит память и исключает конфликты блокировок SQLite.

### 8. Что такое Repository и зачем он нужен?
**Ответ:** Репозиторий (Repository) — это класс архитектурного слоя данных, который служит единой точкой доступа к информации для ViewModel. Он скрывает детали того, откуда берутся данные (из локальной БД Room, кэша в памяти или REST API сети), предоставляя ViewModel чистый интерфейс. Это упрощает подмену источников данных и облегчает модульное тестирование (Unit Testing).

### 9. Как подписаться на изменения в БД из Compose?
**Ответ:** Если метод DAO возвращает поток `Flow<List<T>>`, во ViewModel он трансформируется в `StateFlow` с помощью функции `.stateIn(...)`. В Composable-функции подписка оформляется через вызов `collectAsState()`:
```kotlin
val tasks by viewModel.tasks.collectAsState()
```
Каждый раз, когда в базе данных происходят изменения (добавление, удаление, редактирование), Room автоматически отправляет обновлённый список в поток, а Compose запускает рекомпозицию UI.

### 10. Как обновить UI при изменении данных в БД?
**Ответ:** Благодаря тому, что метод DAO возвращает `Flow<List<Task>>`, программисту не нужно вручную вызывать функции обновления списка. Room отслеживает изменения в таблицах SQLite через механизм `InvalidationTracker` и автоматически генерирует новое событие в Flow. UI, подписанный через `collectAsState()`, мгновенно получает актуальное состояние и перерисовывает необходимые элементы списка `LazyColumn`.

---

## 8. ВЫВОДЫ

В ходе выполнения лабораторной работы:
1. Изучены теоретические основы и архитектура библиотеки **Room Persistence Library**.
2. Разработаны все ключевые компоненты локальной базы данных: Entity `Task`, `TaskDao` и синглтон `AppDatabase`.
3. Настроен репозиторий `TaskRepository` и реактивная ViewModel со сложной композицией потоков через `combine()`.
4. Реализовано современное Android-приложение «Список задач» с поиском, фильтрацией по категориям, сортировкой и редактированием.
5. Написаны модульные тесты для проверки бизнес-логики.
6. Приложение успешно собрано, протестировано и проверено на реальном смартфоне Xiaomi Redmi Note 9 Pro.
