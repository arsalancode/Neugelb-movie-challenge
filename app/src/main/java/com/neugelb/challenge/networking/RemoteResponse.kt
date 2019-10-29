package com.neugelb.challenge.networking

import com.google.gson.annotations.SerializedName

/**
 * Envelope class to support pagination
* */

data class RemoteResponse<T>(
        @SerializedName("page") val page: Int,
        @SerializedName("total_results") val total_results: Int,
        @SerializedName("total_pages") val total_pages: Int,
        @SerializedName("results") val results: T?
)