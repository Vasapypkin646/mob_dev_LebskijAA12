package com.example.hello

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hello.ui.theme.HelloTheme

/**
 * Главная Activity приложения на Jetpack Compose.
 * Отображает экран с приветствием и информацией о пользователе.
 *
 * @author [ВАШЕ ИМЯ]
 * @version 1.0
 * @since 2026-09-03
 */
class MainActivity : ComponentActivity() {

    /**
     * Вызывается при создании Activity.
     * Устанавливает Compose-содержимое экрана.
     *
     * @param savedInstanceState Сохранённое состояние (если есть)
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HelloTheme {
                // TODO: Измените вызов Greeting на вашу реализацию
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }
}

/**
 * Composable-функция, отображающая приветствие.
 *
 * @param name Имя пользователя (или "Android" по умолчанию)
 * @param modifier Модификатор для стилизации
 */
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    // TODO: Измените текст на "Моё первое приложение"
    // TODO: Добавьте второе Text с вашим именем
    // TODO: Измените цвета и отступы
    Text(
		text = "Моё почти первое приложение",
		color = Color.Green,
		modifier = modifier.padding(16.dp)
	)
	Text(
        text = "Лебский Артём",
        modifier = modifier.padding(16.dp),
		color = Color.Red
    )
	Text(
        text = "Пин-б-о-24-1",
        modifier = modifier.padding(16.dp),
		color = Color.Red
    )
}

/**
 * Предпросмотр UI в Android Studio.
 * Позволяет видеть изменения без запуска эмулятора.
 */
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    HelloTheme {
        Greeting("Android")
    }
}