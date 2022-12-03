package io.github.unredundant.aoc.day

object Day07 : Day<UShort, UShort> {
  override val calendarDate: Int = 7

  override fun silver(): UShort {
    val circuits: MutableMap<String, UShort> = mutableMapOf()
    var instructions = input.parseInstruction()
    return evaluateInstructions(instructions, circuits)
  }

  override fun gold(): UShort {
    val circuits: MutableMap<String, UShort> = mutableMapOf("b" to silver())
    var instructions = input.parseInstruction().filterNot { it.output == "b"  }
    return evaluateInstructions(instructions, circuits)
  }

  private fun evaluateInstructions(instructions: List<Instruction>, circuits: MutableMap<String, UShort>): UShort {
    var instructions = instructions
    while (instructions.isNotEmpty()) {
      val (canBeEvaluated, rest) = instructions.partition { it.canBeEvaluated(circuits) }
      instructions = rest
      canBeEvaluated.forEach { it.evaluate(circuits) }
    }
    return circuits["a"]!!
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
  ) {
    fun canBeEvaluated(circuit: Map<String, UShort>): Boolean {
      return when (operation) {
        Operation.INPUT -> input1.toIntOrNull() != null || circuit.containsKey(input1)
        Operation.NOT -> circuit.containsKey(input1)
        Operation.AND, Operation.OR, Operation.LSHIFT, Operation.RSHIFT -> {
          (input1.toIntOrNull() != null || circuit.containsKey(input1))
            && (input2?.toIntOrNull() != null || circuit.containsKey(input2))
        }
      }
    }

    fun evaluate(circuit: MutableMap<String, UShort>) {
      circuit[output] = when (operation) {
        Operation.INPUT -> input1.toUShortOrNull() ?: circuit[input1]!!
        Operation.NOT -> circuit[input1]!!.inv()
        Operation.AND -> (input1.toUShortOrNull() ?: circuit[input1]!!) and(input2?.toUShortOrNull() ?: circuit[input2]!!)
        Operation.OR -> (input1.toUShortOrNull() ?: circuit[input1]!!) or (input2?.toUShortOrNull() ?: circuit[input2]!!)
        Operation.LSHIFT -> ((input1.toUShortOrNull() ?: circuit[input1]!!).toInt() shl input2!!.toInt()).toUShort()
        Operation.RSHIFT -> ((input1.toUShortOrNull() ?: circuit[input1]!!).toInt() shr input2!!.toInt()).toUShort()
      }
    }
  }

  private fun String.parseInstruction() = lines().map { line ->
    when {
      line.matches(Regex("NOT \\w+ -> \\w+")) -> {
        val (_, input, output) = line.split("NOT ", " -> ")
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
