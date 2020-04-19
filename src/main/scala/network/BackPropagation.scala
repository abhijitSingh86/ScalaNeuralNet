package network

import breeze.linalg.DenseVector
import breeze.numerics.sigmoid
import org.slf4j.LoggerFactory

object BackPropagation {
  private val logger = LoggerFactory.getLogger(getClass.getName)

  def sigmoidDerivative(v: Double): Double = {
    sigmoid(v) * (1 - sigmoid(v))
  }

  def costDerivative(result: DenseVector[Double], expected: DenseVector[Double]): DenseVector[Double] = {
    val diff: DenseVector[Double] = result - expected
    logger.debug(s"cost vector is ${diff}")
    diff
  }

}
