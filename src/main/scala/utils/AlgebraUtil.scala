package utils

import breeze.linalg.{DenseMatrix, DenseVector, Transpose}
import breeze.stats.distributions.Poisson

object AlgebraUtil {
  def add(weights: DenseMatrix[Double], bias: DenseVector[Double]): DenseMatrix[Double] = {
    weights + bias.asDenseMatrix.t
  }


  def multiply(weight: DenseMatrix[Double], input: DenseVector[Double]):DenseMatrix[Double] = {

    weight * input.asDenseMatrix.t
  }

  def getWeightMatrix(rows: Int, cols: Int, isRandomFilled: Boolean = false): DenseMatrix[Double] = {
    val matrix = new DenseMatrix[Double](rows,cols)
    if(isRandomFilled){
      (0 until rows).foreach( (x:Int) => {
        matrix(x, ::) := DenseVector(getRandomPoissonDistribution(cols):_*).t
      })
    }
    matrix
  }

  def getRandomVector(size: Int, flipCoins:Int =5): DenseVector[Double] = {
    val sample: _root_.scala.collection.IndexedSeq[Double] = getRandomPoissonDistribution(size, flipCoins)
    DenseVector(sample:_*)
  }

  private def getRandomPoissonDistribution(size: Int, flipCoins: Int =5) = {
    val poi = new Poisson(flipCoins)
    val sample = poi.sample(size).map(x => poi.probabilityOf(x))
    sample
  }

  def updateVector(orig:DenseVector[Double], updated:DenseVector[Double])={
      orig := updated
  }
}
