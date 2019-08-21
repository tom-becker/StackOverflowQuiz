package com.tombecker.acculynxchallenge.model.remote

import com.tombecker.acculynxchallenge.model.AnswerResponse
import com.tombecker.acculynxchallenge.model.QuestionResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path


interface StackOverflowApi {

    @GET("/2.2/questions?site=stackoverflow&tagged=android&filter=!9Z(-wwK4f")
    fun getQuestions(): Single<QuestionResponse>

    @GET("/2.2/questions/{id}/?site=stackoverflow&tagged=android&filter=!9Z(-wwK4f")
    fun getQuestion(@Path("id") id: Int): Single<QuestionResponse>

    @GET("/2.2/questions/{id}/answers?site=stackoverflow&filter=!9Z(-wzftf")
    fun getAnswers(@Path("id") id: Int): Single<AnswerResponse>

}