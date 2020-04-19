package main

import breeze.linalg.DenseVector
import com.github.fommil.netlib.BLAS
import org.slf4j.LoggerFactory

object TestBreezeInstallation {
  def main(args:Array[String]): Unit = {
    println("Init logging...")
    System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "TRACE");
    val log = LoggerFactory.getLogger("main")
    log.trace("Starting...")
    val b = BLAS.getInstance()
    log.trace(s"BLAS = $b")
    val v = DenseVector(1,2,3,4)
    log.trace("Ending.")
  }
}