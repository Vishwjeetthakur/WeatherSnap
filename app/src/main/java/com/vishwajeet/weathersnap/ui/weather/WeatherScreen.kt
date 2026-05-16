package com.vishwajeet.weathersnap.ui.weather

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vishwajeet.weathersnap.ui.navigation.Screen
import com.vishwajeet.weathersnap.ui.weather.components.WeatherDetailCard
import com.vishwajeet.weathersnap.ui.weather.components.getWeatherCondition

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(
    viewModel: WeatherViewModel = hiltViewModel(),
    onNavigateToCreateReport: (Screen.CreateReportRoute) -> Unit,
    onNavigateToReports: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsState()

    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF4A7DFF),
            Color(0xFF6A5CFF),
            Color(0xFF8E67FF),
            Color(0xFFB36BFF)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient)
    ) {

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {

                CenterAlignedTopAppBar(
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.Transparent
                    ),
                    title = {
                        Text(
                            text = "WeatherSnap",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    actions = {

                        TextButton(
                            onClick = onNavigateToReports
                        ) {
                            Text(
                                text = "Reports",
                                color = Color.White
                            )
                        }
                    }
                )
            }
        ) { padding ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 18.dp)
                    .verticalScroll(rememberScrollState())
            ) {

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Discover Weather",
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Black
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Search any city for live weather updates",
                    color = Color.White.copy(alpha = 0.82f),
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.height(28.dp))

                OutlinedTextField(
                    value = uiState.searchQuery,
                    onValueChange = {
                        viewModel.onQueryChange(it)
                    },
                    placeholder = {
                        Text(
                            "Search city...",
                            color = Color.White.copy(alpha = 0.65f)
                        )
                    },
                    supportingText = {
                        Text(
                            "Enter more than 2 letters",
                            color = Color.White.copy(alpha = 0.72f)
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = Color.White.copy(alpha = 0.14f),
                        focusedContainerColor = Color.White.copy(alpha = 0.18f),
                        unfocusedBorderColor = Color.Transparent,
                        focusedBorderColor = Color.White.copy(alpha = 0.5f),
                        cursorColor = Color.White,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                AnimatedVisibility(
                    visible = uiState.isSearching,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 14.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Text(
                            text = "Searching cities...",
                            color = Color.White.copy(alpha = 0.82f),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                AnimatedVisibility(
                    visible = uiState.suggestions.isNotEmpty(),
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White.copy(alpha = 0.15f)
                        )
                    ) {

                        LazyColumn(
                            modifier = Modifier.heightIn(max = 240.dp)
                        ) {

                            items(uiState.suggestions) { city ->

                                ListItem(
                                    headlineContent = {
                                        Text(
                                            text = "${city.name}, ${city.admin1 ?: city.country}",
                                            color = Color.White
                                        )
                                    },
                                    supportingContent = {
                                        Text(
                                            text = "Tap to load weather",
                                            color = Color.White.copy(alpha = 0.7f)
                                        )
                                    },
                                    modifier = Modifier.clickable {
                                        viewModel.selectCity(city)
                                    },
                                    colors = ListItemDefaults.colors(
                                        containerColor = Color.Transparent
                                    )
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(26.dp))

                Crossfade(
                    targetState = uiState.isLoading,
                    label = "weather_loading"
                ) { loading ->

                    if (loading) {

                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {

                            CircularProgressIndicator(
                                color = Color.White
                            )
                        }

                    } else {

                        uiState.weatherInfo?.let { weather ->

                            WeatherDetailCard(
                                city = uiState.selectCity?.name ?: "Unknown",
                                temp = weather.current.temperature,
                                humidity = weather.current.humidity,
                                wind = weather.current.windSpeed,
                                pressure = weather.current.pressure,
                                condition = getWeatherCondition(weather.current.weatherCode),
                                onAction = {

                                    onNavigateToCreateReport(
                                        Screen.CreateReportRoute(
                                            cityName = uiState.selectCity?.name ?: "Unknown",
                                            temp = weather.current.temperature,
                                            humidity = weather.current.humidity,
                                            condition = getWeatherCondition(weather.current.weatherCode),
                                            wind = weather.current.windSpeed,
                                            pressure = weather.current.pressure,
                                            )
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}