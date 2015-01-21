import ppl.delite.framework
import ppl.delite.framework.DeliteApplication
import ppl.delite.framework.codegen.delite.DeliteCodeGenPkg
import ppl.delite.runtime
import ppl.delite.runtime._
import ppl.tests.scalatest
import ppl.tests.scalatest._
import scala.tools.nsc.io._
import scala.virtualization.lms.internal.{CudaCodegen, CCodegen}
import ppl.dsl.assignment2.{SimpleVectorApplicationRunner, SimpleVectorApplication, SimpleVectorExp, SimpleVectorCodegenScala, SimpleVectorCodegenCuda, SimpleVectorCodegenC}

class TestSuite extends DeliteSuite {

  def test1() {
    framework.Config.generateCpp = false
    framework.Config.generateCUDA = true
    runtime.Config.numCuda = 1
    runtime.Config.numCpp = 0
    runtime.Config.numThreads = 1

    println("\tRunning test1... framework.Config: " + framework.Config)

    trait Runner extends DeliteTestRunner with SimpleVectorApplicationRunner with DeliteTestModule

    val runner = new Runner {

      def func(x: Rep[Int]): Rep[Int] = {
        x
      }

      def main() {
        val v = DeliteArrayBuffer.fromFunction(10)(i => 1)
        val s = v.reduce(_ + _)(0) // s is referential primitive, passed into conditional block as input
        val x = Vector[Int](100) + 1 + s
        val y = Vector[Int](100) + 2 + s

        val z = x + y
        z.pprint

      }
    }

    val cf = runner.liftedMain _

    runner.registerFunction(cf)

    val stream = new java.io.PrintWriter(new java.io.FileWriter("out.deg"))
    val scala_emitter = new SimpleVectorCodegenScala{val IR: runner.type = runner}
    val cuda_emitter  = new SimpleVectorCodegenCuda {val IR: runner.type = runner}
    val cpp_emitter  = new SimpleVectorCodegenC {val IR: runner.type = runner}
    //cuda_emitter.emitSource(cf, "xxx", new java.io.PrintWriter(new java.io.FileWriter("out.cu")) )

    val emitters = List(scala_emitter, cuda_emitter, cpp_emitter)
    //val emitters = List(scala_emitter, cuda_emitter)
    lazy val deliteGenerator = new DeliteCodeGenPkg { val IR: runner.type = runner; val generators = emitters; }
    val prefix = "generated"
    val sep = java.io.File.separator
    val args = Array("out.deg", "42")
    def writeModules(baseDir: String) {
      Directory(Path(baseDir)).createDirectory()
      val writer = new java.io.FileWriter(baseDir + "modules.dm")
      writer.write("datastructures:\n")
      writer.write("kernels:datastructures\n")
      writer.close()
    }
    for (e <- emitters) {
      val base = prefix + sep + e.toString + sep
      writeModules(base)
      e.initializeGenerator(base + "kernels" + sep, args)
      e match {
        case e: CCodegen    => e.headerStream.println("#include \"DeliteCpp.h\"")
        case e: CudaCodegen => e.headerStream.println("#include \"DeliteCuda.h\"")
        case _ =>
      }

    }

    runner.emitRegisteredSource(deliteGenerator, stream)

    for (e <- emitters) {
      val base = prefix + sep + e.toString + sep
      e.emitDataStructures(base + "datastructures" + sep)
      e.finalizeGenerator()
    }

    emitters foreach { _.emitTransferFunctions()}

    Delite.embeddedMain(args, Map())
  }

}
