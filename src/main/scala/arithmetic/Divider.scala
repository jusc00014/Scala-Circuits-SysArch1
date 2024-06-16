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
    
    val sumcollector = RegInit(0.U(bitWidth.W))     // the R' in the project description
    val counter = RegInit(0.U((bitWidth + 1).W))  
    val loopdone = RegInit(false.B)

    when(io.start ) {

        counter := (bitWidth - 1).U
        sumcollector  := 0.U
        remainder := 0.U
        loopdone := false.B
        divisor := io.divisor


        for (i <- 0 until bitWidth) {           //this is trying to initialise the quotient
            quotient(i) := io.dividend(i)
        }

        
        //quotient  := VecInit(io.dividend)


     }.elsewhen (!loopdone){

            sumcollector := remainder <<  1 
            sumcollector := sumcollector + quotient(counter)

            when (sumcollector < divisor){
                quotient(counter) :=  0.U
                remainder := sumcollector
            }.otherwise{
                quotient(counter) :=  1.U
                remainder := sumcollector - divisor
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