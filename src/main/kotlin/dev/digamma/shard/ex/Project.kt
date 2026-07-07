package dev.digamma.shard.ex

import com.intellij.openapi.fileEditor.ex.FileEditorManagerEx
import com.intellij.openapi.project.Project

val Project.fileEditorManager
    get() = FileEditorManagerEx.getInstanceEx(this)
