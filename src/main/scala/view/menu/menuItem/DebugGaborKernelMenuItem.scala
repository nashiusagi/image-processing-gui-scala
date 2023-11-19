package view.menu.menuItem

import scalafx.scene.control.MenuItem
import view.subWindow.DebugGaborKernelStage
import img.Image
import scalafx.scene.canvas.GraphicsContext
import scalafx.stage.Stage

class DebugGaborKernelMenuItem(image: Image, gc: GraphicsContext) {
  private val debugGaborKernelMenuItem: MenuItem = new MenuItem(
    "gabor filtering"
  );

  debugGaborKernelMenuItem.setOnAction((_) -> {
    val angleSliderWindow: Stage =
      new DebugGaborKernelStage(image, gc).createStage()
    angleSliderWindow.show();
  })

  def getView() = debugGaborKernelMenuItem;
}
