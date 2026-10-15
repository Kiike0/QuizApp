package com.example.myapplication.data

import com.example.myapplication.data.local.AppDao
import com.example.myapplication.data.local.QuestionEntity
import com.example.myapplication.data.local.UserEntity
import kotlinx.coroutines.flow.Flow

class QuizRepository(private val dao: AppDao) {
    val allQuestions: Flow<List<QuestionEntity>> = dao.getAllQuestions()
    val allUsers: Flow<List<UserEntity>> = dao.getAllUsers()

    suspend fun getUserByEmail(email: String): UserEntity? {
        return dao.getUserByEmail(email)
    }

    suspend fun insertUser(user: UserEntity) {
        dao.insertUser(user)
    }

    suspend fun deleteUser(email: String) {
        dao.deleteUser(email)
    }

    suspend fun insertQuestion(question: QuestionEntity) {
        dao.insertQuestion(question)
    }

    suspend fun deleteQuestion(id: Long) {
        dao.deleteQuestion(id)
    }
}
