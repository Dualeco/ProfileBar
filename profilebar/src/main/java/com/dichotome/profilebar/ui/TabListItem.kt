package com.dichotome.profilebar.ui

open class TabListItem(
    val name: String,
    val thumbnailUrl: String
)

class FavListItem(
    title: String,
    val subtitle: String,
    thumbnailUrl: String) : TabListItem(title, thumbnailUrl)
