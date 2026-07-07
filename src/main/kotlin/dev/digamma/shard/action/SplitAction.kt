package dev.digamma.shard.action

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.fileEditor.impl.FileEditorManagerImpl
import dev.digamma.shard.ex.splitComposite
import dev.digamma.shard.util.Side

class SplitAction(private val side: Side, private val move: Boolean, template: Template) : ShardAction(template) {
    override fun doUpdate(event: AnActionEvent) =
        event.editorWindow.let {
            val composite = it?.getSelectedComposite(false)
            when {
                composite == null -> State.HIDDEN
                move && it.tabCount > 1 -> State.ENABLED
                !move && !FileEditorManagerImpl.forbidSplitFor(composite.file) -> State.ENABLED
                else -> State.DISABLED
            }
        }

    override fun doPerform(event: AnActionEvent) {
        event.editorWindow?.run {
            splitComposite(getSelectedComposite(false) ?: return, side, move)
        }
    }

    @Suppress("CompanionObjectInExtension")
    companion object {
        val LEFT = SplitAction(Side.LEFT, false, Template("action.split.left.text"))
        val TOP = SplitAction(Side.TOP, false, Template("action.split.top.text"))
        val RIGHT = SplitAction(Side.RIGHT, false, Template("action.split.right.text"))
        val BOTTOM = SplitAction(Side.BOTTOM, false, Template("action.split.bottom.text"))

        val MOVE_LEFT = SplitAction(Side.LEFT, true, Template("action.split.and.move.left.text"))
        val MOVE_TOP = SplitAction(Side.TOP, true, Template("action.split.and.move.top.text"))
        val MOVE_RIGHT = SplitAction(Side.RIGHT, true, Template("action.split.and.move.right.text"))
        val MOVE_BOTTOM = SplitAction(Side.BOTTOM, true, Template("action.split.and.move.bottom.text"))
    }
}
