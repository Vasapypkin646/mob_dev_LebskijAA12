# Отчёт по лабораторной работе №2

## Информация о студенте
- **ФИО:** Лебский Артём Александрович
- **Группа:** Пин-б-о-24-1
- **Курс:** 3, семестр 1
- **Дисциплина:** Программирование мобильных устройств
- **Дата выполнения:** 22.09.2026
- **ОС:** Debian (Linux)

---

## 1. ЦЕЛЬ РАБОТЫ

1. Закрепить базовый синтаксис языка Kotlin.
2. Научиться работать с **null-безопасностью**, коллекциями и функциями высшего порядка.
3. Освоить основы **корутин (Coroutines)** для асинхронного программирования.
4. Применить полученные знания для решения практических задач.
5. Подготовить проект к интеграции с Android-приложением (Kotlin/JVM).

---

## 2. ЗАДАЧИ РАБОТЫ

**Выполнены следующие задачи:**

> 1. Настройка среды разработки (IntelliJ IDEA Community на Debian).
> 2. Реализация задач блока 1 «Базовый синтаксис» (greetUser, getSeason, factorial).
> 3. Реализация задач блока 2 «Null-безопасность» (parseIntSafe, filterNonNullAndDouble).
> 4. Реализация задач блока 3 «Коллекции и функции высшего порядка» (averageAge, groupByFirstLetter, findLongestWord).
> 5. Реализация задач блока 4 «Корутины» (delayedPrint, runParallelTasks).
> 6. Реализация дополнительного задания на оценку «Отлично» (processUserInput).
> 7. Написание KDoc-комментариев для всех функций.
> 8. Создание тестов в функции main() для проверки всех решений.
> 9. Оформление документации README.md и отчёта REPORT.md.
> 10. Загрузка проекта в публичный репозиторий GitLab.

---

## 3. ХОД ВЫПОЛНЕНИЯ РАБОТЫ

### 3.1. Настройка среды разработки

Настройка выполнялась на операционной системе Debian (Linux). Был установлен SDKMAN и через него — компилятор Kotlin:

```bash
curl -s "https://get.sdkman.io" | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"
sdk install kotlin
kotlin -version
```

Дополнительно была установлена IntelliJ IDEA Community Edition:

```bash
wget -O idea.tar.gz "https://download.jetbrains.com/idea/ideaIC-2024.1.4.tar.gz"
tar -xzf idea.tar.gz
cd idea-IC-241.18034.62/bin
./idea.sh
```

### 3.2. Структура проекта

Проект организован в соответствии с шаблоном лабораторной работы:

```text
kotlin-tasks/
├── src/
│   └── main/
│       └── kotlin/
│           ├── Main.kt                 # Точка входа, тестирование функций
│           ├── tasks/
│           │   ├── BasicSyntax.kt      # Задачи блока 1 (1.1–1.3)
│           │   ├── NullSafety.kt       # Задачи блока 2 (2.1–2.2)
│           │   ├── Collections.kt      # Задачи блока 3 (3.1–3.3)
│           │   ├── Coroutines.kt       # Задачи блока 4 (4.1–4.2)
│           │   ├── DopTask.kt        # Дополнительное задание
│           │   └── DataClasses.kt      # Data class Person
│           └── utils/
│               └── TestUtils.kt        # Вспомогательные функции
├── build.gradle.kts
├── README.md
└── .gitignore
```

### 3.3. Блок 1. Базовый синтаксис

**Задача 1.1. Приветствие пользователя**

Реализована функция greetUser, формирующая приветствие с вычислением возраста через 10 лет:

```kotlin
fun greetUser(name: String, age: Int): String {
    return "Привет, $name! Через 10 лет тебе будет ${age + 10} лет."
}
```

**Задача 1.2. Определение времени года**

Для определения сезона использована конструкция when с диапазонами:

```kotlin
fun getSeason(month: Int): String {
    val result = when (month) {
        12, 1, 2 -> "Зима"
        3, 4, 5  -> "Весна"
        in 6..8  -> "Лето"
        in 9..11 -> "Осень"
        else     -> "Некорректный месяц"
    }
    return result
}
```

**Задача 1.3. Факториал числа (tailrec)**

Реализована хвостовая рекурсия с аккумулятором. Компилятор Kotlin автоматически оптимизирует её в цикл, что исключает переполнение стека:

```kotlin
tailrec fun factorial(n: Int, accumulator: Long = 1L): Long {
    return if (n <= 1) accumulator
           else factorial(n - 1, n * accumulator)
}
```

