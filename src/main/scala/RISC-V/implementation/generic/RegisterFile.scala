package RISCV.implementation.generic

import chisel3._

import RISCV.interfaces.generic.AbstractRegisterFile

class RegisterFile extends AbstractRegisterFile {
  val registers = RegInit(VecInit(Seq.fill(32)(0.U(32.W))))  //Initialisiere 32 Registe von je 32 Bit und setze sie auf 0

  //Lese Werte von rs1 und rs2
  io.reg_read_data1 := Mux(io.reg_rs1 =/= 0.U, registers(io.reg_rs1), 0.U(32.W))
  io.reg_read_data2 := Mux(io.reg_rs2 =/= 0.U, registers(io.reg_rs2), 0.U(32.W))

  when(~io_reset.rst_n) { //Reset? (Wenn null)
    registers := VecInit(Seq.fill(32)(0.U(32.W)))
  } .elsewhen(io.reg_write_en & io.reg_rd =/= 0.U) { //Schreiben? ist rd = 0?
    registers(io.reg_rd) := io.reg_write_data //Schreibe Wert in Register rd
  }
}