package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.myapplication.ui.QuizViewModel
import com.example.myapplication.ui.admin.AdminScreen
import com.example.myapplication.ui.login.LoginScreen
import com.example.myapplication.ui.quiz.QuizScreen
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

    private val quizViewModel: QuizViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val uiState by quizViewModel.uiState.collectAsState()

                    if (!uiState.isLoggedIn) {
                        LoginScreen(
                            viewModel = quizViewModel,
                            modifier = Modifier.padding(innerPadding)
                        )
                    } else if (uiState.isAdmin) {
                        AdminScreen(
                            viewModel = quizViewModel,
                            modifier = Modifier.padding(innerPadding)
                        )
                    } else {
                        QuizScreen(
                            viewModel = quizViewModel,
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
            }
        }
    }
}
