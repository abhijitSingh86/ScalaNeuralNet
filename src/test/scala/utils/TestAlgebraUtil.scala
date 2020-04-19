package utils

import breeze.linalg.{DenseMatrix, DenseVector}
import unit.UnitSpec

class TestAlgebraUtil extends UnitSpec {

  " AlgebraUtils" should {

    "return normal distribution weight matrix " in {
      val vector = AlgebraUtil.getRandomVector(10)
      vector.size should be(10)
    }

    "Return weight matrix with Random data " in {
      val rows = 3
      val cols = 5
      val isRandomFilled = true
      val matrix = AlgebraUtil.getWeightMatrix(rows,cols, isRandomFilled)
      matrix.rows should be (rows)
      matrix.cols should be (cols)
    }

    "Return correct multiplication of weights and inputs " in {
      val input = AlgebraUtil.getRandomVector(5)
      val weight = AlgebraUtil.getWeightMatrix(4,5,true)
      val multiplyResult = AlgebraUtil.multiply(weight,input)
      multiplyResult.rows should be (4)
      multiplyResult.cols should be (1)
    }

    "Validate the multiplication for understanding" in {
      val input = DenseVector[Double](1,2,3)
      val weights = DenseMatrix.zeros[Double](2,3)
      weights(0,::) := DenseVector[Double](2,3,4).t
      weights(1,::) := DenseVector[Double](5,5,5).t
      val res = AlgebraUtil.multiply(weights,input)
      res(0,0) should be (20)
      res(1,0) should be (30)
    }

    "return matrix vector addition" in {
      val weights = DenseMatrix.zeros[Double](2,1)
      weights(0,::) := DenseVector[Double](2).t
      weights(1,::) := DenseVector[Double](5).t
      val bias = DenseVector[Double](1,1)
      val res:DenseMatrix[Double] = AlgebraUtil.add(weights,bias)
      res(0,0) should be(3)
      res(1,0) should be(6)
    }

    " retrun a updated vector" in {
      val orig = AlgebraUtil.getRandomVector(5)
      val updatedV = AlgebraUtil.getRandomVector(5)
      AlgebraUtil.updateVector(orig,updatedV)
      orig.data should be(updatedV.data)
    }
  }

}
