package io.snyk.plugin.ui.actions

import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.intellij.openapi.project.DumbAware
import io.snyk.plugin.services.SnykTaskQueueService

/**
 * Stop scan project with Snyk action.
 */
class SnykStopScanAction : AnAction(AllIcons.Actions.Suspend), DumbAware {

    override fun actionPerformed(actionEvent: AnActionEvent) {
        actionEvent.project!!.service<SnykTaskQueueService>().getCurrentProgressIndicator()?.cancel()
    }

    override fun update(actionEvent: AnActionEvent) {
        val indicator = actionEvent.project!!.service<SnykTaskQueueService>().getCurrentProgressIndicator()

        if (indicator != null) {
            actionEvent.presentation.isEnabled = indicator.isRunning
        }
    }
}
