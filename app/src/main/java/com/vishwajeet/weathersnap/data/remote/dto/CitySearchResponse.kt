package com.vishwajeet.weathersnap.data.remote.dto

data class CitySearchResponse(
    val results : List<CityDto>?
)

data class CityDto(
    val id: Long,
    val name: String,
    val country: String,
    val admin1: String?,
    val latitude: Double,
    val longitude: Double
)