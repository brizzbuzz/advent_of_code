package io.github.unredundant.aoc.day

import kotlin.math.ceil


object Day10 : Day<Int, Int> {
  override val calendarDate: Int = 10
  val map = input.lines().mapIndexed { y, l -> l.mapIndexed { x, c -> Coordinate(x, y) to c } }.flatten()
    .associate { it.first to it.second }

  private val startingPoint = map.filterValues { it == 'S' }.keys.first()
  private val startingNeighbors = startingPoint.findValidNeighbors()
  private val pipeMap = startingPoint.travel(startingNeighbors.first(), 0, mapOf(startingPoint to Pair(0, 'S')))

  private fun Coordinate.findValidNeighbors(): Set<Direction> {
    val valid = mutableSetOf<Direction>()
    if (map[west()] == 'F' || map[west()] == 'L' || map[west()] == '-') valid += Direction.WEST
    if (map[east()] == 'J' || map[east()] == '7' || map[east()] == '-') valid += Direction.EAST
    if (map[north()] == '7' || map[north()] == 'F' || map[north()] == '|') valid += Direction.NORTH
    if (map[south()] == 'J' || map[south()] == 'L' || map[south()] == '|') valid += Direction.SOUTH
    return valid.toSet()
  }

  private tailrec fun Coordinate.travel(
    comingFrom: Direction,
    step: Int,
    path: Map<Coordinate, Pair<Int, Char>>
  ): Map<Coordinate, Pair<Int, Char>> {
    val newCoords = when (comingFrom) {
      Direction.EAST -> east()
      Direction.SOUTH -> south()
      Direction.NORTH -> north()
      Direction.WEST -> west()
    }

    val newStep = step + 1

    if (map[newCoords] == 'S') return path

    val newDirection = when (map[newCoords]) {
      'F' -> if (comingFrom == Direction.WEST) Direction.SOUTH else Direction.EAST
      'L' -> if (comingFrom == Direction.WEST) Direction.NORTH else Direction.EAST
      '-' -> if (comingFrom == Direction.WEST) Direction.WEST else Direction.EAST
      'J' -> if (comingFrom == Direction.EAST) Direction.NORTH else Direction.WEST
      '7' -> if (comingFrom == Direction.EAST) Direction.SOUTH else Direction.WEST
      '|' -> if (comingFrom == Direction.NORTH) Direction.NORTH else Direction.SOUTH
      else -> error("Unhandled pipe at $newCoords: ${map[newCoords]}")
    }

    return newCoords.travel(newDirection, newStep, path + (newCoords to Pair(newStep, map[newCoords]!!)))
  }

  enum class Direction { EAST, SOUTH, NORTH, WEST }

  override fun silver(): Int = ceil(pipeMap.values.maxOf { it.first }.toDouble() / 2).toInt()

  override fun gold(): Int {
    return 0
  }

  data class Coordinate(val x: Int, val y: Int) {
    fun west() = Coordinate(x - 1, y)
    fun east() = Coordinate(x + 1, y)
    fun north() = Coordinate(x, y - 1)
    fun south() = Coordinate(x, y + 1)
  }
}
