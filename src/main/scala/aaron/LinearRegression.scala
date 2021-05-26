package aaron

import scala.language.implicitConversions

/**
 * Functions for linear regression
 */
object LinearRegression:

  /**
   * the 'bias' or 'offset' is  (aveY - A * aveX)
   *
   * where 'A' is the cross-correlation divided by the variance of the Xs:
   *
   * Σ (x - aveX)(y - aveY)  # cross-correlation
   * ----------------------
   * Σ (x - aveX) ^ 2        # variance (ish)
   *
   *
   * @param points
   * @return
   */
  def regressionCoefficient(points: List[Point]) =
    val xs = points.map(_.x)
    val ys = points.map(_.y)
    val aveX = xs.sum / points.size
    val aveY = points.map(_.y).sum / points.size

    val crossCorrelation = points.foldLeft(0.0) {
      case (total, Point(x, y)) =>
        val next = (x - aveX) * (y - aveY)
        total + next
    }

    val demoninator = xs.map(x => Math.pow(x - aveX, 2)).sum
    crossCorrelation / demoninator

  def correlationCoefficient(points: List[Point]): Double =
    val xs = points.map(_.x)
    val ys = points.map(_.y)
    val aveX = xs.sum / points.size
    val aveY = points.map(_.y).sum / points.size

    val deviations = points.map {
      case Point(x, y) => (x - aveX) * (y - aveY)
    }.sum
    val xVariance = Math.sqrt(xs.map(x => Math.pow((x - aveX), 2)).sum)
    val yVariance = Math.sqrt(ys.map(y => Math.pow((y - aveY), 2)).sum)

    deviations / (xVariance * yVariance)

  case class BestFit(a: Double, b: Double)

  case class Point(x: Double, y: Double)

  object Point:
    implicit def fromTuple(xy: (Int, Int)): Point = Point(xy._1, xy._2)

  def stdDev[N: Numeric](values: List[N]) =
    import Numeric.Implicits._
    val total = values.sum.toDouble
    val mean = total / values.size.toDouble
    val squares: Double = values.map(x => Math.pow(x.toDouble - mean, 2)).sum
    Math.sqrt(squares / values.size)

  def bestFit(points: List[Point]): BestFit =
    val xs = points.map(_.x)
    val ys = points.map(_.y)
    val aveX = xs.sum / points.size
    val aveY = points.map(_.y).sum / points.size
    val num = points.map {
      case Point(x, y) => (x - aveX) * (y - aveY)
    }.sum

    val denom = xs.map(x => (x - aveX) * (x - aveX)).sum

    val a = num / denom
    val b = aveY - (a * aveX)
    BestFit(a, b)

