package com.nasahapps.urbanary.model

import com.google.gson.annotations.SerializedName

data class Definition(
    val definition: String? = null,
    val word: String? = null,
    @SerializedName("thumbs_up") val thumbsUp: Int = 0,
    @SerializedName("thumbs_down") val thumbsDown: Int = 0
)