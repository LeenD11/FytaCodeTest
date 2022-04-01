package com.leen.fytacodetest.networking.resultdataclasses

import java.io.Serializable

data class ApiResponse(
    val bestMatch: String? = null,
    val language: String? = null,
    val preferedReferential: String? = null,
    val query: Query? = null,
    val remainingIdentificationRequests: Int? = null,
    val results: List<Result>? = null,
    val switchToProject: String? = null,
    val version: String? = null
) : Serializable
