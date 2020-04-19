package input

import unit.UnitSpec

class MnsitDataReaderTest  extends UnitSpec{

  "MNSIT data reader " should {
    "return correct image test data " in {
      val data = MnsitDataReader.readImageFile(MnsitDataReader.TRAIN_IMAGE_FILE)
      data.size should be(60000)
    }

    "return correct label data " in {
      val data = MnsitDataReader.readLabelFile(MnsitDataReader.TRAIN_LABEL_FILE)
      data.size should be (60000)
      data.headOption match {
        case s:Option[Byte] => //Nothing
        case _ => fail()
      }
    }
  }

}
