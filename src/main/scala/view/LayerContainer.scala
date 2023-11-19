package view

import scalafx.scene.canvas.Canvas
import scalafx.scene.layout.Pane

class LayerContainer(val layer: Canvas) {
  private val bottomLayer = layer;

  private val layerPane: Pane = new Pane();
  layerPane.getChildren().add(bottomLayer);

  def add(additionalLayer: Canvas) = {
    layerPane.getChildren().add(additionalLayer);
  }

  def getView() = layerPane;
}
