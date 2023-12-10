package io.github.unredundant.aoc.util

import io.github.unredundant.aoc.day.Day
import kotlin.system.measureNanoTime
import kotlin.system.measureTimeMillis

object Util {
  fun getInput(day: Int): String {
    return this::class.java.classLoader
      ?.getResource("day_${day.toString().padStart(2, '0')}.txt")
      ?.readText()
      ?: error("Unable to locate file 👀")
  }

  fun <S, G> printDayResults(day: Day<S, G>) {
    val silverTime = measureNanoTime {
      val silverResult = day.silver()
      println("Day ${day.calendarDate} Silver: $silverResult")
    }
    val goldTime = measureNanoTime {
      val goldResult = day.gold()
      println("Day ${day.calendarDate} Gold:   $goldResult")
    }
    println("Day ${day.calendarDate} Silver: ${silverTime.nanoToMilli()}ms")
    println("Day ${day.calendarDate} Gold:   ${goldTime.nanoToMilli()}ms")
    println("-".repeat(30))
  }

  private fun Long.nanoToMilli(): String {
    return "%.3f".format(toDouble() / 1_000_000)
  }
}
