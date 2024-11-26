package com.example.seton.common.domain.util

import com.example.seton.common.presentation.state.ContainerColor
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun ContainerColor.serialize() : String {
    return Json.encodeToString(this)
}

fun String.deserialize() : ContainerColor {
    return Json.decodeFromString<ContainerColor>(this)
}