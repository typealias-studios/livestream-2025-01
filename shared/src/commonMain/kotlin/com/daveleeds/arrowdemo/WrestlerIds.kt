package com.daveleeds.arrowdemo

import kotlinx.serialization.*

@Serializable
data class WrestlerIds(
    val ids: List<Int>
)
