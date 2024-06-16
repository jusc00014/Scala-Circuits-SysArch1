file://<WORKSPACE>/src/main/scala/arithmetic/Divider.scala
### java.lang.IndexOutOfBoundsException: 0

occurred in the presentation compiler.

presentation compiler configuration:
Scala version: 3.3.3
Classpath:
<HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/scala-lang/scala3-library_3/3.3.3/scala3-library_3-3.3.3.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/scala-lang/scala-library/2.13.12/scala-library-2.13.12.jar [exists ]
Options:



action parameters:
offset: 887
uri: file://<WORKSPACE>/src/main/scala/arithmetic/Divider.scala
text:
```scala
package arithmetic

import chisel3._
import chisel3.util._

class Divider(bitWidth: Int) extends Module {
    val io = IO(new Bundle {
        val start = Input(Bool())
        val done = Output(Bool())
        val dividend = Input(UInt(bitWidth.W))
        val divisor = Input(UInt(bitWidth.W))
        val quotient = Output(UInt(bitWidth.W))
        val remainder = Output(UInt(bitWidth.W))

            // Debug ports
    //val sumcollector = Output(UInt(bitWidth.W))
    //val counter = Output(UInt((bitWidth + 1).W))
    })

    val remainder = RegInit(0.U(bitWidth.W))       //current remainder
    val quotient = RegInit(VecInit(Seq.fill(bitWidth)(0.U(1.W))))   //= {dividend[i:0], quotient[Nâˆ’1:i+1]}, where dividend is the input dividend and quotient is the final output quotient, and i is the current cycle
    val divisor = RegInit(0.U(bitWidth.W))         //divisor
    for(@@)
    
    val sumcollector = RegInit(0.U(bitWidth.W))     // the R' in the project description
    val counter = RegInit(0.U((bitWidth + 1).W))  
    val loopdone = RegInit(false.B)

    when(io.start ) {

        counter := (bitWidth - 1).U
        sumcollector  := 0.U
        remainder := 0.U
        loopdone := false.B


        for (i <- 0 until bitWidth) {           //this is trying to initialise the quotient
            quotient(i) := io.dividend(i)
        }

        
        //quotient  := VecInit(io.dividend)


     }.elsewhen (!loopdone){

            sumcollector := remainder <<  1 
            sumcollector := sumcollector + quotient(counter)

            when (sumcollector < io.divisor){
                quotient(counter) :=  0.U
                remainder := sumcollector
            }.otherwise{
                quotient(counter) :=  1.U
                remainder := sumcollector - io.divisor
            }

            counter := counter - 1.U

    when(counter === 0.U) {
      loopdone := true.B
    }
  }

  io.done := loopdone
  io.quotient := quotient.asUInt
  io.remainder := remainder
}

/*package arithmetic

import chisel3._
import chisel3.util._

class Divider(bitWidth: Int) extends Module {
    val io = IO(new Bundle {
        val start = Input(Bool())
        val done = Output(Bool())
        val dividend = Input(UInt(bitWidth.W))
        val divisor = Input(UInt(bitWidth.W))
        val quotient = Output(UInt(bitWidth.W))
        val remainder = Output(UInt(bitWidth.W))
    })

    val remainder = RegInit(0.U(bitWidth.W))       //current remainder
    val quotient = RegInit(VecInit(Seq.fill(bitWidth)(0.U(1.W))))   //= {dividend[i:0], quotient[N−1:i+1]}, where dividend is the input dividend and quotient is the final output quotient, and i is the current cycle
    val divisor = RegInit(0.U(bitWidth.W))         //divisor
    
    ??? // TODO: implement Problem 1.1 here
} */
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