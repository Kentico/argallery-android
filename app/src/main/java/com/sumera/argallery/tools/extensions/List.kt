package com.sumera.argallery.tools.extensions

fun <E> List<E>.isInBounds(index: Int): Boolean {
    return index in 0..(size - 1)
}