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

  private val gears: Map<Coordinate, Gear> = input.lines().mapIndexed { y, line ->
    line.mapIndexedNotNull { x, c ->
      if (c == '*') {
        Coordinate(x, y) to Gear()
      } else {
        null
      }
    }
  }.flatten().toMap()

  override fun silver(): Int = sanitizedInput.lines().windowed(3).map { it.toWindow() }.sumOf {w ->
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
  private fun Window.toBorderWindow(start: Int, length: Int): Window {
    val topSubstring = top.substring(start - 1, start + length + 1)
    val bottomSubstring = bottom.substring(start - 1, start + length + 1)
    val rightChar = middle[start - 1]
    val leftChar = middle[start + length]
    return Window(
      top = topSubstring,
      middle = bottomSubstring,
      bottom = rightChar.toString() + leftChar.toString()
    )
  }
  private fun List<String>.toWindow() = Window(top = first(), middle = get(1), bottom = last())
  data class Window(val top: String, val middle: String, val bottom: String) {
    fun prettyPrint() {
      println("""
        $top
        $middle
        $bottom
      """.trimIndent())
    }
  }
  data class Coordinate(val x: Int, val y: Int)
  data class Gear(val parts: List<Int> = emptyList())

  override fun gold(): Int {
    TODO("Not yet implemented")
  }

  private val Char.isNumber: Boolean
    get() = this in '0'..'9'
}

fun main() {
  println(Day03.silver())
}
