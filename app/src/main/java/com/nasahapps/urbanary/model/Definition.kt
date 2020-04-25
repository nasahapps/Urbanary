package com.nasahapps.urbanary.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Definition(
        val definition: String? = null,
        val word: String? = null,
        @SerializedName("thumbs_up") val thumbsUp: Int = 0,
        @SerializedName("thumbs_down") val thumbsDown: Int = 0
) : Parcelable