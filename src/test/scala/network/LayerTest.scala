package network

import breeze.linalg.DenseVector
import unit.UnitSpec
import utils.AlgebraUtil

class LayerTest extends UnitSpec{

  "Layer " should {
    "update bias correctly" in{
      val layer = Layer.inputLayer(5)
      val layer1 = Layer.apply(4,layer)
      val newBias = AlgebraUtil.getRandomVector(4)
      val biasOp = layer1.biasVectorOp
      layer1.updateBias(newBias)
      biasOp.isDefined should be (true)
      biasOp.get.data should be (newBias.data)
    }

    "Correctly update the Weights as well"  in {
      val layer = Layer.inputLayer(5)
      val layer1 = Layer.apply(4,layer)
      val newBias = AlgebraUtil.getRandomVector(4)
      val weightOp = layer1.weightMatrixOp
      val activation = DenseVector.zeros[Double](5)
      layer1.updateWeight(newBias,activation,1)
      weightOp.isDefined should be (true)
      weightOp.get.rows should be (4)
      weightOp.get.cols should be (5)
      (0 until 4) foreach( x =>
      weightOp.get(x,::).inner.toArray should be (activation.toArray)
        )

    }
  }

}
