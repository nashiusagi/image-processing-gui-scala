package view.menu

import img.Image
import view.menu.menuItem._
import scalafx.scene.canvas.GraphicsContext
import scalafx.scene.control.Menu

class DebugMenu(image: Image, gc: GraphicsContext) {
  private val debugMenu: Menu = new Menu("_Debug")

  private val debugGrayScaleMenuItem =
    new DebugGrayScaleMenuItem(image, gc).getView()
  private val debugGaborKernelMenuItem =
    new DebugGaborKernelMenuItem(image, gc).getView()

  debugMenu
    .getItems()
    .addAll(debugGrayScaleMenuItem, debugGaborKernelMenuItem)

  def getView() = debugMenu;
}
