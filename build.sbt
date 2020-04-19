name := "ScalaNeuralNet"

version := "0.1"

scalaVersion := "2.11.5"

name := "Breeze1"
version := "1.0"
scalaVersion := "2.11.8"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.1" % "test"
libraryDependencies += "org.scalanlp" % "breeze_2.11" % "0.12"
libraryDependencies += "org.scalanlp" % "breeze-natives_2.11" % "0.12"
libraryDependencies += "org.slf4j" % "slf4j-api" % "1.7.22"
libraryDependencies += "org.slf4j" % "slf4j-simple" % "1.7.22"
resolvers += "Sonatype Releases" at "https://oss.sonatype.org/content/repositories/releases/"
fork in run := true
fork in test := true
javaOptions in run += "-Dcom.github.fommil.netlib.NativeSystemBLAS.natives=mkl_rt.dll"
javaOptions in test += "-Dcom.github.fommil.netlib.NativeSystemBLAS.natives=mkl_rt.dll"