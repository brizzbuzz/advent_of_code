package io.github.unredundant.aoc.day

interface Day<SResult, GResult> {
  val calendarDate: Int
  fun silver(): SResult
  fun gold(): GResult
}
