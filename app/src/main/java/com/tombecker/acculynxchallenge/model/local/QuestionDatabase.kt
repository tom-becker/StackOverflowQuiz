package com.tombecker.acculynxchallenge.model.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tombecker.acculynxchallenge.model.QuestionModel

@Database(entities = [QuestionModel::class], version = 1)
abstract class QuestionDatabase : RoomDatabase() {

    abstract fun questionDao(): QuestionDao

    companion object {
        @Volatile
        private var instance: QuestionDatabase? = null

        private val LOCK = Any()

        fun getInstance(context: Context) = instance?: synchronized(LOCK) {
            instance ?: buildDb(context).also {
                instance = it
            }
        }

        private fun buildDb(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            QuestionDatabase::class.java,
            "questionDatabase"
        ).build()
    }

}