package RISCV.implementation.RV32M

import chisel3._
import chisel3.util._

import RISCV.interfaces.generic.AbstractExecutionUnit
import RISCV.model._


class MultiplicationUnit (/*genCU : => AbstractControlUnit, genDecoder : => AbstractDecoder*/) extends AbstractExecutionUnit {

    io.misa := "b01__0000__0_00000_00000_00100_00000_00000".U

    io.valid := false.B    
    io.stall := STALL_REASON.NO_STALL

    io_data <> DontCare
    io_reg <> DontCare
    io_pc <> DontCare
    io_reset <> DontCare
/*2.4 Maybe this helps: https://arxiv.org/pdf/2010.16171
    val decoder = Module(genDecoder)
    val control_unit = Module(genCU)
    
    decoder.io_decoder := io.instr
    io.stall := control_unit.io_ctrl.stall

    io_reg.reg_rs1 := decoder.io_decoder.rs1
    io_reg.reg_rs2 := decoder.io_decoder.rs2
    io_reg.reg_rd := decoder.io_decoder.rd */
    //TODO: Your solution to Problem 2.4 should go here
}
