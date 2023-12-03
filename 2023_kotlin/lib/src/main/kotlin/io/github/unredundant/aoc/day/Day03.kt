package io.github.unredundant.aoc.day

object Day03 : Day<Int, Int> {

  override val calendarDate: Int = 3

  private val symbols = listOf('#', '$', '%', '&', '*', '+', '-', '_', '/', '=', '@')

  private val paddedInput = let {
    val lineLength = input.lines().first().length
    val padStr = ".".repeat(lineLength)
    padStr.plus("\n").plus(input).plus("\n").plus(padStr)
      .lines()
      .joinToString("\n") { ".$it." }
  }

  private val sanitizedInput = paddedInput.replace("-", "_")

  override fun silver(): Int = sanitizedInput.lines().windowed(3).map { it.toWindow() }.sumOf { w ->
    val result = w.middle.fold(Pair(0, 0)) { acc, c ->
      val (index, score) = acc

      if (index >= w.middle.length) {
        return@fold acc
      }

      var numberLength = 0
      while (w.middle[index + numberLength].isNumber) {
        numberLength++
      }

      if (numberLength == 0) {
        return@fold Pair(index + 1, score)
      }

      val borderWindow = w.toBorderWindow(index, numberLength)
      val number = w.middle.substring(index, index + numberLength).toInt()

      if (borderWindow.containsSymbol) {
        return@fold Pair(index + numberLength, score + number)
      }

      Pair(index + numberLength, score)
    }
    result.second
  }

  private val Window.containsSymbol: Boolean
    get() = symbols.any { s -> top.contains(s) || middle.contains(s) || bottom.contains(s) }

  // TODO Clean this up
  private fun Window.gearCoordinates(startX: Int, startY: Int): List<Coordinate> = top.mapIndexedNotNull { index, c ->
    if (c.isGear) {
      Coordinate(startX + index, startY)
    } else {
      null
    }
  } +
    middle.mapIndexedNotNull { index, c ->
      if (c.isGear) {
        Coordinate(startX + index, startY + 1)
      } else {
        null
      }
    } +
    bottom.mapIndexedNotNull { index, c ->
      if (c.isGear) {
        Coordinate(startX + index, startY + 2)
      } else {
        null
      }
    }


  private fun Window.toBorderWindow(start: Int, length: Int): Window {
    val topSubstring = top.substring(start - 1, start + length + 1)
    val middleSubstring = middle.substring(start - 1, start + length + 1)
    val bottomSubstring = bottom.substring(start - 1, start + length + 1)
    return Window(
      top = topSubstring,
      middle = middleSubstring,
      bottom = bottomSubstring,
    )
  }

  private fun List<String>.toWindow() = Window(top = first(), middle = get(1), bottom = last())
  data class Window(val top: String, val middle: String, val bottom: String) {
    fun prettyPrint() {
      println(
        """
        $top
        $middle
        $bottom
      """.trimIndent()
      )
    }
  }

  data class Coordinate(val x: Int, val y: Int)
  data object Gear

  private val Char.isGear: Boolean
    get() = this == '*'

  override fun gold(): Int = sanitizedInput.lines().windowed(3).map { it.toWindow() }.mapIndexed { y, w ->
    val result = w.middle.fold(Pair(0, emptyList<GearComponent>())) { acc, c ->
      val (index, gearComponents) = acc

      if (index >= w.middle.length) {
        return@fold acc
      }

      var numberLength = 0
      while (w.middle[index + numberLength].isNumber) {
        numberLength++
      }

      if (numberLength == 0) {
        return@fold Pair(index + 1, gearComponents)
      }

      val borderWindow = w.toBorderWindow(index, numberLength)
      val number = w.middle.substring(index, index + numberLength).toInt()
      val adjacentGearCoordinates = borderWindow.gearCoordinates(index, y)

      if (borderWindow.containsSymbol) {
        return@fold Pair(
          index + numberLength,
          gearComponents + adjacentGearCoordinates.map { GearComponent(it, number) })
      }

      Pair(index + numberLength, gearComponents)
    }
    result.second
  }
    .flatten()
    .groupBy { it.coordinate }
    .mapValues { (_, v) -> v.map { it.score } }
    .filterValues { it.size == 2 }
    .mapValues { (_, v) -> v[0] * v[1] }
    .values
    .sum()

  data class GearComponent(val coordinate: Coordinate, val score: Int)

  private val Char.isNumber: Boolean
    get() = this in '0'..'9'
}