### 3.4. Блок 2. Null-безопасность

**Задача 2.1. Безопасное преобразование строки в число**

Использована функция toIntOrNull(), которая возвращает null вместо исключения:

```kotlin
fun parseIntSafe(str: String?): Int? {
    return str?.toIntOrNull()
}
```

**Задача 2.2. Фильтрация null и удвоение**

Реализована цепочка функций высшего порядка filterNotNull и map:

```kotlin
fun filterNonNullAndDouble(list: List<Int?>): List<Int> {
    return list.filterNotNull().map { it * 2 }
}
```

### 3.5. Блок 3. Коллекции и функции высшего порядка

**Задача 3.1. Средний возраст**

```kotlin
fun averageAge(people: List<Person>): Double {
    return if (people.isEmpty()) 0.0
           else people.map { it.age }.average()
}
```

**Задача 3.2. Группировка слов по первой букве**

```kotlin
fun groupByFirstLetter(words: List<String>): Map<Char, List<String>> {
    return words
        .filter { it.isNotEmpty() }
        .groupBy { it.first().uppercaseChar() }
}
```

**Задача 3.3. Поиск самого длинного слова**

```kotlin
fun findLongestWord(words: List<String>): String? {
    return words.maxByOrNull { it.length }
}
```

### 3.6. Блок 4. Корутины

**Задача 4.1. Асинхронная задержка**

Реализована suspend-функция с использованием delay:

```kotlin
suspend fun delayedPrint(message: String, delayMs: Long) {
    println(message)
    delay(delayMs)
    println("Готово!")
}
```

**Задача 4.2. Параллельное выполнение задач**

Для параллельного запуска трёх задач использованы async/await внутри coroutineScope:

```kotlin
suspend fun runParallelTasks(): List<String> = coroutineScope {
    val deferred1 = async { delay(1000); "Результат 1" }
    val deferred2 = async { delay(2000); "Результат 2" }
    val deferred3 = async { delay(3000); "Результат 3" }

    listOf(deferred1.await(), deferred2.await(), deferred3.await())
}
```

### 3.7. Дополнительное задание (на «Отлично»)

Реализована функция processUserInput, которая определяет тип входных данных (числа или текст) и возвращает соответствующую статистику:

```kotlin
fun processUserInput(input: String?): Map<String, Any> {
    if (input.isNullOrBlank()) return emptyMap()

    val trimmed = input.trim()
    val parts = trimmed.split(",").map { it.trim() }
    val numbers = parts.mapNotNull { it.toIntOrNull() }

    return if (numbers.size == parts.size && numbers.isNotEmpty()) {
        mapOf(
            "sum" to numbers.sum(),
            "average" to numbers.average(),
            "count" to numbers.size,
            "sorted" to numbers.sorted()
        )
    } else {
        val words = trimmed.split(Regex("\\\\s+")).filter { it.isNotEmpty() }
        mapOf(
            "wordCount" to words.size,
            "longestWord" to (words.maxByOrNull { it.length } ?: ""),
            "charCount" to trimmed.replace(" ", "").length
        )
    }
}
```

### 3.8. Тестирование в Main.kt

В функции main() собраны проверки всех реализованных задач с ожидаемым выводом в комментариях:

```kotlin
fun main() {
    println("=== Тестирование лабораторной работы №2 ===\\n")

    println("--- Блок 1. Базовый синтаксис ---")
    println(BasicSyntax.greetUser("Анна", 25))
    println(BasicSyntax.getSeason(3))
    println(BasicSyntax.factorial(5))

    println("--- Блок 2. Null-безопасность ---")
    println(NullSafety.parseIntSafe("123"))
    println(NullSafety.parseIntSafe("abc"))
    println(NullSafety.filterNonNullAndDouble(listOf(1, null, 3, null, 5)))

    println("--- Блок 3. Коллекции ---")
    val people = listOf(Person("Анна", 25), Person("Иван", 30), Person("Мария", 35))
    println(Collections.averageAge(people))
    println(Collections.groupByFirstLetter(listOf("apple", "apricot", "banana")))
    println(Collections.findLongestWord(listOf("apple", "banana", "pineapple")))

    println("--- Блок 4. Корутины ---")
    runBlocking {
        Coroutines.delayedPrint("Начинаем...", 2000L)
        println(Coroutines.runParallelTasks())
    }

    println("--- Дополнительное задание ---")
    println(ExtraTask.processUserInput("1,2,3,4,5"))
    println(ExtraTask.processUserInput("Hello Kotlin World"))
    println(ExtraTask.processUserInput(null))
}
```

