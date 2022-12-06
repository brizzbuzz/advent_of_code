package io.github.unredundant.aoc.day

object Day05 : Day<String, String> {
  override val calendarDate: Int = 5

  override fun silver(): String {
    val (startingCrates, instructions) = input.parseInput()
    val result = applyInstructionsOneByOne(startingCrates, instructions)
    return result.crates.map { it.last() }.joinToString("")
  }

  override fun gold(): String {
    val (startingCrates, instructions) = input.parseInput()
    val result = applyInstructionsInChunks(startingCrates, instructions)
    return result.crates.map { it.last() }.joinToString("")
  }

  private data class Instruction(val count: Int, val start: Int, val end: Int)
  private data class CrateStack(val crates: List<List<Char>>)

  private fun applyInstructionsOneByOne(crateStack: CrateStack, instructions: List<Instruction>) =
    instructions.fold(crateStack) { crates, instruction ->
      crates.moveOneByOne(instruction)
    }

  private fun applyInstructionsInChunks(crateStack: CrateStack, instructions: List<Instruction>) =
    instructions.fold(crateStack) { crates, instruction ->
      crates.moveChunked(instruction)
    }

  private fun String.parseInput() = split("\n\n").let { (startingPosition, steps) ->
    val instructions = steps.lines().map { line ->
      val (_, c, s, e) = line.split("move ", " from ", " to ")
      Instruction(c.toInt(), s.toInt() - 1, e.toInt() - 1)
    }
    Pair(startingPosition.formulateStartingCrates(), instructions)
  }

  private fun String.formulateStartingCrates(): CrateStack {
    val columns = lines().last().split(" ").map { it.trim() }.filterNot { it.isEmpty() }.count()
    val container = Array(columns) { CharArray(lines().count()) { '-' } }
    val crates = lines().reversed().drop(1).map { it.chunked(4) }
    crates.forEachIndexed { i, r ->
      r.forEachIndexed { j, c ->
        if (c.isNotBlank()) {
          container[j][i] = c.replace("[", "").replace("] ", "").first()
        }
      }
    }
    return CrateStack(container.map { it.toList().filterNot { c -> c == '-' } }.toList())
  }

  private fun CrateStack.moveOneByOne(instruction: Instruction): CrateStack {
    val crates = this.crates.toMutableList()
    val movedCrates = crates[instruction.start].takeLast(instruction.count).reversed()
    crates[instruction.start] = crates[instruction.start].dropLast(instruction.count)
    crates[instruction.end] = crates[instruction.end] + movedCrates
    return CrateStack(crates)
  }

  private fun CrateStack.moveChunked(instruction: Instruction, maxChunkSize: Int = 3): CrateStack {
    val crates = this.crates.toMutableList()
    val movedCrates = crates[instruction.start].takeLast(instruction.count).reversed().chunked(maxChunkSize).flatten().reversed()
    crates[instruction.start] = crates[instruction.start].dropLast(instruction.count)
    crates[instruction.end] = crates[instruction.end] + movedCrates
    return CrateStack(crates)
  }
}
