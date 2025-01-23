package com.daveleeds.arrowdemo

import kotlinx.serialization.Serializable

@Serializable
data class Wrestler(val id: Int, val name: String, val age: Int, val weight: Int, val hometown: Hometown)

@Serializable
data class Hometown(val city: String, val country: String)
