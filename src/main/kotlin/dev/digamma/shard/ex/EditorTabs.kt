package dev.digamma.shard.ex

import com.intellij.ui.tabs.impl.JBEditorTabs

val JBEditorTabs.isNavigable
    get() = !isHideTabs && targetInfo?.isHidden == false

val JBEditorTabs.allowsReordering
    get() = !isAlphabeticalMode()

val JBEditorTabs.visibleInfos
    get() = tabs.filterNot { it.isHidden }
