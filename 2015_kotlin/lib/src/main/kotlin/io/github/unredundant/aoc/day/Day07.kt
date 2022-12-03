package io.github.unredundant.aoc.day

object Day07 : Day<Short, Int> {
  override val calendarDate: Int = 7

  override fun silver(): Short {
    val circuits: MutableMap<String, Short> = mutableMapOf()
    var instructions = input.parseInstruction()
    return 0
  }

  override fun gold(): Int {
    TODO("Not yet implemented")
  }

  private enum class Operation {
    INPUT,
    AND,
    OR,
    LSHIFT,
    RSHIFT,
    NOT,
  }

  private data class Instruction(
    val operation: Operation,
    val input1: String,
    val input2: String?,
    val output: String,
  )

  private fun String.parseInstruction() = lines().map { line ->
    when {
      line.matches(Regex("NOT \\w+ -> \\w+")) -> {
        val (input, output) = line.split(" -> ")
        Instruction(Operation.NOT, input, null, output)
      }

      line.matches(Regex("\\w+ AND \\w+ -> \\w+")) -> {
        val (input1, input2, output) = line.split(" AND ", " -> ")
        Instruction(Operation.AND, input1, input2, output)
      }

      line.matches(Regex("\\w+ OR \\w+ -> \\w+")) -> {
        val (input1, input2, output) = line.split(" OR ", " -> ")
        Instruction(Operation.OR, input1, input2, output)
      }

      line.matches(Regex("\\w+ LSHIFT \\w+ -> \\w+")) -> {
        val (input1, input2, output) = line.split(" LSHIFT ", " -> ")
        Instruction(Operation.LSHIFT, input1, input2, output)
      }

      line.matches(Regex("\\w+ RSHIFT \\w+ -> \\w+")) -> {
        val (input1, input2, output) = line.split(" RSHIFT ", " -> ")
        Instruction(Operation.RSHIFT, input1, input2, output)
      }

      line.matches(Regex("\\w+ -> \\w+")) -> {
        val (input, output) = line.split(" -> ")
        Instruction(Operation.INPUT, input, null, output)
      }

      else -> throw IllegalArgumentException("Invalid instruction: $line")
    }
  }
}
