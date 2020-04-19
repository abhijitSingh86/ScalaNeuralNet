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



  def predict(inputData: DenseVector[Double]): DenseVector[Double] = {
    layers.foldLeft(None:Option[DenseVector[Double]])((A,b)=>{
      if(A.isDefined){
        b.getActivation(A.get)._2
      }else{
        b.getActivation(inputData)._2
      }
    }).getOrElse(inputData)
  }

  def evaluate(inputData: DenseMatrix[Double], resultMatrix: DenseMatrix[Double], dataLength: Int): Unit = {

    def maxIndex(v:DenseVector[Double]):Int ={
      v.data.indexOf(v.data.max)
    }

    val result = (dataLength until inputData.rows).foldLeft((0,0):Tuple2[Int,Int])((a,x)=>{
      val input = inputData(x,::).t
      val desiredOutput =  resultMatrix(x,::).t
      val result = predict(input)
      if(maxIndex(result) == maxIndex(desiredOutput)){
        (a._1+1,a._2)
      }else{
        (a._1,a._2+1)
      }
    })

    logger.info(s"correct percentage ${ (result._1+0.0) / (result._1.toDouble + result._2)}")

  }

  def learn(inputData: DenseMatrix[Double], resultMatrix: DenseMatrix[Double], epochs: Int = 1, batchSize: Int = 2): Network = {

    if(layers.size <= 1){
     // Do nothing
    }else {
      (0 to epochs) foreach { epoch =>
        val dataLength = inputData.rows /2
        logger.info(s" Initiating ${epoch}  with ${dataLength} batches of input.")
        (0 to dataLength) foreach { row =>
          pass(inputData(row,::).t, resultMatrix(row,::).t, dataLength)
        }
        logger.info(s"${epoch} completed. Evaluation results are below")
        // evaluate and print
        evaluate(inputData, resultMatrix , dataLength)
      }
    }
    this
  }

  private def pass(initialActivation: DenseVector[Double], resultVector: DenseVector[Double], totalSize:Int): Unit = {
    val activationsBuffer = ArrayBuffer[DenseVector[Double]](initialActivation)
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
    var delta: DenseVector[Double] = BackPropagation.costDerivative(activationsBuffer.last, resultVector)
    layers.last.updateBias(delta)
    layers.last.updateWeight(delta, activationsBuffer.last,1,0)

    val reversedLayer = layers.drop(1).take(layers.size-2).zip(zBuffer.take(zBuffer.size-1)).reverse
    var i = layers.size - 1
    for (rl <- reversedLayer) {
      val nextLayerWeight = layers(i).weightMatrixOp
      val sigPrime = rl._2.map(BackPropagation.sigmoidDerivative(_))
      delta = diag(AlgebraUtil.multiply(nextLayerWeight.get.t ,delta)(::,0)) * sigPrime
      rl._1.updateBias(delta)
      rl._1.updateWeight(delta, activationsBuffer(i - 1),totalSize)
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
