package com.dichotome.profilebar.stubs

import java.util.*

class FavListItem(
    title: String,
    val subtitle: String,
    thumbnailUrl: String,
    uuid: String = UUID.randomUUID().toString()
) : TabListItem(
    uuid,
    title,
    thumbnailUrl
)
