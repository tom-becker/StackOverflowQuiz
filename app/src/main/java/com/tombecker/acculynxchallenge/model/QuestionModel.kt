package com.tombecker.acculynxchallenge.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class QuestionModel (
    @ColumnInfo(name = "accepted_answer_id")
    val acceptedAnswerId: Int?,
    @ColumnInfo(name = "answer_count")
    val answerCount: Int?,
    @ColumnInfo(name = "body")
    val body: String?,
    @ColumnInfo(name = "creation_date")
    val creationDate: Int?,
    @ColumnInfo(name = "is_answered")
    val isAnswered: Boolean?,
    @ColumnInfo(name = "owner_name")
    val ownerName: String?,
    @ColumnInfo(name = "question_id")
    val questionId: Int?,
    @ColumnInfo(name = "title")
    val title: String?
) {
    @PrimaryKey(autoGenerate = true)
    var uuid: Int = 0
}