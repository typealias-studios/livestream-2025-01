package com.daveleeds.arrowdemo

import arrow.optics.optics
import kotlinx.serialization.Serializable

@Serializable
data class WrestlerIds(val ids: List<Int>)

@Serializable
@optics data class Wrestler(val id: Int, val name: String, val age: Int, val weight: Int, val hometown: Hometown) {
    companion object
}

@Serializable
@optics data class Hometown(val city: String, val country: String) {
    companion object
}