---

## 4. РЕЗУЛЬТАТЫ ВЫПОЛНЕНИЯ

Вывод программы при запуске Main.kt:

```text
=== Тестирование лабораторной работы №2 ===

--- Блок 1. Базовый синтаксис ---
Привет, Анна! Через 10 лет тебе будет 35 лет.
Весна
120

--- Блок 2. Null-безопасность ---
123
null
[2, 6, 10]

--- Блок 3. Коллекции ---
30.0
{A=[apple, apricot], B=[banana]}
pineapple

--- Блок 4. Корутины ---
Начинаем...
Готово!
[Результат 1, Результат 2, Результат 3]

--- Дополнительное задание ---
{sum=15, average=3.0, count=5, sorted=[1, 2, 3, 4, 5]}
{wordCount=3, longestWord=Kotlin, charCount=16}
{}
```

---

## 5. ОТВЕТЫ НА КОНТРОЛЬНЫЕ ВОПРОСЫ

### 1. В чём разница между val и var?

**val** объявляет неизменяемую ссылку — значение можно присвоить только один раз при инициализации. **var** объявляет изменяемую переменную, значение которой можно переприсваивать. По соглашениям Kotlin рекомендуется использовать val везде, где это возможно — это уменьшает количество ошибок и делает код более предсказуемым.

```kotlin
val name = "Анна"   // нельзя переприсвоить
var age = 25        // можно: age = 26
```

### 2. Что такое null-безопасность и как она реализована в Kotlin?

Kotlin на уровне системы типов разделяет nullable- и non-nullable-типы. По умолчанию переменная не может содержать null. Чтобы разрешить null, используется знак вопроса:

```kotlin
var a: String = "text"    // не может быть null
var b: String? = null     // может быть null
```

Основные инструменты:
- **?. (safe call)** — вызов метода только если объект не null.
- **?: (elvis)** — значение по умолчанию, если слева null.
- **!! (non-null assertion)** — принудительное разыменование с риском NPE (не рекомендуется).
- **?.let { }** — безопасное выполнение блока.

### 3. Что такое tailrec и когда его следует использовать?

**tailrec** — модификатор, указывающий компилятору, что функция является хвостовой рекурсией. Компилятор преобразует её в обычный цикл, что исключает рост стека вызовов и StackOverflowError. Условие: рекурсивный вызов должен быть **последней** операцией функции. Применяется в алгоритмах, где естественная реализация — рекурсивная, но глубина может быть большой (обходы, факториал, вычисления по накопителю).

### 4. Чем filter отличается от filterNotNull?

- **filter** принимает предикат и оставляет элементы, для которых он истинен. Работает с любыми типами и любыми условиями.
- **filterNotNull** — специализированная функция для коллекций с nullable-элементами. Она отбрасывает все null и **сужает тип** с List<T?> до List<T>.

```kotlin
val list = listOf(1, null, 3)
list.filter { it != null }      // List<Int?> — тип не меняется
list.filterNotNull()            // List<Int> — тип сужен
```

### 5. Что такое функции высшего порядка? Приведите пример.

Функции высшего порядка — это функции, которые **принимают другие функции как параметры** или **возвращают функцию**. В Kotlin функции — объекты первого класса.

Пример из работы: groupBy принимает лямбду-селектор ключа:

```kotlin
words.groupBy { it.first().uppercaseChar() }
```

Пример собственной функции высшего порядка:

```kotlin
fun applyTwice(x: Int, op: (Int) -> Int): Int = op(op(x))
applyTwice(3) { it * 2 }  // 12
```

### 6. Что такое корутины и зачем они нужны?

Корутины — механизм асинхронного программирования в Kotlin. Они позволяют писать асинхронный код в последовательном стиле без блокировки потока. Корутина приостанавливается в точке suspend, освобождая поток для других задач, и возобновляется позже. Это эффективнее потоков: тысячи корутин могут выполняться в одном потоке.

### 7. В чём разница между launch и async?

- **launch** — запускает корутину «выстрелил и забыл», возвращает Job без результата. Используется для операций, результат которых не нужен.
- **async** — запускает корутину, возвращает Deferred<T> — контейнер с будущим результатом. Результат получается через await(). Используется для параллельных вычислений.

```kotlin
val job: Job = launch { doSomething() }
val deferred: Deferred<Int> = async { compute() }
val result = deferred.await()
```

