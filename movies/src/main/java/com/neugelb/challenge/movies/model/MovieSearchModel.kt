package com.neugelb.challenge.movies.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


// https://image.tmdb.org/t/p/w300/rXsh4MI6uyVgZBSSzXCfitJnVPy.jpg
//{
//        poster_path: "/rXsh4MI6uyVgZBSSzXCfitJnVPy.jpg",
//        id: 530385,
//        adult: false,
//        title: "Midsommar",
//        vote_average: 7.2,
//        release_date: "2019-07-03"
//},

@Parcelize
data class MovieSearchModel(
        @SerializedName("poster_path") val poster_path: String?,
        @SerializedName("id") val id: Int,
        @SerializedName("adult") val adult: Boolean?,
        @SerializedName("title") val title: String?,
        @SerializedName("vote_average") val vote_average: Double?,
        @SerializedName("release_date") val release_date: String?
) : Parcelable
