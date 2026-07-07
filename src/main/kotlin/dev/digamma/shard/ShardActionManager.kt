package dev.digamma.shard

import com.intellij.ide.AppLifecycleListener
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.extensions.PluginId
import dev.digamma.shard.action.*
import org.intellij.lang.annotations.Language

object ShardActionManager {
    private fun registerAction(id: String, action: ShardAction) {
        @Suppress("UnresolvedPluginConfigReference")
        ActionManager.getInstance().registerAction("Shard.$id", action, PluginId.getId("dev.digamma.shard"))
    }

    private fun replaceAction(@Language("devkit-action-id") id: String, action: ShardAction) {
        ActionManager.getInstance().replaceAction(id, action)
    }

    object StartupActivity : AppLifecycleListener {
        override fun appFrameCreated(commandLineArgs: List<String?>) {
            registerAction("FocusSplitter.Left", FocusSplitterAction.Left())
            registerAction("FocusSplitter.Top", FocusSplitterAction.Top())
            registerAction("FocusSplitter.Right", FocusSplitterAction.Right())
            registerAction("FocusSplitter.Bottom", FocusSplitterAction.Bottom())

            registerAction("MoveSplitter.Left", MoveSplitterAction.Left())
            registerAction("MoveSplitter.Top", MoveSplitterAction.Top())
            registerAction("MoveSplitter.Right", MoveSplitterAction.Right())
            registerAction("MoveSplitter.Bottom", MoveSplitterAction.Bottom())

            registerAction("Split.Left", SplitAction.Left())
            registerAction("Split.Top", SplitAction.Top())
            replaceAction("SplitVertically", SplitAction.Right())
            replaceAction("SplitHorizontally", SplitAction.Bottom())

            registerAction("SplitAndMove.Left", SplitAction.MoveLeft())
            registerAction("SplitAndMove.Top", SplitAction.MoveTop())
            replaceAction("MoveTabRight", SplitAction.MoveRight())
            replaceAction("MoveTabDown", SplitAction.MoveBottom())

            registerAction("MoveTab.Forward", MoveTabAction.Backward())
            registerAction("MoveTab.Backward", MoveTabAction.Forward())
            registerAction("MoveTab.Start", MoveTabAction.Start())
            registerAction("MoveTab.End", MoveTabAction.End())

            registerAction("MoveTabToSplitter.Left", MoveTabToSplitterAction.Left())
            registerAction("MoveTabToSplitter.Top", MoveTabToSplitterAction.Top())
            registerAction("MoveTabToSplitter.Right", MoveTabToSplitterAction.Right())
            registerAction("MoveTabToSplitter.Bottom", MoveTabToSplitterAction.Bottom())
        }
    }
}
