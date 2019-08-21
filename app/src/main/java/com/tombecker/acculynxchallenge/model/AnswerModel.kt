package com.tombecker.acculynxchallenge.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

//@Entity
data class AnswerModel (
//    @ColumnInfo(name = "answer_id")
    val answerId: Int?,
//    @ColumnInfo(name = "body")
    val body: String?,
//    @ColumnInfo(name = "owner_name")
    val ownerName: String?,
//    @ColumnInfo(name = "question_id")
    val questionId: Int?
)
//{
//    @PrimaryKey(autoGenerate = true)
//    var uuid: Int = 0
//}