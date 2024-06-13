file://<WORKSPACE>/src/main/scala/arithmetic/ParallelUnit.scala
### java.lang.IndexOutOfBoundsException: 0

occurred in the presentation compiler.

presentation compiler configuration:
Scala version: 3.3.3
Classpath:
<HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/scala-lang/scala3-library_3/3.3.3/scala3-library_3-3.3.3.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/scala-lang/scala-library/2.13.12/scala-library-2.13.12.jar [exists ]
Options:



action parameters:
offset: 2083
uri: file://<WORKSPACE>/src/main/scala/arithmetic/ParallelUnit.scala
text:
```scala
package arithmetic

import chisel3._
import scala.reflect.ClassTag
import chisel3.util._
import chisel3.experimental.hierarchy.{Definition, Instance, instantiable, public}

abstract class ComputationalUnit(width: Int) extends Module {
    val io = IO(new Bundle {
        val a = Input(UInt(width.W))
        val b = Input(UInt(width.W))
        val c = Output(UInt(width.W))
    })  
}

class ParallelUnit(vectorSize: Int, arraySize: Int, unitWidth: Int, comp : (Int) => ComputationalUnit) extends Module {
    require(vectorSize % arraySize == 0)
    val io = IO(new Bundle {
        val a = Input(Vec(vectorSize, UInt(unitWidth.W)))
        val b = Input(Vec(vectorSize, UInt(unitWidth.W)))
        val start = Input(Bool())
        val done = Output(Bool())   //Done must be alternable
        val c = Output(Vec(vectorSize, UInt(unitWidth.W)))
    })

    val units = Seq.fill(arraySize)(Module(comp(unitWidth)))
    //1.3
    io.done := false.B
    //Initialize a counter to ckeck the number of cycles
    val count = Reg(UInt(log2Ceil((vectorSize/arraySize)+1).W))
    //Create a Mealy Machine to track the process
    val state = RegInit(0.U)   //State is 0 before start, 1 in process and 2 when done
    state := 0.U
    switch(state) {
        is(0.U){ //If calculation is waiting to start
            when(io.start){
            state = 1.U
            count := 0.U
            }
        }
        is(1.U) { //If calculation is in process
            for (i <- 0 until arraySize){   //Iteration over the units
            //Connect the incomming signals
            units(i).io.a := io.a(arraySize.U * count + i.U) //as a = [a00; a01; a02; ...; a0arraySize; a10; a11; a12; ...]
            units(i).io.b := io.b(arraySize.U * count + i.U)
            //Give the outgoing signal
            io.c(i) := units(i).io.c
            }
            count := count + 1.U
            when (count > (vectorSize/arraySize).U)
            {
                state := 2.U
            }
        }
        is(2.U) { //If calculation has finished
            io.done := true.B
            (@@)
            for (i <- 0 until arraySize){   //Iteration over the units
                val x = (count - 1.U) * arraySize.U + i.U
                io.c(x) := units(i).io.c
            }
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