package RISCV.implementation.RV32I

import chisel3._
import chisel3.util._

import RISCV.interfaces.RV32I.AbstractBranchUnit
import RISCV.model.RISCV_TYPE

class BranchUnit  extends AbstractBranchUnit{
    io_branch.branch_taken := false.B
  switch (io_branch.instr_type) {
    is (RISCV_TYPE.beq) {
        io_branch.branch_taken := io_branch.alu_out === 0.U //branch wenn branch_taken und ALU gibt 0
    }
    is (RISCV_TYPE.bne) {
        io_branch.branch_taken := io_branch.alu_out =/= 0.U //branch wenn branch_taken und ALU gibt nicht 0
    }
    is (RISCV_TYPE.blt) {
        io_branch.branch_taken := io_branch.alu_out === 1.U //branch wenn branch_taken und ALU gibt 1
    }
    is (RISCV_TYPE.bge) {
        io_branch.branch_taken := io_branch.alu_out === 0.U //branch wenn branch_taken und ALU gibt 0
    }
    is (RISCV_TYPE.bltu) {
        io_branch.branch_taken := io_branch.alu_out === 1.U //branch wenn branch_taken und ALU gibt 1
    }
    is (RISCV_TYPE.bgeu) {
        io_branch.branch_taken := io_branch.alu_out === 0.U //branch wenn branch_taken und ALU gibt 1
    }
  }
}