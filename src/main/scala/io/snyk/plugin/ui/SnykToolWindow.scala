package io.snyk.plugin.ui

import com.intellij.openapi.Disposable
import com.intellij.openapi.actionSystem.{ActionGroup, ActionManager, DataProvider}
import com.intellij.openapi.project.{DumbAware, Project}
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.wm.{ToolWindow, ToolWindowFactory}
import com.intellij.util.ui.UIUtil
import io.snyk.plugin.SnykPluginProjectComponent
import io.snyk.plugin.ui.state.Navigator


/**
  * Top-level entry point to the plugin, as specified in `plugin.xml`.
  * Initialises the `SnykToolWindow` panel and registers it as content.
  */
class SnykToolWindowFactory extends ToolWindowFactory with DumbAware {
  override def createToolWindowContent(project: Project, toolWindow: ToolWindow): Unit = {
    val panel = new SnykToolWindow(project)
    val contentManager = toolWindow.getContentManager
    val content = contentManager.getFactory.createContent(panel, null, false)
    contentManager.addContent(content)
    Disposer.register(project, panel)
  }
}

class SnykToolWindow(project: Project) extends SimpleToolWindowPanel(true, true) with DataProvider with Disposable {
  this.setBackground(UIUtil.getPanelBackground)

  private val projectComponent = project.getComponent(classOf[SnykPluginProjectComponent])

  import projectComponent.pluginState

  pluginState.navigator := Navigator.newInstance(project, this, pluginState.idToBuildToolProject)

  val htmlPanel = new SnykHtmlPanel(project, pluginState)

  initialiseToolbar()
  setContent(htmlPanel)

  private[this] def initialiseToolbar(): Unit = {
    val actionManager = ActionManager.getInstance()
    val actionGroup = actionManager.getAction("io.snyk.plugin.ActionBar").asInstanceOf[ActionGroup]
    val actionToolbar = actionManager.createActionToolbar("Snyk Toolbar", actionGroup, true)

    setToolbar(actionToolbar.getComponent)
  }

  override def dispose(): Unit = {
  }
}

