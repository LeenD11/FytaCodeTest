package com.leen.fytacodetest.networking.resultdataclasses

data class Query(
    val images: List<String>,
    val includeRelatedImages: Boolean,
    val organs: List<String>,
    val project: String
)