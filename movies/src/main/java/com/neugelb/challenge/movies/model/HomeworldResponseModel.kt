package com.neugelb.challenge.movies.model

import com.google.gson.annotations.SerializedName

data class HomeworldResponseModel(
        @SerializedName("name") val name: String? = "",
        @SerializedName("population") val population: String? = "")
