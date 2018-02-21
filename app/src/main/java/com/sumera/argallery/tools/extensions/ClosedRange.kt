package com.sumera.argallery.tools.extensions

import java.util.*

fun ClosedRange<Int>.random() = Random().nextInt(endInclusive - start) +  start