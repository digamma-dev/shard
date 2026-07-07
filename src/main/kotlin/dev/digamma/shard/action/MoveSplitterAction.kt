package dev.digamma.shard.action

import com.intellij.openapi.actionSystem.AnActionEvent
import dev.digamma.shard.ex.component
import dev.digamma.shard.ex.getNeighbor
import dev.digamma.shard.ex.replaceComponent
import dev.digamma.shard.ex.splitter
import dev.digamma.shard.util.Side

abstract class MoveSplitterAction(private val side: Side, template: Template) : ShardAction(template) {
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

    class Left : MoveSplitterAction(Side.LEFT, Template("action.move.splitter.left.text"))
    class Top : MoveSplitterAction(Side.TOP, Template("action.move.splitter.top.text"))
    class Right : MoveSplitterAction(Side.RIGHT, Template("action.move.splitter.right.text"))
    class Bottom : MoveSplitterAction(Side.BOTTOM, Template("action.move.splitter.bottom.text"))
}
