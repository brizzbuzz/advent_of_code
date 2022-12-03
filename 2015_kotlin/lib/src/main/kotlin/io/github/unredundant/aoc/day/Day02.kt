package io.github.unredundant.aoc.day

object Day02 : Day<Int, Int> {
  override val calendarDate: Int = 2

  override fun silver(): Int = input.parseInput().sumOf { it.calculateWrappingPaper() }

  override fun gold(): Int = input.parseInput().sumOf { it.calculateRibbonLength() }

  private data class Box(val l: Int, val w: Int, val h: Int)

  private fun String.parseInput(): List<Box> = lines().map { line ->
    val (l, w, h) = line.split("x").map { it.toInt() }
    Box(l, w, h)
  }

  private fun Box.calculateWrappingPaper(): Int = 2 * (l * w + w * h + h * l) + minOf(l * w, w * h, h * l)

  private fun Box.calculateRibbonLength(): Int = 2 * (l + w + h - maxOf(l, w, h)) + l * w * h
}
