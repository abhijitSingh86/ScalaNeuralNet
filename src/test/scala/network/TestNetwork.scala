package network

import java.io.{DataInputStream, FileInputStream}

import breeze.linalg.{DenseMatrix, DenseVector}
import input.MnsitDataReader
import unit.UnitSpec
import utils.AlgebraUtil

class TestNetwork  extends UnitSpec{

  "Network " should {
    " return input in one layer network " in {

      val inputData = AlgebraUtil.getWeightMatrix(1,4, true)

      val network = Network(List(Layer.inputLayer(4 )))

      val trainedNetwork:Network = network.learn(inputData, inputData)

      val result:DenseVector[Double] = trainedNetwork.predict(inputData(0,::).t)

      result should be (inputData(0,::).t)
    }
    "Neural network with 1 layer should return correct result" in {
      val inputData = AlgebraUtil.getWeightMatrix(1,10, true)
      val outputData = AlgebraUtil.getWeightMatrix(1,4, true)

      val inputLayer = Layer.inputLayer(10 )
      val hiddenLayer1 = Layer.apply(6,inputLayer)
      val output = Layer.apply(4,hiddenLayer1)


      val network = Network(List(inputLayer, hiddenLayer1, output))

      val trainedNetwork:Network = network.learn(inputData, outputData)

      val result:DenseVector[Double] = trainedNetwork.predict(inputData(0,::).t)
      result.size should be (4)
    }

    "Neural network predict data from MNSIT database" in {
      val inputData = DenseMatrix.apply(MnsitDataReader.readImageFile(MnsitDataReader.TRAIN_IMAGE_FILE).toList.map(x=>x.map(_.toDouble)):_*)
      val result = MnsitDataReader.readLabelFile(MnsitDataReader.TRAIN_LABEL_FILE).map(x=> {
        val a = Array.fill[Double](10)(0)
        a.update(x,1)
        a
      })
      val outputData = DenseMatrix(result:_*)

      val inputLayer = Layer.inputLayer(inputData.cols )
      val hiddenLayer1 = Layer.apply(100,inputLayer)
      val hiddenLayer2 = Layer.apply(50,hiddenLayer1)
      val output = Layer.apply(10,hiddenLayer2)


      val network = Network(List(inputLayer, hiddenLayer1, hiddenLayer2, output))

      network.learn(inputData,outputData)



    }
  }

}
