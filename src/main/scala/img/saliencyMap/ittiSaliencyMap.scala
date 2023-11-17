package img.saliencyMap

import img._
import _root_.img.filtering.GaborFilter

class ittiSaliencyMap() extends SaliencyMap() {
  def calcRImage(src: Image): Image = {
    val rPixels: Seq[Pixel] = for (pixel <- src.pixels) yield {
      val tmpValue = pixel.red - (pixel.green + pixel.blue) / 2
      val newValue = Math.max(tmpValue, 0.0)
      val newColor =
        0xff000000 + ((newValue * 255).toInt << 16) + ((newValue * 255).toInt << 8) + (newValue * 255).toInt

      new Pixel(pixel.x, pixel.y, newColor)
    }

    new Image(rPixels, src.width, src.height)
  }

  def calcGImage(src: Image): Image = {
    val gPixels: Seq[Pixel] = for (pixel <- src.pixels) yield {
      val tmpValue = pixel.green - (pixel.red + pixel.blue) / 2
      val newValue = Math.max(tmpValue, 0.0)
      val newColor =
        0xff000000 + ((newValue * 255).toInt << 16) + ((newValue * 255).toInt << 8) + (newValue * 255).toInt

      new Pixel(pixel.x, pixel.y, newColor)
    }

    new Image(gPixels, src.width, src.height)
  }

  def calcBImage(src: Image): Image = {
    val bPixels: Seq[Pixel] = for (pixel <- src.pixels) yield {
      val tmpValue = pixel.blue - (pixel.red + pixel.green) / 2
      val newValue = Math.max(tmpValue, 0.0)
      val newColor =
        0xff000000 + ((newValue * 255).toInt << 16) + ((newValue * 255).toInt << 8) + (newValue * 255).toInt

      new Pixel(pixel.x, pixel.y, newColor)
    }

    new Image(bPixels, src.width, src.height)
  }

  def calcYImage(src: Image): Image = {
    val yPixels: Seq[Pixel] = for (pixel <- src.pixels) yield {
      val tmpValue = (pixel.red + pixel.green) / 2 - Math.abs(
        pixel.red - pixel.green
      ) / 2 - pixel.blue
      val newValue = Math.max(tmpValue, 0.0)
      val newColor =
        0xff000000 + ((newValue * 255).toInt << 16) + ((newValue * 255).toInt << 8) + (newValue * 255).toInt

      new Pixel(pixel.x, pixel.y, newColor)
    }

    new Image(yPixels, src.width, src.height)
  }

  def calcColorSaliencyMap(src: Image): (Image, Image) = {
    val rImage = calcRImage(src)
    val gImage = calcGImage(src)
    val bImage = calcBImage(src)
    val yImage = calcYImage(src)

    val rPyramid = makePyramid(rImage)
    val gPyramid = makePyramid(gImage)
    val bPyramid = makePyramid(bImage)
    val yPyramid = makePyramid(yImage)

    val rgMap =
      ((rPyramid(0) - gPyramid(0)) - (rPyramid(1) - gPyramid(1)))
        .+((rPyramid(0) - gPyramid(0)) - (rPyramid(2) - gPyramid(2)))
        .+((rPyramid(1) - gPyramid(1)) - (rPyramid(2) - gPyramid(2)))
        .+((rPyramid(2) - gPyramid(2)) - (rPyramid(3) - gPyramid(3)))

    val byMap =
      ((bPyramid(0) - yPyramid(0)) - (bPyramid(1) - yPyramid(1)))
        .+((bPyramid(0) - yPyramid(0)) - (bPyramid(2) - yPyramid(2)))
        .+((bPyramid(1) - yPyramid(1)) - (bPyramid(2) - yPyramid(2)))
        .+((bPyramid(2) - yPyramid(2)) - (bPyramid(3) - yPyramid(3)))

    (rgMap, byMap)
  }

