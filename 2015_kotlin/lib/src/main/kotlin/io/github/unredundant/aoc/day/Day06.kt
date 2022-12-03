package io.github.unredundant.aoc.day

object Day06 : Day<Int, Int> {
  override val calendarDate: Int = 6

  override fun silver(): Int {
    val lights = Array(1000) { BooleanArray(1000) { false } }
    input.parseInput().forEach { i ->
      i.execute { x, y ->
        when (i.command) {
          Command.ON -> lights[x][y] = true
          Command.OFF -> lights[x][y] = false
          Command.TOGGLE -> lights[x][y] = !lights[x][y]
        }
      }
    }
    return lights.sumOf { it.count { on -> on } }
  }

  override fun gold(): Int {
    val lights = Array(1000) { IntArray(1000) { 0 } }
    input.parseInput().forEach { i ->
      i.execute { x, y ->
        when (i.command) {
          Command.ON -> lights[x][y] = lights[x][y] + 1
          Command.OFF -> lights[x][y] = maxOf(lights[x][y] - 1, 0)
          Command.TOGGLE -> lights[x][y] = lights[x][y] + 2
        }
      }
    }
    return lights.sumOf { it.sum() }
  }

  private data class Position(val x: Int, val y: Int)

  private enum class Command {
    ON, OFF, TOGGLE;

    companion object {
      fun fromString(str: String) = when (str) {
        "turn on" -> ON
        "turn off" -> OFF
        "toggle" -> TOGGLE
        else -> throw IllegalArgumentException("Unknown command: $str")
      }
    }
  }

  private data class Instruction(val command: Command, val start: Position, val end: Position)

  private fun String.parseInput(): List<Instruction> {
    val regex = Regex("(turn on|turn off|toggle) (\\d+),(\\d+) through (\\d+),(\\d+)")
    return lines().map { line ->
      val (command, x1, y1, x2, y2) = regex.matchEntire(line)!!.destructured
      Instruction(Command.fromString(command), Position(x1.toInt(), y1.toInt()), Position(x2.toInt(), y2.toInt()))
    }
  }

  private fun Instruction.execute(block: (Int, Int) -> Unit) {
    for (x in start.x..end.x) {
      for (y in start.y..end.y) {
        block(x, y)
      }
    }
  }
}
