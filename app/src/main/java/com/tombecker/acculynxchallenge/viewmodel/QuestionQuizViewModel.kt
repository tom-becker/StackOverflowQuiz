package com.tombecker.acculynxchallenge.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.tombecker.acculynxchallenge.model.AnswerModel
import com.tombecker.acculynxchallenge.model.AnswerResponse
import com.tombecker.acculynxchallenge.model.QuestionModel
import com.tombecker.acculynxchallenge.model.QuestionResponse
import com.tombecker.acculynxchallenge.model.remote.StackOverflowApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class QuestionQuizViewModel(application: Application) : BaseViewModel(application) {

    private var apiService = StackOverflowApiService()
    private val disposable = CompositeDisposable()

    val selectedQuestion = MutableLiveData<QuestionModel>()
    val answerList = MutableLiveData<List<AnswerModel>>()
    val loadingQuestionError = MutableLiveData<Boolean>()
    val loadingAnswersError = MutableLiveData<Boolean>()
    val answersAreLoading = MutableLiveData<Boolean>()

    fun fetchQuestion(questionId: Int?) {
        answersAreLoading.value = true

        if(questionId != null) {
            fetchFromRemote(questionId)
        } else {
            loadingQuestionError.value = true
            loadingAnswersError.value = true
            answersAreLoading.value = false
        }
    }

    fun isAnswerCorrect(position: Int): Boolean {
        return selectedQuestion.value?.acceptedAnswerId ==
                answerList.value?.get(position)?.answerId
    }

    private fun fetchFromRemote(questionId: Int) {
        disposable.addAll(
            apiService.getQuestion(questionId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    processQuestionResponse(response)
                    loadingQuestionError.value = false
                }, { e ->
                    e.printStackTrace()
                    loadingQuestionError.value = true
                }),
            apiService.getAnswers(questionId)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    answerList.value = processAnswerResponse(response)
                    loadingAnswersError.value = false
                    answersAreLoading.value = false
                }, { e ->
                    e.printStackTrace()
                    loadingAnswersError.value = true
                    answersAreLoading.value = false
                })
        )
    }

    private fun processQuestionResponse(response: QuestionResponse) {
        val question = response.items?.get(0)
        question?.let {
            selectedQuestion.value = QuestionModel(
                acceptedAnswerId = it.acceptedAnswerId,
                answerCount = it.answerCount,
                ownerName = it.owner?.displayName,
                body = it.body,
                questionId = it.questionId,
                title = it.title,
                creationDate = it.creationDate,
                isAnswered = it.isAnswered
            )
        }
    }

    private fun processAnswerResponse(response: AnswerResponse): List<AnswerModel> {
        val newList = arrayListOf<AnswerModel>()

        response.items?.let {
            for (answer in response.items) {
                newList.add(AnswerModel(
                    answerId = answer.answerId,
                    body = answer.body,
                    ownerName = answer.owner?.displayName,
                    questionId = answer.questionId
                ))
            }
        }

        return newList
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

}
