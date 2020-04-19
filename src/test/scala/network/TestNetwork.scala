package network

import breeze.linalg.{DenseMatrix, DenseVector}
import unit.UnitSpec
import utils.AlgebraUtil

class TestNetwork  extends UnitSpec{

  "Network " should {
    " return input in one layer network " in {

      val inputData = AlgebraUtil.getWeightMatrix(1,4, true)

      val network = Network(List(Layer.inputLayer(4 )))

      val trainedNetwork:Network = network.learn(inputData, inputData)

      val result:DenseVector[Double] = trainedNetwork.predict(inputData)

      result should be (inputData(::,1))
    }
    "Neural network with 1 layer should return correct result" in {
      val inputData = AlgebraUtil.getWeightMatrix(1,10, true)
      val outputData = AlgebraUtil.getWeightMatrix(1,4, true)

      val inputLayer = Layer.inputLayer(10 )
      val hiddenLayer1 = Layer.apply(6,inputLayer)
      val output = Layer.apply(4,hiddenLayer1)


      val network = Network(List(inputLayer, hiddenLayer1, output))

      val trainedNetwork:Network = network.learn(inputData, outputData)

      val result:DenseVector[Double] = trainedNetwork.predict(inputData)
      result.size should be (4)

    }
  }

}
