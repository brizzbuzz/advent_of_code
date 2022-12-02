package io.github.unredundant.aoc.day

import io.github.unredundant.aoc.util.Util.getInput

object Day03 : Day<Int, Int> {
  override val calendarDate: Int = 3

  override fun silver(): Int {
    val input = getInput(3)
    val tracker: MutableMap<Position, Int> = mutableMapOf<Position, Int>().withDefault { 0 }
    var currPosition = Position(0, 0)
    tracker[currPosition] = tracker.getValue(currPosition) + 1
    input.toDirections().forEach { direction ->
      currPosition = currPosition.move(direction)
      tracker[currPosition] = tracker.getValue(currPosition) + 1
    }
    return tracker.filterValues { it > 0 }.size
  }

  override fun gold(): Int {
    TODO("Not yet implemented")
  }

  private data class Position(val x: Int, val y: Int) {
    fun move(direction: Direction) = when (direction) {
      Direction.NORTH -> copy(y = y + 1)
      Direction.SOUTH -> copy(y = y - 1)
      Direction.EAST -> copy(x = x + 1)
      Direction.WEST -> copy(x = x - 1)
    }
  }

  private enum class Direction {
    NORTH,
    SOUTH,
    EAST,
    WEST
  }

  private fun String.toDirections(): List<Direction> = this.toCharArray().map { c ->
    when (c) {
      '^' -> Direction.NORTH
      'v' -> Direction.SOUTH
      '>' -> Direction.EAST
      '<' -> Direction.WEST
      else -> throw IllegalArgumentException("Invalid direction: $c")
    }
  }
}
