package com.example.myapplication.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [UserEntity::class, QuestionEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "quiz_database"
                )
                    .fallbackToDestructiveMigration(true)
                    .addCallback(DatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        populateDatabase(database.appDao())
                    }
                }
            }

            suspend fun populateDatabase(dao: AppDao) {
                // Insert default admin user: admin@admin.es / admin0090
                dao.insertUser(
                    UserEntity(
                        email = "admin@admin.es",
                        password = "admin0090",
                        isAdmin = true
                    )
                )

                // Add sample question
                dao.insertQuestion(
                    QuestionEntity(
                        questionText = "¿Cuál es el lenguaje principal para desarrollar apps Android nativas?",
                        optionA = "Python",
                        optionB = "Kotlin",
                        optionC = "C++",
                        optionD = "Ruby",
                        correctAnswer = "B"
                    )
                )
            }
        }
    }
}
