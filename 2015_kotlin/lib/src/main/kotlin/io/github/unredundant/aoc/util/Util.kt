package io.github.unredundant.aoc.util

import io.github.unredundant.aoc.day.Day

object Util {
  fun getInput(day: Int): String {
    return this::class.java.classLoader
      ?.getResource("day_${day.toString().padStart(2, '0')}.txt")
      ?.readText()
      ?: error("Unable to locate file 👀")
  }

  fun <S, G> printDayResults(day: Day<S, G>) {
    println("-".repeat(30))
    println("Day ${day.calendarDate} Silver: ${day.silver()}")
    println("Day ${day.calendarDate} Gold:   ${day.gold()}")
    println("-".repeat(30))
  }
}
