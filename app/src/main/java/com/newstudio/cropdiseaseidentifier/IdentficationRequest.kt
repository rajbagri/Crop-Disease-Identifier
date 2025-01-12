package com.newstudio.cropdiseaseidentifier

data class IdentificationRequest(
    val images: List<String>,
    val latitude: Double,
    val longitude: Double,
    val similar_images: Boolean
)

