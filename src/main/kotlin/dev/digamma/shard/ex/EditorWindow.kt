package dev.digamma.shard.ex

import com.intellij.openapi.fileEditor.impl.EditorWindow
import com.intellij.openapi.fileEditor.impl.EditorWindowHolder
import com.intellij.openapi.ui.Splitter
import dev.digamma.shard.util.Side
import java.awt.Component
import java.awt.Point

val EditorWindow.component
    get() = tabbedPane.component

val EditorWindow.splitter
    get() = component.parent as? Splitter

fun EditorWindow.getNeighbor(side: Side) =
    getNearestNeighbor(side)

fun EditorWindow.getNearestNeighbor(side: Side): EditorWindow? {
    // Shift the target location by 25% to compensate for slight visual offsets
    val location = when (side) {
        Side.LEFT -> Point(-2, component.height / 4)
        Side.TOP -> Point(component.width / 4, -2)
        Side.RIGHT -> Point(component.width + 2, component.height / 4)
        Side.BOTTOM -> Point(component.width / 4, component.height + 2)
    }

    // Find the first ancestor that contains the target location
    var target: Component? = component.hierarchy
        .takeWhile { it.parent !== owner }
        .firstNotNullOfOrNull { source ->
            location += source.location
            source.parent.takeIf { it.contains(location) }
        }

    // Traverse the splitter hierarchy at the target location down to the first editor window
    while (target is Splitter) {
        target = target.getComponentAt(location)
            .also { location -= it.location }
    }

    return (target as? EditorWindowHolder)?.editorWindow
}
