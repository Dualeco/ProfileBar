package com.dichotome.profilebar.stubs

import java.util.*

open class TabListItem(
    val name: String,
    val thumbnailUrl: String,
    val uuid: String = UUID.randomUUID().toString()
)