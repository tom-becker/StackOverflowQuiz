package com.tombecker.acculynxchallenge.model


import com.google.gson.annotations.SerializedName

data class AnswerResponse(
    @SerializedName("has_more")
    val hasMore: Boolean?,
    @SerializedName("items")
    val items: List<Answer>?,
    @SerializedName("quota_max")
    val quotaMax: Int?,
    @SerializedName("quota_remaining")
    val quotaRemaining: Int?
) {
    data class Answer(
        @SerializedName("answer_id")
        val answerId: Int?,
        @SerializedName("body_markdown")
        val body: String?,
        @SerializedName("community_owned_date")
        val communityOwnedDate: Int?,
        @SerializedName("creation_date")
        val creationDate: Int?,
        @SerializedName("is_accepted")
        val isAccepted: Boolean?,
        @SerializedName("last_activity_date")
        val lastActivityDate: Int?,
        @SerializedName("last_edit_date")
        val lastEditDate: Int?,
        @SerializedName("owner")
        val owner: Owner?,
        @SerializedName("question_id")
        val questionId: Int?,
        @SerializedName("score")
        val score: Int?
    ) {
        data class Owner(
            @SerializedName("accept_rate")
            val acceptRate: Int?,
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