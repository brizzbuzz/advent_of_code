package io.github.unredundant.aoc.day

import io.github.unredundant.aoc.util.Util

object Day01 {

  fun silver() {
    val input = Util.getInput(1)
    val max = input.split("\n\n").maxOfOrNull { it.split("\n").sumOf { cals -> cals.toInt() } }
    println(max)
  }

  fun gold() {
    val input = Util.getInput(1)
    val topThreeCalories = input.split("\n\n")
      .map { it.split("\n").sumOf { cals -> cals.toInt() } }
      .sorted()
      .takeLast(3)
      .sum()
    print(topThreeCalories)
  }
}
