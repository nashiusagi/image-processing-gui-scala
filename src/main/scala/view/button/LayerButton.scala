package view.button

import scalafx.scene.canvas.Canvas
import scalafx.scene.control.Button

class LayerButton(name: String, canvas: Canvas) {
  private val layerButton = new Button(name);
  layerButton.setOnAction((_) -> {
    canvas.toFront();
  })

  def getView() = layerButton;
}