### 8. Что такое suspend-функция?

**suspend** — модификатор, помечающий функцию, которая может приостанавливать выполнение корутины. Такие функции можно вызывать только из корутин или других suspend-функций. Внутри suspend-функции можно использовать delay, await и другие приостанавливающие операции. Компилятор превращает её в state machine, сохраняющую состояние между приостановками.

### 9. Как работает groupBy в Kotlin?

groupBy — функция, которая принимает лямбду-селектор ключа и возвращает Map<K, List<T>>. Для каждого элемента вычисляется ключ, и элемент добавляется в список, ассоциированный с этим ключом. Порядок элементов внутри списка сохраняется. Пример: words.groupBy { it.first() } — сгруппирует слова по первой букве.

### 10. Что такое data class и зачем он нужен?

**data class** — класс, для которого компилятор автоматически генерирует:
- equals() и hashCode() — сравнение по полям;
- toString() — читаемое представление;
- copy() — создание копии с изменёнными полями;
- componentN() — функции для деструктуризации.

Используется для классов-значений (DTO, модели, события).

```kotlin
data class Person(val name: String, val age: Int)
val p = Person("Анна", 25)
val (n, a) = p           // деструктуризация
val p2 = p.copy(age = 26) // копия с изменением
```

---

## 6. С какими сложностями столкнулись

1. **Сложность:** Поначалу было непривычно использовать tailrec для рекурсии.
   - **Решение:** Изучил документацию и понял, что компилятор превращает такую рекурсию в цикл, поэтому вызов должен быть последним, а результат — копиться в аккумуляторе.

2. **Сложность:** Освоение корутин — async и await казались сложными.
   - **Решение:** Написал несколько простых примеров с одной задачей, потом перешёл к параллельному запуску трёх задач в coroutineScope.

3. **Сложность:** Разобраться с groupBy и лямбда-выражениями.
   - **Решение:** Использовал it.first().uppercaseChar() для группировки по первой букве.

4. **Сложность:** Реализация дополнительного задания — определение типа входных данных (числа vs текст).
   - **Решение:** Применил split(",") + mapNotNull { it.toIntOrNull() } и сравнил количество валидных чисел с общим числом частей.

---

## 7. Что нового узнали

- Как работают tailrec-функции и зачем нужен аккумулятор.
- Null-безопасность в Kotlin: операторы ?., ?:, !! и функции toIntOrNull, isNullOrBlank.
- Функции высшего порядка: filter, map, filterNotNull, groupBy, maxByOrNull, average.
- Основы корутин: runBlocking, coroutineScope, launch, async, await, delay.
- Принципы хвостовой рекурсии и её оптимизации компилятором.

---

## 8. ВЫВОДЫ

В ходе выполнения лабораторной работы были закреплены базовые конструкции языка Kotlin и освоены ключевые инструменты стандартной библиотеки:

> 1. Реализованы все 10 задач четырёх блоков и дополнительное задание на оценку «Отлично».
> 2. Освоена хвостовая рекурсия (tailrec) и её преимущество перед обычной — отсутствие риска StackOverflowError.
> 3. Отработаны приёмы null-безопасности: безопасные вызовы, elvis-оператор, работа с nullable-коллекциями.
> 4. Изучены функции высшего порядка и цепочки преобразований коллекций — код стал компактнее и выразительнее циклов.
> 5. Получен практический опыт работы с корутинами: параллельное выполнение задач через async/await и корректное тестирование через runBlocking.
> 6. Оформлена документация (README.md, REPORT.md) и проект загружен в публичный репозиторий GitLab.

Kotlin показал себя как выразительный и безопасный язык. Особенно понравилась работа с коллекциями, где одна строчка может заменить цикл в несколько строк, и корутины, которые без блокировки потока позволяют выполнять несколько задач одновременно.

---

## 9. ИСПОЛЬЗОВАННЫЕ ИСТОЧНИКИ

1. [Официальная документация Kotlin](https://kotlinlang.org/docs/home.html)
2. [Kotlin для Android-разработчиков](https://developer.android.com/kotlin)
3. [Корутины в Kotlin](https://kotlinlang.org/docs/coroutines-guide.html)
4. [Kotlin Collections — операции](https://kotlinlang.org/docs/collection-operations.html)
5. [Null Safety в Kotlin](https://kotlinlang.org/docs/null-safety.html)
6. [Kotlin Playground](https://play.kotlinlang.org/)
