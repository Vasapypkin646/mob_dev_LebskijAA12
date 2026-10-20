# Отчёт по лабораторной работе №4

## «Навигация и жизненный цикл Activity в Jetpack Compose»

## Сведения о студенте

- **ФИО:** Лебский Артём Александрович
- **Группа:** Пин-б-о-24-1
- **Курс:** 3
- **Семестр:** 1
- **Дата выполнения:** 2026-10-20
- **Дисциплина:** Программирование мобильных устройств
- **ОС:** Debian (Linux)

---

## 1. ЦЕЛЬ РАБОТЫ

> 1. Изучить жизненный цикл Activity в Android.
> 2. Освоить компонент **Navigation** для перехода между экранами в Compose.
> 3. Научиться передавать данные между экранами.
> 4. Реализовать многоэкранное приложение с навигационной цепочкой.
> 5. Подготовить проект к дальнейшей интеграции с архитектурой MVVM.

---

## 2. ЗАДАЧИ РАБОТЫ

**Выполнены следующие задачи:**

> 1. Добавлена зависимость `navigation-compose` в `build.gradle.kts`.
> 2. Создан data-класс `User` с полями id, name, age, city, email, avatarUrl.
> 3. Создан экран списка пользователей `UserListScreen` с `LazyColumn`.
> 4. Создан компонент `UserCard` для отображения карточки пользователя.
> 5. Настроен навигационный граф `NavGraph` с `NavHost` и `NavController`.
> 6. Реализован переход на детальный экран при клике на карточку.
> 7. Создан детальный экран `UserDetailScreen` с информацией о пользователе.
> 8. Реализована передача `userId` через аргументы навигации (`navArgument`).
> 9. Добавлена кнопка «Назад» на детальном экране через `IconButton` и `popBackStack()`.
> 10. Добавлены `@Preview` для всех экранов и компонентов.
> 11. Создана заглушка данных `getSampleUsers()` из 5 пользователей.
> 12. Выполнена сборка проекта и запуск на реальном устройстве Xiaomi Redmi Note 9 Pro.

---

## 3. ХОД ВЫПОЛНЕНИЯ РАБОТЫ

### 3.1. Добавление зависимости

В файл `app/build.gradle.kts` добавлена зависимость для навигации:

```kotlin
dependencies {
    // Jetpack Compose
    implementation(platform("androidx.compose:compose-bom:2024.09.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")

    // Navigation для Compose
    implementation("androidx.navigation:navigation-compose:2.7.7")

    // Жизненный цикл
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.0")
}
```

**Результат:** Навигация подключена к проекту.

### 3.2. Создание data-класса User

Создан data-класс `User`, содержащий информацию о пользователе:

```kotlin
package com.example.navigation.data

data class User(
    val id: Int,
    val name: String,
    val age: Int,
    val city: String,
    val email: String = "",
    val avatarUrl: String? = null
)

fun getSampleUsers(): List<User> = listOf(
    User(1, "Алексей Смирнов", 25, "Москва", "alex@mail.ru"),
    User(2, "Мария Иванова", 30, "Санкт-Петербург", "maria@mail.ru"),
    User(3, "Иван Петров", 22, "Казань", "ivan@mail.ru"),
    User(4, "Екатерина Сидорова", 28, "Новосибирск", "ekaterina@mail.ru"),
    User(5, "Дмитрий Козлов", 35, "Екатеринбург", "dmitry@mail.ru")
)
```

**Результат:** Модель данных и заглушка из 5 пользователей готовы.

### 3.3. Создание компонента UserCard

Компонент `UserCard` реализован в виде карточки с именем, возрастом и городом:

```kotlin
@Composable
fun UserCard(
    user: User,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = user.name,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Возраст: ${user.age}, Город: ${user.city}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
```

**Результат:** Карточка пользователя корректно отображается и реагирует на нажатие.

### 3.4. Создание экрана списка UserListScreen

Экран `UserListScreen` отображает список пользователей в `LazyColumn`:

```kotlin
@Composable
fun UserListScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val users = getSampleUsers()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Список пользователей") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(users) { user ->
                    UserCard(
                        user = user,
                        onClick = {
                            navController.navigate("user_detail/${user.id}")
                        }
                    )
                }
            }
        }
    }
}
```

**Результат:** Список пользователей отображается, при клике выполняется переход на детальный экран.

### 3.5. Настройка навигационного графа NavGraph

Создан `NavGraph` с двумя маршрутами и передачей аргумента `userId`:

```kotlin
@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "user_list"
    ) {
        composable(route = "user_list") {
            UserListScreen(navController = navController)
        }

        composable(
            route = "user_detail/{userId}",
            arguments = listOf(
                navArgument("userId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId") ?: 0
            UserDetailScreen(
                userId = userId,
                navController = navController
            )
        }
    }
}
```

**Результат:** Навигация настроена, данные передаются через аргументы.

### 3.6. Создание детального экрана UserDetailScreen

Детальный экран отображает информацию о пользователе и кнопку «Назад»:

