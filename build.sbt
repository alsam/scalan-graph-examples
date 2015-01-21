version := "0.0"

scalaVersion := "2.10.2"

scalaOrganization := "org.scala-lang.virtualized"

organization := "huawei-research"

scalacOptions += "-Yvirtualize"

scalacOptions += "-Yno-generic-signatures"

libraryDependencies += "EPFL" %% "lms" % "0.3-SNAPSHOT"

libraryDependencies += "com.huawei.scalan" %% "enterprise-edition" % "0.3.0-SNAPSHOT"

libraryDependencies += "stanford-ppl" %% "framework" % "0.1-SNAPSHOT"

libraryDependencies += "stanford-ppl" %% "runtime" % "0.1-SNAPSHOT"

libraryDependencies += "stanford-ppl" %% "delite-test" % "0.1-SNAPSHOT"

libraryDependencies += "org.scala-lang.virtualized" % "scala-library" % "2.10.2"

libraryDependencies += "org.scala-lang.virtualized" % "scala-compiler" % "2.10.2"

libraryDependencies += "org.scalatest" %% "scalatest" % "2.1.2"

libraryDependencies += "junit" % "junit" % "4.10" % "test"


scalaSource in Compile <<= baseDirectory(_ / "src")

scalaSource in Test <<= baseDirectory(_ / "tests")

retrieveManaged := true

