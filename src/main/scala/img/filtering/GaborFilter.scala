package img.filtering

import img._

class GaborFilter(
    ksize: Int,
    _sigma: Double,
    _gamma: Double,
    _lambda: Int,
    _psi: Int,
    _angle: Int
) extends Filter(ksize) {
  val sigma = _sigma
  val gamma = _gamma
  val lambda = _lambda
  val psi = _psi
  val angle = _angle

  override val kernel: List[Double] = calcGabor

  private def calcGabor: List[Double] = {
    val gaborResult: List[Double] =
      for (y <- (0 until kernelSize).toList; x <- (0 until kernelSize).toList)
        yield {
          val px = x - paddingSize
          val py = y - paddingSize

          // degree to radian
          val theta: Double = angle.toDouble / 180.0 * Math.PI

          val _x = Math.cos(theta) * px + Math.sin(theta) * py
          val _y = -1 * Math.sin(theta) * px + Math.cos(theta) * py

          Math.exp(
            -(_x * _x + gamma * gamma * _y * _y)
              / (2.0 * sigma * sigma)
          ) * Math.cos(2 * Math.PI * _x / lambda + psi)
        }

    val gaborResultSum =
      gaborResult.foldLeft(0.0)((acc, x) => acc + Math.abs(x))

    gaborResult.map(x => x / gaborResultSum)
  }

  def kernelToImage: Image = {
    val standardKernel = kernel.map(x => x - kernel.min)
    val kernelToPixels: List[Pixel] =
      for (i <- (0 until kernelSize * kernelSize).toList) yield {
        val x = i % kernelSize
        val y = i / kernelSize

        // normalize to [0.0, 1.0]
        val element: Double = standardKernel(i) / standardKernel.max

        val newColor =
          0xff000000 + ((element * 255).toInt << 16) + ((element * 255).toInt << 8) + (element * 255).toInt

        new Pixel(x, y, newColor)
      }

    new Image(kernelToPixels, kernelSize, kernelSize)
  }
}
