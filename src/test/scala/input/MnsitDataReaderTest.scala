package input

import unit.UnitSpec

import scala.collection.immutable

class MnsitDataReaderTest  extends UnitSpec{

  "MNSIT data reader " should {
    "return correct image test data " in {
      val data: immutable.Seq[Array[Byte]] = MnsitDataReader.readImageFile(MnsitDataReader.TRAIN_IMAGE_FILE)
      data.size should be(60000)
    }

    "return correct label data " in {
      val data: immutable.Seq[Byte] = MnsitDataReader.readLabelFile(MnsitDataReader.TRAIN_LABEL_FILE)
      data.size should be (60000)
      data.headOption match {
        case s:Option[Byte] => //Nothing
        case _ => fail()
      }
    }
  }

}
