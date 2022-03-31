package com.leen.fytacodetest.networking.resultdataclasses

data class Result(
    val gbif: Gbif,
    val images: List<Image>,
    val score: Double,
    val species: Species
)