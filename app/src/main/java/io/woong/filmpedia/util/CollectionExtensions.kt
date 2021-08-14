package io.woong.filmpedia.util

fun <E> Collection<E>?.isNotNullOrEmpty(): Boolean = !this.isNullOrEmpty()
