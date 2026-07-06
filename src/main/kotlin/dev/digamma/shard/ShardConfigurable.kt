package dev.digamma.shard

import com.intellij.openapi.options.BoundSearchableConfigurable
import com.intellij.ui.dsl.builder.bindItem
import com.intellij.ui.dsl.builder.panel
import com.intellij.ui.dsl.builder.toNullableProperty
import com.intellij.ui.dsl.listCellRenderer.textListCellRenderer
import dev.digamma.shard.ShardBundle.message

class ShardConfigurable : BoundSearchableConfigurable(message("settings.name"), message("settings.name")) {
    private val settings
        get() = ShardSettings.getState()

    override fun createPanel() = panel {
        group(message("settings.splitters.title")) {
            row(message("settings.focus.strategy.label")) {
                comboBox(ShardSettings.FocusStrategy.entries, textListCellRenderer { it?.text })
                    .bindItem(settings::focusStrategy.toNullableProperty())
                    .apply {
                        comment(component.item.description)
                        component.addItemListener { comment?.text = component.item.description }
                    }
            }
        }
    }
}
