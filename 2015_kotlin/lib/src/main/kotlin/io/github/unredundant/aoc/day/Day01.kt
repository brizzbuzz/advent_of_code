package io.github.unredundant.aoc.day

import io.github.unredundant.aoc.util.Util.getInput

object Day01 : Day<Int, Int> {
  override val calendarDate: Int = 1

  override fun silver() = getInput(1).toCharArray().map { c ->
    when (c) {
      '(' -> 1
      ')' -> -1
      else -> 0
    }
  }.sum()


  // TODO Use fold?
  override fun gold(): Int {
    var position = 1
    val nonNegative = getInput(1).toCharArray().takeWhile {
      position += when (it) {
        '(' -> 1
        ')' -> -1
        else -> 0
      }
      position > 0
    }
    return nonNegative.count() + 1
  }
}
