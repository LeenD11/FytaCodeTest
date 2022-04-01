package com.leen.fytacodetest.networking.resultdataclasses

import java.io.Serializable

data class Family(
    val scientificName: String,
    val scientificNameAuthorship: String,
    val scientificNameWithoutAuthor: String
): Serializable