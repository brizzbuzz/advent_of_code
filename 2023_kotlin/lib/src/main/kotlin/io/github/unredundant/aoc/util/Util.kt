package io.github.unredundant.aoc.util

import io.github.unredundant.aoc.day.Day
import kotlin.system.measureTimeMillis

object Util {
  fun getInput(day: Int): String {
    return this::class.java.classLoader
      ?.getResource("day_${day.toString().padStart(2, '0')}.txt")
      ?.readText()
      ?: error("Unable to locate file 👀")
  }

  fun <S, G> printDayResults(day: Day<S, G>) {
    val silverTime = measureTimeMillis {
      val silverResult = day.silver()
      println("Day ${day.calendarDate} Silver: $silverResult")
    }
    val goldTime = measureTimeMillis {
      val goldResult = day.gold()
      println("Day ${day.calendarDate} Gold:   $goldResult")
    }
    println("Day ${day.calendarDate} Silver: ${silverTime}ms")
    println("Day ${day.calendarDate} Gold:   ${goldTime}ms")
    println("-".repeat(30))
  }
}
