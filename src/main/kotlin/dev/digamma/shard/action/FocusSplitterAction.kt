package dev.digamma.shard.action

import com.intellij.openapi.actionSystem.AnActionEvent
import dev.digamma.shard.ex.getNeighbor
import dev.digamma.shard.util.Side

abstract class FocusSplitterAction(private val side: Side, template: Template) : ShardAction(template) {
    override fun doUpdate(event: AnActionEvent) =
        event.editorWindow.let {
            when {
                it == null -> State.HIDDEN
                it.getNeighbor(side) != null -> State.ENABLED
                else -> State.DISABLED
            }
        }

    override fun doPerform(event: AnActionEvent) {
        event.editorWindow?.getNeighbor(side)?.requestFocus(true)
    }

    class Left : FocusSplitterAction(Side.LEFT, Template("action.focus.splitter.left.text"))
    class Top : FocusSplitterAction(Side.TOP, Template("action.focus.splitter.top.text"))
    class Right : FocusSplitterAction(Side.RIGHT, Template("action.focus.splitter.right.text"))
    class Bottom : FocusSplitterAction(Side.BOTTOM, Template("action.focus.splitter.bottom.text"))
}
