package com.example.counter.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.counter.data.CounterState
import com.example.counter.ui.theme.CounterTheme
import com.example.counter.viewmodel.CounterViewModel

/**
 * Главный экран счётчика, реализующий паттерн MVVM.
 * Экран подписывается на состояние ViewModel через [collectAsState] и перерисовывается
 * при обновлении [CounterState].
 *
 * @param viewModel Экземпляр [CounterViewModel] для управления состоянием и событиями.
 * @param modifier Модификатор оформления экрана.
 *
 * @author Лебский Артём Александрович
 * @since 2026-11-02
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CounterScreen(
    viewModel: CounterViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    // Подписка на реактивный поток состояния StateFlow
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "MVVM Счётчик",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        CounterContent(
            state = state,
            onIncrement = { viewModel.increment() },
            onDecrement = { viewModel.decrement() },
            onReset = { viewModel.reset() },
            onStepSelected = { step -> viewModel.setStep(step) },
            onLoadInitial = { viewModel.loadInitialValue() },
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        )
    }
}

/**
 * Внутренний компонент отображения содержимого экрана счётчика без прямой привязки к ViewModel.
 * Упрощает предварительный просмотр (@Preview) и изолированное UI-тестирование.
 *
 * @param state Текущее неизменяемое состояние счётчика.
 * @param onIncrement Обработчик нажатия кнопки увеличения.
 * @param onDecrement Обработчик нажатия кнопки уменьшения.
 * @param onReset Обработчик сброса значения счётчика.
 * @param onStepSelected Обработчик выбора шага.
 * @param onLoadInitial Обработчик запуска асинхронной загрузки данных.
 * @param modifier Модификатор оформления.
 */
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CounterContent(
    state: CounterState,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    onReset: () -> Unit,
    onStepSelected: (Int) -> Unit,
    onLoadInitial: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .verticalScroll(scrollState)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // Карточка с числовым значением счётчика и прогрессом
        ElevatedCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 28.dp, horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Текущее значение",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                // Анимированное отображение числа
                AnimatedContent(
                    targetState = state.count,
                    transitionSpec = {
                        if (targetState > initialState) {
                            slideInVertically { height -> height } togetherWith
                                    slideOutVertically { height -> -height }
                        } else {
                            slideInVertically { height -> -height } togetherWith
                                    slideOutVertically { height -> height }
                        }
                    },
                    label = "CounterAnimation"
                ) { countValue ->
                    Text(
                        text = countValue.toString(),
                        style = MaterialTheme.typography.displayLarge,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.ExtraBold
                    )
                }

                // Индикатор достижения лимита (0 .. 100)
                LinearProgressIndicator(
                    progress = { (state.count.toFloat() / state.maxValue.toFloat()).coerceIn(0f, 1f) },
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceContainerHighest
                )

                Row(
                    modifier = Modifier.fillMaxWidth(0.7f),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Мин: ${state.minValue}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Макс: ${state.maxValue}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Блок основных кнопок «−» и «+»
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = onDecrement,
                enabled = state.count > state.minValue && !state.isLoading,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                ),
                modifier = Modifier
                    .weight(1f)
                    .height(64.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Remove,
                    contentDescription = "Уменьшить счётчик",
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "− ${state.step}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Button(
                onClick = onIncrement,
                enabled = state.count < state.maxValue && !state.isLoading,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                modifier = Modifier
                    .weight(1f)
                    .height(64.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Увеличить счётчик",
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "+ ${state.step}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Блок выбора величины шага с помощью RadioButton
        OutlinedCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Шаг счёта:",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                val availableSteps = listOf(1, 5, 10, 25)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    availableSteps.forEach { stepOption ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { onStepSelected(stepOption) }
                                .padding(horizontal = 4.dp, vertical = 4.dp)
                        ) {
                            RadioButton(
                                selected = (state.step == stepOption),
                                onClick = { onStepSelected(stepOption) }
                            )
                            Text(
                                text = "$stepOption",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = if (state.step == stepOption) FontWeight.Bold else FontWeight.Normal,
                                modifier = Modifier.padding(end = 4.dp)
                            )
                        }
                    }
                }
            }
        }

        // Дополнительные управляющие действия: Сброс и Асинхронная загрузка
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onReset,
                enabled = !state.isLoading,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text("Сброс")
            }

            FilledTonalButton(
                onClick = onLoadInitial,
                enabled = !state.isLoading,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .weight(1.3f)
                    .height(50.dp)
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Загрузка...")
                } else {
                    Icon(
                        imageVector = Icons.Default.CloudDownload,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Загрузить (42)")
                }
            }
        }

        // Блок истории изменений (дополнительное задание)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "История изменений",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Действий: ${state.history.size}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                if (state.history.isEmpty()) {
                    Text(
                        text = "История пока пуста",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        itemsIndexed(state.history) { index, historyValue ->
                            val isLast = index == state.history.lastIndex
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(
                                        if (isLast) MaterialTheme.colorScheme.primaryContainer
                                        else MaterialTheme.colorScheme.surfaceContainerHighest
                                    )
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "$historyValue",
                                    color = if (isLast) MaterialTheme.colorScheme.onPrimaryContainer
                                    else MaterialTheme.colorScheme.onSurface,
                                    fontWeight = if (isLast) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CounterScreenPreview() {
    CounterTheme {
        CounterContent(
            state = CounterState(
                count = 42,
                step = 10,
                maxValue = 100,
                history = listOf(0, 10, 20, 30, 40, 42)
            ),
            onIncrement = {},
            onDecrement = {},
            onReset = {},
            onStepSelected = {},
            onLoadInitial = {}
        )
    }
}