  def makeIntensityPyramid(orig: Image): Seq[Image] = {
    val pyramid: Seq[Image] = for (i <- (1 to 6)) yield {
      val upScale: Double = Math.pow(2, i).toDouble
      val downScale: Double = 1 / upScale
      val downScaleInterpolation =
        new BilinearInterpolation(downScale, downScale)
      val upScaleInterpolation = new BilinearInterpolation(upScale, upScale)

      val downscaledImage: Image = downScaleInterpolation.interpolate(orig)
      val gaussian = gaussianFilter.filtering(downscaledImage)
      val upScaledImage: Image =
        upScaleInterpolation.interpolate(gaussian)

      upScaledImage
    }

    orig +: pyramid
  }

  def calcIntensitySaliencyMap(src: Image): Image = {
    val pyramid = makePyramid(src)
    val out: Image =
      (pyramid(0) - pyramid(1))
        .+(pyramid(0) - pyramid(2))
        .+(pyramid(1) - pyramid(2))
        .+(pyramid(2) - pyramid(3))

    out.normalize()
  }

  def calcOrientationSaliencyMap(src: Image): (Image, Image, Image, Image) = {
    import scala.concurrent.{Future, Await}
    import scala.concurrent.duration.FiniteDuration
    implicit val ec: scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global

    val gaborFilter0 = new GaborFilter(11, 1.5, 1.2, 3, 0, 0)
    val gaborFilter45 = new GaborFilter(11, 1.5, 1.2, 3, 0, 45)
    val gaborFilter90 = new GaborFilter(11, 1.5, 1.2, 3, 0, 90)
    val gaborFilter135 = new GaborFilter(11, 1.5, 1.2, 3, 0, 135)

    val gaborFilterdImage0: Future[Image] = Future { gaborFilter0.filtering(src).normalize() }
    val gaborFilterdImage45: Future[Image] = Future{ gaborFilter45.filtering(src) }
    val gaborFilterdImage90: Future[Image] = Future { gaborFilter90.filtering(src) }
    val gaborFilterdImage135: Future[Image] = Future { gaborFilter135.filtering(src)}

    val pyramid0 = gaborFilterdImage0.map(makePyramid(_))
    val pyramid45 = gaborFilterdImage45.map(makePyramid(_))
    val pyramid90 = gaborFilterdImage90.map(makePyramid(_))
    val pyramid135 = gaborFilterdImage135.map(makePyramid(_))

    val map0 = pyramid0.map(pyramid0 =>
      ((pyramid0(0) - pyramid0(1)))
        .+((pyramid0(0) - pyramid0(2)))
        .+((pyramid0(1) - pyramid0(2)))
        .+((pyramid0(2) - pyramid0(3)))
    )

    val map45 = pyramid45.map(pyramid45 =>
      ((pyramid45(0) - pyramid45(1)))
        .+((pyramid45(0) - pyramid45(2)))
        .+((pyramid45(1) - pyramid45(2)))
        .+((pyramid45(2) - pyramid45(3)))
    )

    val map90 = pyramid90.map(pyramid90 =>
      ((pyramid90(0) - pyramid90(1)))
        .+((pyramid90(0) - pyramid90(2)))
        .+((pyramid90(1) - pyramid90(2)))
        .+((pyramid90(2) - pyramid90(3)))
    )

    val map135 = pyramid135.map(pyramid135 =>
      ((pyramid135(0) - pyramid135(1)))
        .+((pyramid135(0) - pyramid135(2)))
        .+((pyramid135(1) - pyramid135(2)))
        .+((pyramid135(2) - pyramid135(3)))
    )

    val result = Await.result(Future.sequence(Seq(map0, map45, map90, map135)), FiniteDuration(30, "seconds"))
    //(map0, map45, map90, map135)
    (result(0), result(1), result(2), result(3))
  }

  override def saliencyMap(orig: Image): Image = {
    val (rg, by) = calcColorSaliencyMap(orig)
    val colorMap: Image = rg.scaling(0.5) + by.scaling(0.5)
    val intensityMap: Image = calcIntensitySaliencyMap(orig.grayScale())
    val (map0, map45, map90, map135) =
      calcOrientationSaliencyMap(orig.grayScale())
    val orientationMap: Image =
      map0.scaling(0.25) + map45.scaling(0.25) + map90.scaling(0.25) + map135
        .scaling(0.25)

    val out = colorMap.scaling(0.333) + intensityMap.scaling(
      0.333
    ) + orientationMap.scaling(0.333)

    out
  }
}
