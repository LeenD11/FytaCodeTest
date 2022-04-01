package com.leen.fytacodetest.networking.resultdataclasses

import java.io.Serializable

data class Result(
    val gbif: Gbif,
    val images: List<Image>,
    val score: Double,
    val species: Species
) : Serializable