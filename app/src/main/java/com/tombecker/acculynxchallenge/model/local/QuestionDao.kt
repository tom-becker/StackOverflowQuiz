package com.tombecker.acculynxchallenge.model.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.tombecker.acculynxchallenge.model.QuestionModel
import io.reactivex.Single

@Dao
interface QuestionDao {

    @Insert
    fun insertQuestions(vararg question: QuestionModel): Single<List<Long>>

    @Query("SELECT * FROM questionmodel")
    fun getAllQuestions(): Single<List<QuestionModel>>

    @Query("SELECT * FROM questionmodel WHERE uuid == :id")
    fun getQuestions(id: Int): Single<QuestionModel>

    @Query("DELETE FROM questionmodel")
    fun deleteAllQuestions(): Single<Int>

}