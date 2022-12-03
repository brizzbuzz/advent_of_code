package io.github.unredundant.aoc.day

import io.github.unredundant.aoc.util.Util.getInput

interface Day<SResult, GResult> {
  val calendarDate: Int
  val input: String
    get() = getInput(calendarDate)

  fun silver(): SResult
  fun gold(): GResult
}
