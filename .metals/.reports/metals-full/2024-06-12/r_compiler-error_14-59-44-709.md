file://<WORKSPACE>/src/main/scala/arithmetic/ParallelUnit.scala
### java.lang.IndexOutOfBoundsException: 0

occurred in the presentation compiler.

presentation compiler configuration:
Scala version: 3.3.3
Classpath:
<HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/scala-lang/scala3-library_3/3.3.3/scala3-library_3-3.3.3.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/scala-lang/scala-library/2.13.12/scala-library-2.13.12.jar [exists ]
Options:



action parameters:
offset: 900
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
        val done = Output(Bool())
        val c = Output(Vec(vectorSize, UInt(unitWidth.W)))
    })

    val units = Seq.fill(arraySize)(Module(comp(unitWidth)))
    for  (@@)

    ??? // TODO: implement Problem 1.3 here
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