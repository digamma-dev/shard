package dev.digamma.shard.ex

import com.intellij.openapi.ui.Splitter
import javax.swing.JComponent

val Splitter.children
    get() = sequence {
        firstComponent?.let { yield(it) }
        secondComponent?.let { yield(it) }
    }

fun Splitter.replaceComponent(oldComponent: JComponent, newComponent: JComponent) {
    if (firstComponent === oldComponent) this.firstComponent = newComponent
    else if (secondComponent === oldComponent) this.secondComponent = newComponent
}
