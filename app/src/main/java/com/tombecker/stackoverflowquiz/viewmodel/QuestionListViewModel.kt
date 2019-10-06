package com.tombecker.stackoverflowquiz.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.tombecker.stackoverflowquiz.model.QuestionModel
import com.tombecker.stackoverflowquiz.model.QuestionResponse
import com.tombecker.stackoverflowquiz.model.local.QuestionDatabase
import com.tombecker.stackoverflowquiz.model.remote.StackOverflowApiService
import com.tombecker.stackoverflowquiz.util.AppConstants.Companion.LIST_CACHE_TIME_IN_NANO
import com.tombecker.stackoverflowquiz.util.AppConstants.Companion.MIN_ANSWER_NUMBER
import com.tombecker.stackoverflowquiz.util.SharedPrefsHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class QuestionListViewModel(application: Application) : BaseViewModel(application) {

    val questionListLiveData = MutableLiveData<List<QuestionModel>>()
    val loadErrorLiveData = MutableLiveData<Boolean>()
    val questionsAreLoading = MutableLiveData<Boolean>()

    private var apiService = StackOverflowApiService()
    private val disposable = CompositeDisposable()
    private var sharedPrefsHelper = SharedPrefsHelper(getApplication())

    fun refresh(ignoreCache: Boolean = false) {
        questionsAreLoading.value = true

        if(!ignoreCache) {
            val cachedTimestamp = sharedPrefsHelper.getCachedTime()
            cachedTimestamp?.let {
                if(it != 0L && System.nanoTime() - it < LIST_CACHE_TIME_IN_NANO) {
                    fetchFromDatabase()
                    return
                }
            }
        }

        fetchFromRemote()
    }

    private fun fetchFromDatabase() {
        disposable.add(QuestionDatabase.getInstance(getApplication())
            .questionDao().getAllQuestions()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ list ->
                questionListLiveData.value = list
                loadErrorLiveData.value = false
                questionsAreLoading.value = false
            }, { e ->
                loadErrorLiveData.value = true
                questionsAreLoading.value = false
                e.printStackTrace()
            })
        )
    }

    private fun fetchFromRemote() {
        disposable.add(
            apiService.getQuestions()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    clearDbAndSave(getQuestionsFromResponse(response))
                    loadErrorLiveData.value = false
                    questionsAreLoading.value = false
                }, { e ->
                    loadErrorLiveData.value = true
                    questionsAreLoading.value = false
                    e.printStackTrace()
                })
        )
    }

    private fun clearDbAndSave(tempQuestionList: List<QuestionModel>) {
        disposable.add(
            QuestionDatabase.getInstance(getApplication())
                .questionDao().deleteAllQuestions()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    saveQuestionsToDb(tempQuestionList)
                }, { e ->
                    e.printStackTrace()
                })
        )
    }

    private fun saveQuestionsToDb(questionList: List<QuestionModel>) {
        disposable.add(
            QuestionDatabase.getInstance(getApplication())
                .questionDao().insertQuestions(*questionList.toTypedArray())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ listIds ->
                    updateQuestionListLiveData(questionList, listIds)
                }, { e ->
                    e.printStackTrace()
                })
        )
    }

    private fun updateQuestionListLiveData(questionList: List<QuestionModel>, listIds: List<Long>) {
        var index = 0
        while (index < listIds.size) {
            questionList[index].uuid = listIds[index].toInt()
            index++
        }

        questionListLiveData.value = questionList
        sharedPrefsHelper.setCachedTime(System.nanoTime())
    }

    private fun getQuestionsFromResponse(response: QuestionResponse): List<QuestionModel> {
        val newList = arrayListOf<QuestionModel>()
        response.items?.let {
            for (question in response.items) {
                if(isValidQuestion(question)) {
                    newList.add(QuestionModel(
                        acceptedAnswerId = question.acceptedAnswerId,
                        answerCount = question.answerCount,
                        ownerName = question.owner?.displayName,
                        body = question.body,
                        questionId = question.questionId,
                        title = question.title,
                        creationDate = question.creationDate,
                        isAnswered = question.isAnswered
                    ))
                }
            }
        }
        return newList
    }

    //filter for only answered questions with more than one answer
    private fun isValidQuestion(question: QuestionResponse.Question): Boolean {
        return question.answerCount ?: 0 >= MIN_ANSWER_NUMBER &&
                question.acceptedAnswerId ?: -1 > 0
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

}
