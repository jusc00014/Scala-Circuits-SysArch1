package RISCV.implementation.RV32I

import chisel3._
import chisel3.util._

import RISCV.model._
import RISCV.interfaces.RV32I.AbstractDecoder

class Decoder extends AbstractDecoder {
//Extrahiere opcode, funct3, funct7 und funct12
  val opcode = RISCV_OP(io_decoder.instr(6, 0))
  val funct3 = RISCV_FUNCT3(io_decoder.instr(14, 12))
  val funct7 = RISCV_FUNCT7(io_decoder.instr(31, 25))
  val funct12 = RISCV_FUNCT12(io_decoder.instr(31, 20))

//Extrahiere Src und Dst register
  val RS1 = io_decoder.instr(19, 15)
  val RS2 = io_decoder.instr(24, 20)
  val RD = io_decoder.instr(11, 7)

//Definiere Standardwerte für die Ausgabe des Decoders
  // define default values
  io_decoder.valid := false.B
  io_decoder.instr_type := RISCV_TYPE.addi
  io_decoder.rs1 := 0.U
  io_decoder.rs2 := 0.U
  io_decoder.rd := 0.U
  io_decoder.imm := 0.U

//Decode je nach opcode
  switch(opcode) {
    is(RISCV_OP.OP) { //R-Type
      io_decoder.instr_type := RISCV_TYPE(opcode.asUInt ## funct3.asUInt ## funct7.asUInt)
      io_decoder.valid := opcode =/= RISCV_OP.UNKNOWN && funct7 =/= RISCV_FUNCT7.UNKNOWN && io_decoder.instr_type =/= RISCV_TYPE.UNKNOWN
      io_decoder.rs1 := RS1
      io_decoder.rs2 := RS2
      io_decoder.rd := RD
      io_decoder.imm := Fill(32, 0.U)
    }
    is(RISCV_OP.OP_IMM) { //I-Type
      io_decoder.instr_type := RISCV_TYPE(opcode.asUInt ## funct3.asUInt ## Fill(7, 0.U))
      io_decoder.valid := opcode =/= RISCV_OP.UNKNOWN && io_decoder.instr_type =/= RISCV_TYPE.UNKNOWN
      io_decoder.rs1 := RS1
      io_decoder.rs2 := 0.U
      io_decoder.rd := RD
      io_decoder.imm := Fill(20, io_decoder.instr(31)) ## io_decoder.instr(31, 20)
    }
    is(RISCV_OP.LUI) {  //LUI (Load upper immediate)
      io_decoder.instr_type := RISCV_TYPE(opcode.asUInt ## Fill(3, 0.U) ## Fill(7, 0.U))
      io_decoder.valid := opcode =/= RISCV_OP.UNKNOWN && io_decoder.instr_type =/= RISCV_TYPE.UNKNOWN
      io_decoder.rs1 := 0.U
      io_decoder.rs2 := 0.U
      io_decoder.rd := RD
      io_decoder.imm := io_decoder.instr(31, 12) ## Fill(12, 0.U)
    }
    is(RISCV_OP.AUIPC) {  //AUIPC (Add upper immediate to pc)
      io_decoder.instr_type := RISCV_TYPE(opcode.asUInt ## Fill(3, 0.U) ## Fill(7, 0.U))
      io_decoder.valid := opcode =/= RISCV_OP.UNKNOWN && io_decoder.instr_type =/= RISCV_TYPE.UNKNOWN
      io_decoder.rs1 := 0.U
      io_decoder.rs2 := 0.U
      io_decoder.rd := RD
      io_decoder.imm := io_decoder.instr(31, 12) ## Fill(12, 0.U)
    }
    is(RISCV_OP.BRANCH) { //B-Type
      io_decoder.instr_type := RISCV_TYPE(opcode.asUInt ## funct3.asUInt ## Fill(7, 0.U))
      io_decoder.valid := opcode =/= RISCV_OP.UNKNOWN && io_decoder.instr_type =/= RISCV_TYPE.UNKNOWN
      io_decoder.rs1 := RS1
      io_decoder.rs2 := RS2
      io_decoder.rd := 0.U
      io_decoder.imm := Fill(19, io_decoder.instr(31)) ## io_decoder.instr(31) ## io_decoder.instr(7) ## io_decoder.instr(30, 25) ## io_decoder.instr(11, 8) ## Fill(1, 0.U)
    }
    is(RISCV_OP.STORE) {  //S-Type
      io_decoder.instr_type := RISCV_TYPE(opcode.asUInt ## funct3.asUInt ## Fill(7, 0.U))
      io_decoder.valid := opcode =/= RISCV_OP.UNKNOWN && io_decoder.instr_type =/= RISCV_TYPE.UNKNOWN
      io_decoder.rs1 := RS1
      io_decoder.rs2 := RS2
      io_decoder.rd := 0.U
      io_decoder.imm := Fill(20, io_decoder.instr(31)) ## io_decoder.instr(31, 25) ## io_decoder.instr(11, 7)
    }
  }
}
