package io.github.unredundant.aoc.util

object Util {
  fun getInput(day: Int): String {
    return this::class.java.classLoader
      ?.getResource("day_${day.toString().padStart(2, '0')}.txt")
      ?.readText()
      ?: error("Unable to locate file 👀")
  }
}
