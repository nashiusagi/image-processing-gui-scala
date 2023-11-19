package view.subWindow

import scalafx.stage.Stage
import scalafx.scene.layout.Pane
import scalafx.scene.canvas.Canvas
import scalafx.scene.control.Slider
import scalafx.scene.control.Label
import scalafx.scene.control.Button
import scalafx.scene.image.WritableImage
import scalafx.embed.swing.SwingFXUtils
import scalafx.scene.layout.VBox
import scalafx.scene.Scene

import img.filtering.GaborFilter
import scalafx.scene.canvas.GraphicsContext

class DebugGaborKernelStage(image: img.Image, gc: GraphicsContext) {
  private val angleSliderWindow: Stage = new Stage();

  private val angleSlider: Slider = new Slider()
  angleSlider.setMin(0.0)
  angleSlider.setMax(180.0)
  angleSlider.setMajorTickUnit(45.0)
  angleSlider.setMinorTickCount(3)
  angleSlider.setShowTickLabels(true)
  angleSlider.setShowTickMarks(true)
  angleSlider.setPrefWidth(200)

  val gaborKernelCanvas = new Canvas(111, 111);

  val lblSliderValue: Label =
    new Label("gabor angle: " + angleSlider.getValue().toString + " [deg]")
  lblSliderValue.setPrefWidth(200);

  angleSlider.valueProperty.addListener {
    (
        o: javafx.beans.value.ObservableValue[_ <: Number],
        oldVal: Number,
        newVal: Number
    ) =>
      angleSlider.setValue(newVal.intValue())
      lblSliderValue.setText(
        "gabor angle: " + newVal.intValue().toString() + " [deg]"
      )
  }

  val applyGaborButton: Button = new Button("show kernel preview")
  applyGaborButton.setOnAction((_) -> {
    val angle = angleSlider.getValue()

    val gaborFilter = new GaborFilter(111, 10.0, 1.2, 10, 0, angle.toInt)
    val out = gaborFilter.kernelToImage

    val writableImageFiltered = new WritableImage(out.width, out.height)
    SwingFXUtils.toFXImage(out.image, writableImageFiltered)
    val gaborKernelGC = gaborKernelCanvas.graphicsContext2D
    gaborKernelGC.drawImage(writableImageFiltered, 0, 0)
  })

  val gaborFilteringButton: Button = new Button("filtering!")
  gaborFilteringButton.setOnAction((_) -> {
    val angle = angleSlider.getValue()

    angleSliderWindow.close()

    val gaborFilter = new GaborFilter(11, 1.5, 1.2, 3, 0, angle.toInt)
    val out = gaborFilter.filtering(image.grayScale()).normalize()

    val writableImageFiltered = new WritableImage(out.width, out.height)
    SwingFXUtils.toFXImage(out.image, writableImageFiltered)
    gc.drawImage(writableImageFiltered, 0, 0)
  })

  val sliderRoot = new VBox()
  sliderRoot.getChildren
    .addAll(
      angleSlider,
      lblSliderValue,
      applyGaborButton,
      gaborKernelCanvas,
      gaborFilteringButton
    )

  angleSliderWindow.setTitle("sample")
  val anglePane: Pane = new Pane()
  anglePane.setPrefSize(500, 250)
  anglePane.getChildren().addAll(sliderRoot)

  // angleSliderWindow.setScene(new Scene(200, 100))
  angleSliderWindow.setScene(new Scene(anglePane))

  def createStage() = angleSliderWindow;

}
