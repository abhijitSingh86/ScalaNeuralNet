package input

import java.io.{DataInputStream, FileInputStream}

import com.sun.tools.javac.util.ByteBuffer

import scala.io.Source

object MnsitDataReader {



  def readImageFile(fileName: String) = {
    val content = new DataInputStream(new FileInputStream("./src/main/resources/"+fileName))
    val magicNumber = content.readInt()
    val totalRows = content.readInt()
    val row = content.readInt()
    val column = content.readInt()

    val totalSize = row*column

    (0 until totalRows).map(x=>{
      val byteArr = new Array[Byte](totalSize)
      content.read(byteArr)
      byteArr
    })
  }

  def readLabelFile(fileName: String) = {
    val content = new DataInputStream(new FileInputStream("./src/main/resources/"+fileName))
    val magicNumber = content.readInt()
    val totalRows = content.readInt()
    (0 until totalRows).map(x=>{
      content.readByte()
    })
  }


  val TRAIN_IMAGE_FILE = "mnsit/train-images-idx3-ubyte"
  val TRAIN_LABEL_FILE = "mnsit/train-labels-idx1-ubyte"

}
