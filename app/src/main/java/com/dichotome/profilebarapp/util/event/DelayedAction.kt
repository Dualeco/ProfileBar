package com.dichotome.profilebarapp.util.event


class DelayedAction(val doAction: () -> Unit, val delay: Long)