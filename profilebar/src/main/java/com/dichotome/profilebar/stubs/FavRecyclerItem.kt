package com.dichotome.profilebar.stubs

import java.util.*

class FavRecyclerItem(
    title: String,
    val subtitle: String,
    thumbnailUrl: String,
    uuid: String = UUID.randomUUID().toString()
) : TabRecyclerItem(
    title,
    thumbnailUrl,
    uuid
)
