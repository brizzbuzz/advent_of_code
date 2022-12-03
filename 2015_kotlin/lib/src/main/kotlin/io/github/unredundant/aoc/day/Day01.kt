package io.github.unredundant.aoc.day

object Day01 : Day<Int, Int> {
  override val calendarDate: Int = 1

  override fun silver() = input.toCharArray().map { c ->
    when (c) {
      '(' -> 1
      ')' -> -1
      else -> 0
    }
  }.sum()

  // TODO Use accumulator?
  override fun gold(): Int {
    var position = 1
    val nonNegative = input.toCharArray().takeWhile {
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
