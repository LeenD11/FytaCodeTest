package com.leen.fytacodetest.networking.resultdataclasses

import java.io.Serializable

data class Species(
    val commonNames: List<String>,
    val family: Family,
    val genus: Genus,
    val scientificName: String,
    val scientificNameAuthorship: String,
    val scientificNameWithoutAuthor: String
): Serializable