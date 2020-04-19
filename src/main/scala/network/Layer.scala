package network

import breeze.linalg.{DenseMatrix, DenseVector}
import breeze.numerics.sigmoid
import utils.AlgebraUtil

class Layer private(private val nodes: Int, private val left: Option[Layer] = None, private val right: Option[Layer] = None) {
  val isInputLayer: Boolean = left.isDefined
  val weightMatrixOp = left.map(x => AlgebraUtil.getWeightMatrix(nodes, x.nodes, isRandomFilled = true))
  val biasVectorOp: Option[DenseVector[Double]] = left.map(x => AlgebraUtil.getRandomVector(nodes))

  private def getZVector(activationsFromPreviousLayer: DenseVector[Double]): Option[DenseVector[Double]] = {
    val z: Option[DenseMatrix[Double]] = for {
      weights <- weightMatrixOp
      bias <- biasVectorOp
    } yield AlgebraUtil.add(AlgebraUtil.multiply(weights, activationsFromPreviousLayer), bias)
    z.map(_ (::, 0))
  }

  def getActivation(activationsFromPreviousLayer: DenseVector[Double]): (Option[DenseVector[Double]], Option[DenseVector[Double]]) = {
    val z = getZVector(activationsFromPreviousLayer)
    (z,z.map(_.map(sigmoid(_))))
  }


  def updateBias(delta:DenseVector[Double]):Unit ={
    for{
      b <- biasVectorOp
      _ = AlgebraUtil.updateVector(b,delta)
    }()
  }

  def updateWeight(delta:DenseVector[Double], activation:DenseVector[Double], eta: Double = 0.001)={
    weightMatrixOp.map(w => {
      val updatedWeight = delta.asDenseMatrix.t*activation.asDenseMatrix
      updatedWeight * w.map(x=> (x - eta))
    })
  }

}

object Layer {

  def apply(nodes: Int, layer: Layer): Layer = new Layer(nodes, Some(layer))
  def inputLayer(nodes: Int) = new Layer(nodes, None)


}