```kotlin
@Composable
fun UserDetailScreen(
    userId: Int,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val user = getSampleUsers().find { it.id == userId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Профиль пользователя") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            if (user != null) {
                Text(
                    text = user.name,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Возраст: ${user.age} лет", fontSize = 18.sp)
                Text(text = "Город: ${user.city}", fontSize = 18.sp)
                Text(text = "Email: ${user.email}", fontSize = 18.sp)
            } else {
                Text(text = "Пользователь не найден")
            }
        }
    }
}
```

**Результат:** Детальный экран корректно отображает данные выбранного пользователя и поддерживает возврат.

### 3.7. Настройка MainActivity

```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NavigationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavGraph()
                }
            }
        }
    }
}
```

**Результат:** Точка входа приложения корректно отображает навигационный граф.

### 3.8. Добавление @Preview

Для экранов и компонентов добавлены аннотации `@Preview`:

```kotlin
@Preview(showBackground = true)
@Composable
fun UserCardPreview() {
    NavigationTheme {
        UserCard(
            user = getSampleUsers().first(),
            onClick = {}
        )
    }
}
```

**Результат:** Предпросмотр работает для всех компонентов.

### 3.9. Сборка и запуск

Выполнена сборка проекта:

```bash
Build → Make Project
```

Результат: **BUILD SUCCESSFUL in 5s**.

Приложение запущено на реальном устройстве **Xiaomi Redmi Note 9 Pro**:
![](./img/img4.jpg)

```text
---------------------------- PROCESS STARTED for package com.example.navigation ----------------------------
10:42 — приложение успешно запущено
```

**Результат:** Экран списка пользователей отображается корректно, переход на детальный экран и возврат работают.

---

## 4. ОТВЕТЫ НА КОНТРОЛЬНЫЕ ВОПРОСЫ

### 1. Какие состояния жизненного цикла Activity вы знаете?

Основные состояния: создание (`onCreate`), старт (`onStart`), возобновление (`onResume`), пауза (`onPause`), остановка (`onStop`), уничтожение (`onDestroy`). В Compose жизненный цикл управляется через `Lifecycle`, но базовые принципы остаются теми же.

### 2. Что такое NavController и для чего он используется?

`NavController` — это компонент, который управляет навигацией: выполняет переходы между экранами, хранит стек навигации и обрабатывает возврат назад. Создаётся через `rememberNavController()`.

### 3. Как добавить навигацию в Compose-проект?

Нужно добавить зависимость `androidx.navigation:navigation-compose` в `build.gradle.kts`, создать `NavHost` с маршрутами через `composable()` и использовать `NavController` для переходов.

### 4. Как передать данные между экранами в Navigation Compose?

Данные передаются через аргументы маршрута: при переходе указывается `navController.navigate("user_detail/${user.id}")`, а в маршруте объявляется `navArgument("userId") { type = NavType.IntType }`. Получение — через `backStackEntry.arguments?.getInt("userId")`.

### 5. Что такое NavHost и как он работает?

`NavHost` — это контейнер для навигации, который связывает `NavController` с маршрутами (`composable`). Он отображает текущий экран в зависимости от состояния навигации.

### 6. Как создать composable-маршрут с аргументом?

Нужно указать маршрут с плейсхолдером: `composable(route = "user_detail/{userId}", arguments = listOf(navArgument("userId") { type = NavType.IntType })) { backStackEntry -> ... }`.

### 7. Как вернуться на предыдущий экран?

Через `navController.popBackStack()` — например, в обработчике кнопки «Назад» в `TopAppBar`.

### 8. Что такое LazyColumn и в чём его преимущество перед Column?

`LazyColumn` — это ленивый список, который создаёт и отображает только видимые элементы. Преимущество перед `Column` — эффективная работа с большими списками и меньший расход памяти.

### 9. Как обработать клик по элементу списка?

Через `Modifier.clickable { onClick() }` на карточке или через параметр `onClick` в компоненте, который вызывает навигацию.

### 10. Как сохранить состояние списка при повороте экрана?

Использовать `rememberSaveable` вместо `remember` или переходить на MVVM с `ViewModel`, который сохраняет состояние при изменении конфигурации.

---

## 5. ИСПОЛЬЗУЕМЫЕ КОМАНДЫ

```bash
# Сборка проекта
Build → Make Project

# Запуск приложения
Run → Run 'app'

# Очистка и пересборка
Build → Clean Project
Build → Rebuild Project
```

---

## 6. ВЫВОДЫ

В ходе выполнения лабораторной работы были изучены и отработаны практические навыки навигации в Jetpack Compose:

> 1. Изучен жизненный цикл Activity: `onCreate`, `onStart`, `onResume`, `onPause`, `onStop`, `onDestroy`.
> 2. Освоен компонент Navigation: `NavHost`, `NavController`, `composable`, `navArgument`.
> 3. Реализована передача данных между экранами через аргументы навигации.
> 4. Создано двухэкранное приложение со списком пользователей и детальным профилем.
> 5. Освоено использование `LazyColumn` для отображения списков.
> 6. Реализована кнопка «Назад» через `popBackStack()`.
> 7. Отработаны навыки разбивки UI на переиспользуемые компоненты (`UserCard`).
> 8. Получен практический опыт сборки и запуска приложения на реальном устройстве.
> 9. Проект подготовлен к дальнейшей интеграции с архитектурой MVVM.
