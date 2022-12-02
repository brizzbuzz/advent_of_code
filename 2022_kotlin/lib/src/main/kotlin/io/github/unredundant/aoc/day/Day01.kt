package io.github.unredundant.aoc.day

import io.github.unredundant.aoc.util.Util

object Day01 : Day<Int, Int> {
  override val calendarDate: Int = 1

  override fun silver(): Int = Util.getInput(1)
    .getInventories()
    .maxOf { it.countElfCalories() }

  override fun gold(): Int = Util.getInput(1)
    .getInventories()
    .map { it.countElfCalories() }
    .sorted()
    .takeLast(3)
    .sum()

  private fun String.getInventories(): List<String> = this.split("\n\n")

  private fun String.countElfCalories(): Int = this.split("\n").sumOf { cals -> cals.toInt() }
}
