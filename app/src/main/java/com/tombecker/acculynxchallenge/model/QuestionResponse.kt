package com.tombecker.acculynxchallenge.model


import com.google.gson.annotations.SerializedName

data class QuestionResponse(
    @SerializedName("items")
    val items: List<Question>?,
    @SerializedName("has_more")
    val hasMore: Boolean?,
    @SerializedName("quota_max")
    val quotaMax: Int?,
    @SerializedName("quota_remaining")
    val quotaRemaining: Int?
) {
    data class Question(
        @SerializedName("accepted_answer_id")
        val acceptedAnswerId: Int?,
        @SerializedName("answer_count")
        val answerCount: Int?,
        @SerializedName("body_markdown")
        val body: String?,
        @SerializedName("creation_date")
        val creationDate: Int?,
        @SerializedName("is_answered")
        val isAnswered: Boolean?,
        @SerializedName("last_activity_date")
        val lastActivityDate: Int?,
        @SerializedName("last_edit_date")
        val lastEditDate: Int?,
        @SerializedName("link")
        val link: String?,
        @SerializedName("owner")
        val owner: Owner?,
        @SerializedName("question_id")
        val questionId: Int?,
        @SerializedName("score")
        val score: Int?,
        @SerializedName("tags")
        val tags: List<String>?,
        @SerializedName("title")
        val title: String?,
        @SerializedName("view_count")
        val viewCount: Int?
    ) {
        data class Owner(
            @SerializedName("display_name")
            val displayName: String?,
            @SerializedName("link")
            val link: String?,
            @SerializedName("profile_image")
            val profileImage: String?,
            @SerializedName("reputation")
            val reputation: Int?,
            @SerializedName("user_id")
            val userId: Int?,
            @SerializedName("user_type")
            val userType: String?
        )
    }
}