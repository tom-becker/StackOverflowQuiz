package com.tombecker.stackoverflowquiz.model.remote

import com.tombecker.stackoverflowquiz.model.AnswerResponse
import com.tombecker.stackoverflowquiz.model.QuestionResponse
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class StackOverflowApiService {

    val BASEURL = "https://api.stackexchange.com"

    private val api = Retrofit.Builder()
        .baseUrl(BASEURL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(StackOverflowApi::class.java)

    fun getQuestions(): Single<QuestionResponse> {
        return api.getQuestions()
    }

    fun getQuestion(id: Int): Single<QuestionResponse> {
        return api.getQuestion(id)
    }

    fun getAnswers(id: Int): Single<AnswerResponse> {
        return api.getAnswers(id)
    }
}