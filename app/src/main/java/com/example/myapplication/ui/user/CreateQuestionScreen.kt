package com.example.myapplication.ui.user

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import com.example.myapplication.ui.QuizViewModel
import com.example.myapplication.util.SoundManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateQuestionScreen(
    viewModel: QuizViewModel,
    onBackToPanel: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    var questionToDelete by remember { mutableStateOf<Long?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBackToPanel) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver al Panel"
                        )
                    }
                },
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.logo),
                            contentDescription = "Logo",
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(6.dp))
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Crear Preguntas", fontWeight = FontWeight.Bold)
                    }
                },
                actions = {
                    TextButton(onClick = { viewModel.logout() }) {
                        Text("Cerrar Sesión", color = MaterialTheme.colorScheme.primary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp, top = 4.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.imagotipocolegio),
                    contentDescription = "Logo Colegio Profesional de Periodistas de Andalucía",
                    modifier = Modifier.height(30.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Colegio Profesional de Periodistas de Andalucía",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
            }
        },
        modifier = modifier
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.TopCenter
        ) {
            LazyColumn(
                modifier = Modifier
                    .widthIn(max = 480.dp)
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Sección para añadir preguntas
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "Añadir Nueva Pregunta",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )

                            OutlinedTextField(
                                value = uiState.newQuestionText,
                                onValueChange = {
                                    SoundManager.playTypingSound(context)
                                    viewModel.onNewQuestionTextChanged(it)
                                },
                                label = { Text("Enunciado de la pregunta") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            OutlinedTextField(
                                value = uiState.newOptionA,
                                onValueChange = {
                                    SoundManager.playTypingSound(context)
                                    viewModel.onNewOptionAChanged(it)
                                },
                                label = { Text("Opción A") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )

                            OutlinedTextField(
                                value = uiState.newOptionB,
                                onValueChange = {
                                    SoundManager.playTypingSound(context)
                                    viewModel.onNewOptionBChanged(it)
                                },
                                label = { Text("Opción B") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )

                            OutlinedTextField(
                                value = uiState.newOptionC,
                                onValueChange = {
                                    SoundManager.playTypingSound(context)
                                    viewModel.onNewOptionCChanged(it)
                                },
                                label = { Text("Opción C") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )

                            OutlinedTextField(
                                value = uiState.newOptionD,
                                onValueChange = {
                                    SoundManager.playTypingSound(context)
                                    viewModel.onNewOptionDChanged(it)
                                },
                                label = { Text("Opción D") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )

                            // Selector de respuesta correcta
                            Text("Respuesta Correcta:", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                listOf("A", "B", "C", "D").forEach { option ->
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        RadioButton(
                                            selected = uiState.newCorrectAnswer == option,
                                            onClick = {
                                                viewModel.onNewCorrectAnswerChanged(option)
                                            }
                                        )
                                        Text(text = option, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }

                            if (uiState.adminMessage != null) {
                                Text(
                                    text = uiState.adminMessage!!,
                                    color = MaterialTheme.colorScheme.primary,
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Button(
                                onClick = {
                                    viewModel.addQuestion()
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                            ) {
                                Text("Guardar Pregunta", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                // Sección de Preguntas Existentes
                item {
                    Text(
                        text = "Preguntas Existentes (${uiState.questions.size})",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                items(uiState.questions) { question ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = question.questionText,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.weight(1f)
                                )
                                IconButton(onClick = { questionToDelete = question.id }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error)
                                }
                            }
                            Text("A) ${question.optionA}", style = MaterialTheme.typography.bodySmall)
                            Text("B) ${question.optionB}", style = MaterialTheme.typography.bodySmall)
                            Text("C) ${question.optionC}", style = MaterialTheme.typography.bodySmall)
                            Text("D) ${question.optionD}", style = MaterialTheme.typography.bodySmall)
                            Text(
                                text = "Correcta: ${question.correctAnswer}",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }

    // Diálogo de confirmación para eliminar pregunta
    if (questionToDelete != null) {
        AlertDialog(
            onDismissRequest = { questionToDelete = null },
            title = { Text("Confirmar Eliminación", fontWeight = FontWeight.Bold) },
            text = { Text("¿Estás seguro de que deseas eliminar esta pregunta?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteQuestion(questionToDelete!!)
                        questionToDelete = null
                    }
                ) {
                    Text("Eliminar", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { questionToDelete = null }) {
                    Text("Cancelar")
                }
            }
        )
    }
}
