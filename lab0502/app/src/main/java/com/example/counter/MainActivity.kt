package com.example.counter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.counter.ui.CounterScreen
import com.example.counter.ui.theme.CounterTheme

/**
 * Главная Activity приложения «MVVM Счётчик».
 * Точка входа в приложение, инициализирующая Compose-интерфейс.
 *
 * @author Лебский Артём Александрович
 * @version 1.0
 * @since 2026-11-02
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CounterTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CounterScreen()
                }
            }
        }
    }
}
