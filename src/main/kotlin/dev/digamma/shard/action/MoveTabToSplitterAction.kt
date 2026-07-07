package dev.digamma.shard.action

import com.intellij.openapi.actionSystem.AnActionEvent
import dev.digamma.shard.ShardSettings
import dev.digamma.shard.ex.getNeighbor
import dev.digamma.shard.ex.moveComposite
import dev.digamma.shard.ex.splitComposite
import dev.digamma.shard.util.Side

class MoveTabToSplitterAction(private val side: Side, template: Template) : ShardAction(template) {
    override fun doUpdate(event: AnActionEvent) =
        event.editorWindow.let {
            when {
                it?.getSelectedComposite(false) == null -> State.HIDDEN
                it.getNeighbor(side) != null || (ShardSettings.getState().splitOnMove && it.tabCount > 1) -> State.ENABLED
                else -> State.DISABLED
            }
        }

    override fun doPerform(event: AnActionEvent) {
        event.editorWindow?.run {
            val composite = getSelectedComposite(false) ?: return
            val target = getNeighbor(side)

            if (target != null) moveComposite(composite, target)
            else if (tabCount > 1) splitComposite(composite, side, true)
        }
    }

    @Suppress("CompanionObjectInExtension")
    companion object {
        val LEFT = MoveTabToSplitterAction(Side.LEFT, Template("action.move.tab.to.splitter.left.text"))
        val TOP = MoveTabToSplitterAction(Side.TOP, Template("action.move.tab.to.splitter.top.text"))
        val RIGHT = MoveTabToSplitterAction(Side.RIGHT, Template("action.move.tab.to.splitter.right.text"))
        val BOTTOM = MoveTabToSplitterAction(Side.BOTTOM, Template("action.move.tab.to.splitter.bottom.text"))
    }
}
