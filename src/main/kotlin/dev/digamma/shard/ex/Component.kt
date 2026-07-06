package dev.digamma.shard.ex

import java.awt.Component

val Component.ancestors
    get() = generateSequence(parent) { it.parent }

val Component.hierarchy
    get() = generateSequence(this) { it.parent }
