package dev.digamma.shard.action

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.options.advanced.AdvancedSettings
import dev.digamma.shard.ex.allowsReordering
import dev.digamma.shard.ex.visibleInfos
import java.util.Collections.rotate

abstract class MoveTabAction(private val direction: Direction, template: Template) : ShardAction(template) {
    enum class Direction { BACKWARD, FORWARD, START, END }

    private val groupPinnedTabs
        get() = AdvancedSettings.getBoolean("editor.keep.pinned.tabs.on.left")

    override fun doUpdate(event: AnActionEvent): State {
        val tabs = event.editorTabs?.takeIf { it.allowsReordering } ?: return State.HIDDEN
        val source = tabs.targetInfo!!
        val target = tabs.visibleInfos.run {
            // Ensure that there is a tab after the current one in the direction of movement
            when (direction) {
                Direction.BACKWARD, Direction.START -> indexOf(source).dec().takeUnless { it < 0 }
                Direction.FORWARD, Direction.END -> indexOf(source).inc().takeUnless { it > lastIndex }
            }?.let(::get)
        }

        // Ensure that the tab after the current one belongs to the same group
        return if (target != null && (!groupPinnedTabs || target.isPinned == source.isPinned)) State.ENABLED
        else State.DISABLED
    }

    override fun doPerform(event: AnActionEvent) {
        val tabs = event.editorTabs ?: return
        val source = tabs.targetInfo!!
        val order = tabs.visibleInfos.toMutableList()

        with(order) {
            val index = indexOf(source)

            when (direction) {
                // Swap the current tab with the one to its left
                Direction.BACKWARD -> set(index, set(index - 1, source))

                // Swap the current tab with the one to its right
                Direction.FORWARD -> set(index, set(index + 1, source))

                // Rotate the current group of tabs to the left
                Direction.START -> rotate(
                    subList(if (groupPinnedTabs && !source.isPinned) indexOfFirst { !it.isPinned } else 0, index + 1),
                    1
                )

                // Rotate the current group of tabs to the right
                Direction.END -> rotate(
                    subList(index, if (groupPinnedTabs && source.isPinned) indexOfLast { it.isPinned } + 1 else size),
                    -1
                )
            }
        }

        tabs.sortTabs(compareBy { order.indexOf(it) })
    }

    class Backward : MoveTabAction(Direction.BACKWARD, Template("action.move.tab.backward.text"))
    class Forward : MoveTabAction(Direction.FORWARD, Template("action.move.tab.forward.text"))
    class Start : MoveTabAction(Direction.START, Template("action.move.tab.start.text"))
    class End : MoveTabAction(Direction.END, Template("action.move.tab.end.text"))
}
