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
    
    val outputRegs = Reg(Vec(vectorSize, UInt(unitWidth.W)))
  val cycleCounter = RegInit(0.U(log2Ceil(vectorSize / arraySize).W))
  val processing = RegInit(false.B)
  val index = WireDefault(0.U(log2Ceil(vectorSize).W))  // Adjusted the width of the index
  //val indexForArray = WireDefault(0.U(vectorSize).W)

  // Set default values for the units' inputs
  for (i <- 0 until (arraySize  ) ) {
    units(i).io.a := 0.U
    units(i).io.b := 0.U
  }

  when(io.start) {
    cycleCounter := 0.U
    processing := true.B
    index := 0.U
    //indexForArray :=  vectorSize / arraySize

  }

  when(processing) {
    for (i <- 0 until (arraySize  )) {
      index := (cycleCounter + ((vectorSize.U / arraySize.U ) * i.U))
      units(i).io.a := io.a((cycleCounter + ((vectorSize.U / arraySize.U ) * i.U)))
      units(i).io.b := io.b((cycleCounter + ((vectorSize.U / arraySize.U ) * i.U)))
     outputRegs((cycleCounter + ((vectorSize.U / arraySize.U ) * i.U))) := units(i).io.c
//outputRegs(index) := units(i).io.c
    }

    cycleCounter := cycleCounter + 1.U

    when(cycleCounter === (vectorSize / arraySize).U - 1.U) {
        processing  :=  false.B
    }
  }

  io.done := !processing
  io.c := outputRegs
}

/*package arithmetic

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
    val count = RegInit(0.U(log2Ceil((vectorSize/arraySize)+1).W))
    //Default values for the outputs to avoid uninitialized references (AI)
    for (i <- 0 until vectorSize) {
        io.c(i) := 0.U
    }
    for (i <- 0 until arraySize) {
        units(i).io.a := 0.U
        units(i).io.b := 0.U
    }
    //Create a Mealy Machine to track the process
    val state = RegInit(0.U(2.W))   //State is 0 before start, 1 in process and 2 when done
    switch(state) {
        is(0.U){ //If calculation is waiting to start
            when(io.start){
                state := 1.U
                count := 0.U
            }
        }
        is(1.U) { //If calculation is in process
            for (i <- 0 until arraySize){   //Iteration over the units
            //Connect the incomming signals
            val y = (count * arraySize.U + i.U)(log2Ceil(vectorSize) -1, 0) //AI
            units(i).io.a := io.a(y) //as a = [a00; a01; a02; ...; a0arraySize; a10; a11; a12; ...]
            units(i).io.b := io.b(y)
            }
            count := count + 1.U
            when (count === 3.U)
            {
                state := 2.U
            }
        }
        is(2.U) { //If calculation has finished
            io.done := true.B
            //Give the outgoing Signals (debugged with AI)
            for (i <- 0 until arraySize){   //Iteration over the units
                val x = ((count - 1.U) * arraySize.U + i.U)(log2Ceil(vectorSize) -1, 0) //AI
                io.c(x) := units(i).io.c
            }
            when(! io.start) { //If restart
            state := 0.U
            io.done := false.B
            }
        }
    }
}
*/