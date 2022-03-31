package com.leen.fytacodetest.networking.resultdataclasses

data class Image(
    val author: String,
    val citation: String,
    val date: Date,
    val license: String,
    val organ: String,
    val url: Url
)