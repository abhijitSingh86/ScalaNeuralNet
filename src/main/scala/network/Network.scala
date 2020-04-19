package network

import breeze.linalg.{DenseMatrix, DenseVector, diag}
import breeze.numerics.sigmoid
import network.Network.Layers
import org.slf4j.LoggerFactory
import utils.AlgebraUtil

import scala.annotation.tailrec
import scala.collection.mutable.ArrayBuffer

class Network(layers: Layers) {

  private val logger = LoggerFactory.getLogger(this.getClass)



  def predict(inputData: DenseMatrix[Double]): DenseVector[Double] = {
    layers.foldLeft(None:Option[DenseVector[Double]])((A,b)=>{
      if(A.isDefined){
        b.getActivation(A.get)._2
      }else{
        b.getActivation(inputData(0,::).t)._2
      }
    }).getOrElse(inputData(::,1))
  }

  def learn(inputData: DenseMatrix[Double], resultMatrix: DenseMatrix[Double], epochs: Int = 1, batchSize: Int = 2): Network = {
    //    val layers = (0 to epochs) flatMap { epoch =>
    //      logger.info(s" Initiating ${epoch}  with ${inputData.rows / 2} batches of input.")
    //      (0 to inputData.rows / 2) flatMap { row =>
    //        forwardPass(inputData)
    //      }
    //    }

    if(layers.size <= 1){
     // Do nothing
    }else {
      pass(inputData, resultMatrix)
    }
    this
  }

  private def pass(initialActivation: DenseMatrix[Double], resultMatrix: DenseMatrix[Double]): Unit = {
    val activationsBuffer = ArrayBuffer[DenseVector[Double]](initialActivation(0,::).t)
    val zBuffer = ArrayBuffer[DenseVector[Double]]()
    // Forward pass
    for (l <- layers.tail) {
      l.getActivation(activationsBuffer.last) match {
        case (Some(z), Some(act)) =>
          zBuffer += z
          activationsBuffer += act
        case _ =>{
          throw new Exception("Calculation failed")
        }
      }

    }
    var delta: DenseVector[Double] = BackPropagation.costDerivative(activationsBuffer.last, resultMatrix(0,::).t)
    layers.last.updateBias(delta)
    layers.last.updateWeight(delta, activationsBuffer.last)

    val reversedLayer = layers.drop(1).take(layers.size-2).zip(zBuffer.take(zBuffer.size-1)).reverse
    var i = layers.size - 1
    for (rl <- reversedLayer) {
      val nextLayerWeight = layers(i).weightMatrixOp
      val sigPrime = rl._2.map(BackPropagation.sigmoidDerivative(_))
      delta = diag(AlgebraUtil.multiply(nextLayerWeight.get.t ,delta)(::,0)) * sigPrime
      rl._1.updateBias(delta)
      rl._1.updateWeight(delta, activationsBuffer(i - 1))
      i = i-1
    }

  }

  private def forwardPass(initialActivation: DenseMatrix[Double], resultMatrix: DenseMatrix[Double]): Layers = {
    // (0 until initialActivation.rows).map { row => {

    // @tailrec
    //      def propagateToLayers(remaining: Layers, ): DenseVector[Double] = {
    //        remaining.headOption match {
    //          case Some(currentLayer) =>
    //            // z = Weights*Activation + Bias
    //
    //            // Activation = Sigmoid(Z)
    //            val activation = z.map(_.map(sigmoid(_)))
    //            // For Tail recursion to operate correctly
    //            if (activation.isDefined) {
    //              propagateToLayers(remaining.tail, activation.get(::, 1))
    //            } else {
    //              // Last layer reached
    //              activationsFromPreviousLayer
    //            }
    //          case None =>
    //            activationsFromPreviousLayer
    //        }
    //      }
    //
    //      val result = propagateToLayers(layers, initialActivation(0, ::).t)
    //      BackPropagation.costDerivative(result,resultMatrix(row,::).t)
    //      //TODO add code to do back propagation
    //
    //    }
    //    }
    layers
  }
}

object Network {
  type Layers = List[Layer]

  def apply(layers: Layers): Network = new Network(layers)
}
