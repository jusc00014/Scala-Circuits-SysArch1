file://<WORKSPACE>/src/main/scala/arithmetic/ParallelUnit.scala
### java.lang.IndexOutOfBoundsException: 0

occurred in the presentation compiler.

presentation compiler configuration:
Scala version: 3.3.3
Classpath:
<HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/scala-lang/scala3-library_3/3.3.3/scala3-library_3-3.3.3.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/scala-lang/scala-library/2.13.12/scala-library-2.13.12.jar [exists ]
Options:



action parameters:
offset: 1431
uri: file://<WORKSPACE>/src/main/scala/arithmetic/ParallelUnit.scala
text:
```scala
package arithmetic

import chisel3._
import chisel3.util._

abstract class ComputationalUnit(width: Int) extends Module {
  val io = IO(new Bundle {
    val a = Input(UInt(width.W))
    val b = Input(UInt(width.W))
    val c = Output(UInt(width.W))
  })
}

class ParallelUnit(vectorSize: Int, arraySize: Int, unitWidth: Int, comp: (Int) => ComputationalUnit) extends Module {
  require(vectorSize % arraySize == 0)
  val io = IO(new Bundle {
    val a = Input(Vec(vectorSize, UInt(unitWidth.W)))
    val b = Input(Vec(vectorSize, UInt(unitWidth.W)))
    val start = Input(Bool())
    val done = Output(Bool())
    val c = Output(Vec(vectorSize, UInt(unitWidth.W)))
  })

  val units = Seq.fill(arraySize)(Module(comp(unitWidth)))

  // Default values for the outputs to avoid uninitialized references
  io.done := false.B
  for (i <- 0 until vectorSize) {
    io.c(i) := 0.U
  }
  for (i <- 0 until arraySize) {
    units(i).io.a := 0.U
    units(i).io.b := 0.U
  }

  // Initialize a counter to check the number of cycles
  val count = RegInit(0.U(log2Ceil(vectorSize / arraySize + 1).W))

  // Create a Mealy Machine to track the process
  val state = RegInit(0.U(2.W)) // 0 before start, 1 in process, 2 when done

  switch(state) {
    is(0.U) { // If calculation is waiting to start
      when(io.start) {
        state := 1.U
        count := 0.U
      }
    }
    is(1.U) { // If calculation is in process
      .setTimeout(@@)
      for (i <- 0 until arraySize) { // Iteration over the units
        // Connect the incoming signals
        val index = (count * arraySize.U + i.U)(log2Ceil(vectorSize) - 1, 0)
        units(i).io.a := io.a(index)
        units(i).io.b := io.b(index)
      }
      count := count + 1.U
      when(count === (vectorSize / arraySize).U) {
        state := 2.U
      }
    }
    is(2.U) { // If calculation has finished
      io.done := true.B
      // Collect the outgoing signals
      for (i <- 0 until arraySize) {
        val index = ((count - 1.U) * arraySize.U + i.U)(log2Ceil(vectorSize) - 1, 0)
        io.c(index) := units(i).io.c
      }
      when(!io.start) {
        state := 0.U
        io.done := false.B
      }
    }
  }

  // Default assignments to avoid uninitialized references
  when(state === 0.U || state === 2.U) {
    for (i <- 0 until arraySize) {
      units(i).io.a := 0.U
      units(i).io.b := 0.U
    }
  }
}

```



#### Error stacktrace:

```
scala.collection.LinearSeqOps.apply(LinearSeq.scala:131)
	scala.collection.LinearSeqOps.apply$(LinearSeq.scala:128)
	scala.collection.immutable.List.apply(List.scala:79)
	dotty.tools.dotc.util.Signatures$.countParams(Signatures.scala:501)
	dotty.tools.dotc.util.Signatures$.applyCallInfo(Signatures.scala:186)
	dotty.tools.dotc.util.Signatures$.computeSignatureHelp(Signatures.scala:94)
	dotty.tools.dotc.util.Signatures$.signatureHelp(Signatures.scala:63)
	scala.meta.internal.pc.MetalsSignatures$.signatures(MetalsSignatures.scala:17)
	scala.meta.internal.pc.SignatureHelpProvider$.signatureHelp(SignatureHelpProvider.scala:51)
	scala.meta.internal.pc.ScalaPresentationCompiler.signatureHelp$$anonfun$1(ScalaPresentationCompiler.scala:412)
```
#### Short summary: 

java.lang.IndexOutOfBoundsException: 0