package com.m7md7sn.dentary.utils

import java.util.concurrent.atomic.AtomicBoolean

open class Event<out T>(private val content: T) {
    var hasBeenHandled = AtomicBoolean(false)
        private set // Allow external read but not write

    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled.compareAndSet(false, true)) {
            content
        } else {
            null
        }
    }

    fun peekContent(): T = content
}