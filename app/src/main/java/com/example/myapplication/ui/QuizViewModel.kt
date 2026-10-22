package com.example.myapplication.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.QuizRepository
import com.example.myapplication.data.local.AppDatabase
import com.example.myapplication.data.local.QuestionEntity
import com.example.myapplication.data.local.UserEntity
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

import androidx.core.content.edit

enum class UserDestination {
    PANEL,
    QUIZ,
    CREATE_QUESTION
}

data class QuizUiState(
    val email: String = "",
    val password: String = "",
    val username: String = "",
    val isRegistering: Boolean = false,
    val isLoggedIn: Boolean = false,
    val isAdmin: Boolean = false,
    val errorMessage: String? = null,
    val questions: List<QuestionEntity> = emptyList(),
    val users: List<UserEntity> = emptyList(),
    
    // User Navigation
    val currentDestination: UserDestination = UserDestination.PANEL,

    // Quiz state
    val currentQuestionIndex: Int = 0,
    val selectedAnswers: Map<Int, String> = emptyMap(), // questionIndex -> selectedOption ("A", "B", "C", "D")
    val quizFinished: Boolean = false,
    val score: Int = 0,

    // Question creation state
    val newQuestionText: String = "",
    val newOptionA: String = "",
    val newOptionB: String = "",
    val newOptionC: String = "",
    val newOptionD: String = "",
    val newCorrectAnswer: String = "A", // "A", "B", "C", "D"
    val adminMessage: String? = null
)

class QuizViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: QuizRepository
    private val sharedPreferences = application.getSharedPreferences("user_session_prefs", Context.MODE_PRIVATE)

    private val _uiState = MutableStateFlow(QuizUiState())
    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()

    init {
        val db = AppDatabase.getDatabase(application)
        repository = QuizRepository(db.appDao())

        // Restore persisted user session if available
        val savedIsLoggedIn = sharedPreferences.getBoolean("is_logged_in", false)
        if (savedIsLoggedIn) {
            val savedEmail = sharedPreferences.getString("logged_email", "") ?: ""
            val savedUsername = sharedPreferences.getString("logged_username", "") ?: ""
            val savedIsAdmin = sharedPreferences.getBoolean("is_admin", false)

            _uiState.update {
                it.copy(
                    isLoggedIn = true,
                    email = savedEmail,
                    username = savedUsername,
                    isAdmin = savedIsAdmin,
                    currentDestination = UserDestination.PANEL
                )
            }
        }

        // Ensure admin user exists in DB on startup
        viewModelScope.launch {
            if (repository.getUserByEmail("admin@admin.es") == null) {
                repository.insertUser(
                    UserEntity(
                        email = "admin@admin.es",
                        password = "admin0090",
                        username = "Administrador",
                        isAdmin = true
                    )
                )
            }
        }

        // Collect questions
        viewModelScope.launch {
            repository.allQuestions.collect { list ->
                _uiState.update { it.copy(questions = list) }
            }
        }

        // Collect users
        viewModelScope.launch {
            repository.allUsers.collect { usersList ->
                _uiState.update { it.copy(users = usersList) }
            }
        }
    }

    private fun saveSession(email: String, username: String, isAdmin: Boolean) {
        sharedPreferences.edit {
            putBoolean("is_logged_in", true)
            putString("logged_email", email)
            putString("logged_username", username)
            putBoolean("is_admin", isAdmin)
        }
    }

    private fun clearSession() {
        sharedPreferences.edit {
            clear()
        }
    }

    fun onEmailChanged(email: String) {
        _uiState.update { it.copy(email = email, errorMessage = null) }
    }

    fun onPasswordChanged(password: String) {
        _uiState.update { it.copy(password = password, errorMessage = null) }
    }

    fun onUsernameChanged(username: String) {
        _uiState.update { it.copy(username = username, errorMessage = null) }
    }

    fun toggleRegisterMode() {
        _uiState.update { it.copy(isRegistering = !it.isRegistering, errorMessage = null) }
    }

    fun navigateToDestination(destination: UserDestination) {
        _uiState.update { it.copy(currentDestination = destination) }
    }

    fun startQuiz() {
        restartQuiz()
        navigateToDestination(UserDestination.QUIZ)
    }

    fun loginOrRegister() {
        val state = _uiState.value
        if (state.email.isBlank() || state.password.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Por favor, completa todos los campos") }
            return
        }

        if (state.isRegistering && state.username.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Por favor, introduce un nombre de usuario") }
            return
        }

        viewModelScope.launch {
            val user = repository.getUserByEmail(state.email.trim())
            if (state.isRegistering) {
                if (user != null) {
                    _uiState.update { it.copy(errorMessage = "El usuario ya existe") }
                } else {
                    val newUser = UserEntity(
                        email = state.email.trim(),
                        password = state.password,
                        username = state.username.trim(),
                        isAdmin = false
                    )
                    repository.insertUser(newUser)
                    saveSession(state.email.trim(), state.username.trim(), false)
                    _uiState.update {
                        it.copy(
                            isLoggedIn = true,
                            isAdmin = false,
                            username = state.username.trim(),
                            errorMessage = null,
                            currentDestination = UserDestination.PANEL
                        )
                    }
                }
            } else {
                if (user != null && user.password == state.password) {
                    val displayUsername = user.username.ifBlank { user.email.substringBefore("@") }
                    saveSession(user.email, displayUsername, user.isAdmin)
                    _uiState.update {
                        it.copy(
                            isLoggedIn = true,
                            isAdmin = user.isAdmin,
                            username = displayUsername,
                            errorMessage = null,
                            currentDestination = UserDestination.PANEL
                        )
                    }
                } else {
                    _uiState.update { it.copy(errorMessage = "Correo o contraseña incorrectos") }
                }
            }
        }
    }

    fun logout() {
        clearSession()
        _uiState.update {
            QuizUiState(questions = it.questions, users = it.users, currentDestination = UserDestination.PANEL)
        }
    }

    // Admin & User question creation functions
    fun onNewQuestionTextChanged(text: String) {
        _uiState.update { it.copy(newQuestionText = text, adminMessage = null) }
    }
    fun onNewOptionAChanged(text: String) { _uiState.update { it.copy(newOptionA = text) } }
    fun onNewOptionBChanged(text: String) { _uiState.update { it.copy(newOptionB = text) } }
    fun onNewOptionCChanged(text: String) { _uiState.update { it.copy(newOptionC = text) } }
    fun onNewOptionDChanged(text: String) { _uiState.update { it.copy(newOptionD = text) } }
    fun onNewCorrectAnswerChanged(answer: String) { _uiState.update { it.copy(newCorrectAnswer = answer) } }

    fun addQuestion() {
        val state = _uiState.value
        if (state.newQuestionText.isBlank() || state.newOptionA.isBlank() || state.newOptionB.isBlank() ||
            state.newOptionC.isBlank() || state.newOptionD.isBlank()) {
            _uiState.update { it.copy(adminMessage = "Por favor, rellena todos los campos de la pregunta y opciones.") }
            return
        }

        viewModelScope.launch {
            repository.insertQuestion(
                QuestionEntity(
                    questionText = state.newQuestionText.trim(),
                    optionA = state.newOptionA.trim(),
                    optionB = state.newOptionB.trim(),
                    optionC = state.newOptionC.trim(),
                    optionD = state.newOptionD.trim(),
                    correctAnswer = state.newCorrectAnswer
                )
            )
            _uiState.update {
                it.copy(
                    newQuestionText = "",
                    newOptionA = "",
                    newOptionB = "",
                    newOptionC = "",
                    newOptionD = "",
                    newCorrectAnswer = "A",
                    adminMessage = "¡Pregunta añadida con éxito!"
                )
            }
        }
    }

    fun deleteQuestion(id: Long) {
        viewModelScope.launch {
            repository.deleteQuestion(id)
        }
    }

    fun deleteUser(email: String) {
        viewModelScope.launch {
            repository.deleteUser(email)
        }
    }

    // Quiz gameplay functions
    fun selectAnswer(questionIndex: Int, option: String) {
        val currentAnswers = _uiState.value.selectedAnswers.toMutableMap()
        currentAnswers[questionIndex] = option
        _uiState.update { it.copy(selectedAnswers = currentAnswers) }
    }

    fun nextQuestion() {
        val state = _uiState.value
        if (state.currentQuestionIndex < state.questions.size - 1) {
            _uiState.update { it.copy(currentQuestionIndex = it.currentQuestionIndex + 1) }
        } else {
            // Calculate score
            var calculatedScore = 0
            state.questions.forEachIndexed { index, question ->
                val selected = state.selectedAnswers[index]
                if (selected == question.correctAnswer) {
                    calculatedScore++
                }
            }
            _uiState.update { it.copy(quizFinished = true, score = calculatedScore) }
        }
    }

    fun previousQuestion() {
        val state = _uiState.value
        if (state.currentQuestionIndex > 0) {
            _uiState.update { it.copy(currentQuestionIndex = it.currentQuestionIndex - 1) }
        }
    }

    fun restartQuiz() {
        _uiState.update {
            it.copy(
                currentQuestionIndex = 0,
                selectedAnswers = emptyMap(),
                quizFinished = false,
                score = 0
            )
        }
    }
}
