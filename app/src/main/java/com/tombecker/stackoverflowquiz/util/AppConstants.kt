package com.tombecker.stackoverflowquiz.util

class AppConstants {

    companion object {
        const val LIST_CACHE_TIME_IN_NANO = 5 * 60000000000L //5 min in nano
        const val LIST_CACHE_TIME_KEY = "pref_time"
        const val MIN_ANSWER_NUMBER = 2
        const val QUESTION_DATE_FORMAT = "dd/MM/yyyy hh:mm:ss.SSS"
    }

}