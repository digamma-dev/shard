package dev.digamma.shard.action

import com.intellij.openapi.actionSystem.AnActionEvent
import dev.digamma.shard.ex.component
import dev.digamma.shard.ex.getNeighbor
import dev.digamma.shard.ex.replaceComponent
import dev.digamma.shard.ex.splitter
import dev.digamma.shard.util.Side

class MoveSplitterAction(private val side: Side, template: Template) : ShardAction(template) {
    override fun doUpdate(event: AnActionEvent) =
        event.editorWindow.let {
            when {
                it == null -> State.HIDDEN
                it.getNeighbor(side) != null -> State.ENABLED
                else -> State.DISABLED
            }
        }

    override fun doPerform(event: AnActionEvent) {
        val source = event.editorWindow ?: return
        val target = source.getNeighbor(side) ?: return

        val sourceSplitter = source.splitter ?: return
        val targetSplitter = target.splitter ?: return

        if (sourceSplitter === targetSplitter) sourceSplitter.swapComponents()
        else {
            targetSplitter.replaceComponent(target.component, source.component)
            sourceSplitter.replaceComponent(source.component, target.component)
        }

        source.requestFocus(true)
    }

    @Suppress("CompanionObjectInExtension")
    companion object {
        val LEFT = MoveSplitterAction(Side.LEFT, Template("action.move.splitter.left.text"))
        val TOP = MoveSplitterAction(Side.TOP, Template("action.move.splitter.top.text"))
        val RIGHT = MoveSplitterAction(Side.RIGHT, Template("action.move.splitter.right.text"))
        val BOTTOM = MoveSplitterAction(Side.BOTTOM, Template("action.move.splitter.bottom.text"))
    }
}
